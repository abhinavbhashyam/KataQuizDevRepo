package com.example.kataquiz;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kataquiz.databinding.ActivityTakeQuizBinding;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Handles the activities of the TakeQuizActivity
 */
public class TakeQuizActivity extends AppCompatActivity implements CardStackListener {

    // reference to Spinner on UI
    Spinner answerSpinner;

    List<Question> cardList = new ArrayList<>();

    ActivityTakeQuizBinding binding;    // allows us to easily reference UI components for this activity

    CardStackLayoutManager manager;     // manager for layout of card stack

    List<Question> wrong = new ArrayList<>();   // list of questions the quiz taker got wrong

    MCQuestionCardAdapter mcQuestionCardAdapter;    // reference to custom adapter for card
                                                    // (allows us to bind data to each card)
    CountDownTimer cdt;   // manager for count down timer that is on each card

    TextView cardTimerTV;   // timer TV that is on each card

    // timer variables
    long totalSeconds = 60;    // 1 min timer
    long warningSeconds = 10;   // after 10 seconds remaining timer will flash red
    long intervalSeconds = 1;   // counts down every second

    // formatting tool for timer
    SimpleDateFormat timerFormat = new SimpleDateFormat("mm:ss");

    /**
     * Manages all action when screen is loaded in/created
     * @param savedInstanceState unused
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_quiz);

        // get the layout binding of this activity
        binding = ActivityTakeQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // initialize cardstack layout manager
        manager = new CardStackLayoutManager(this, this);
        manager.setStackFrom(StackFrom.Top);    // set appearence of stack
        manager.setVisibleCount(3);    // 3 cards visible at a time
        manager.setDirections(Direction.HORIZONTAL);    // swiping horizontally
        manager.setCanScrollVertical(false);    // cannot scroll vertically

        // we need to retrieve the questions that were sent from the previous screen
        Bundle extras = getIntent().getBundleExtra("QUIZ_QUESTIONS");
        ArrayList<Question> questionsForQuiz = extras.getParcelableArrayList("QUIZ_QUESTIONS");

        Log.i("TEST INTENT", String.valueOf(questionsForQuiz.size()));

        // initialize the card stack view with the appropriate questions
        mcQuestionCardAdapter = new MCQuestionCardAdapter(questionsForQuiz);
        binding.cardStack.setLayoutManager(manager);
        binding.cardStack.setHasFixedSize(true);    // size of card doesn't change
        binding.cardStack.setAdapter(mcQuestionCardAdapter);
    }

    @Override
    public void onCardDragging(Direction direction, float ratio) {

    }

    @Override
    public void onCardSwiped(Direction direction) {

    }

    @Override
    public void onCardRewound() {

    }

    @Override
    public void onCardCanceled() {

    }

    /**
     * When a card appears, we start a timer. When that timer finishes, the card is automatically swiped
     * off the screen
     *
     * @param view     view object corresponding to current card
     * @param position where we are in the stack of cards (as an index)
     */
    @Override
    public void onCardAppeared(View view, int position) {
        // get reference to the timer
        cardTimerTV = view.findViewById(R.id.mcTimerTV);

        // we have an animation for the timer when its running out
        Animation a = AnimationUtils.loadAnimation(this, R.anim.blink);

        // initialize the countdown timer
        cdt = new CountDownTimer(totalSeconds * 1000, intervalSeconds * 1000) {
            // callback that is called every time the timer ticks
            @Override
            public void onTick(long millisUntilFinished) {
                // if there are less than 10 seconds remaining, flash timer in red color
                if (millisUntilFinished <= ((warningSeconds + 1) * 1000)) {
                    cardTimerTV.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
                    cardTimerTV.setTextColor(Color.RED);
                    cardTimerTV.startAnimation(a);
                } else {
                    // otherwise timer will stay green

                    // note: bottom if statement is required for version check
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        cardTimerTV.setTextColor(getColor(R.color.green));
                    }
                }

                // keep timer updated with current elapsed time
                cardTimerTV.setText(timerFormat.format(millisUntilFinished));
            }

            // callback that is called when timer ends
            @Override
            public void onFinish() {
                // update the timer accordingly
                cardTimerTV.setText(timerFormat.format(0));
                binding.cardStack.swipe();  // auto-swipe when time is up
                Toast.makeText(getApplicationContext(), "Time expired for question!", Toast.LENGTH_SHORT).show();
            }
        };

        cdt.start();    // start timer when card appears
    }

    @Override
    public void onCardDisappeared(View view, int position) {
        cdt.cancel(); // cancel timer; card has been swiped
    }
}