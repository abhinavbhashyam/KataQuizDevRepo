package com.example.kataquiz;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kataquiz.databinding.ActivityTakeQuizBinding;
import com.google.android.material.textfield.TextInputLayout;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles the activities of the TakeQuizActivity
 */
public class TakeQuizActivity extends AppCompatActivity implements CardStackListener {
    ActivityTakeQuizBinding binding;    // allows us to easily reference UI components for this activity

    CardStackLayoutManager manager;     // manager for layout of card stack

    QuestionCardAdapter mcQuestionCardAdapter;    // reference to custom adapter for card
                                                  // (allows us to bind data to each card)

    List<Question> wrongQuestions = new ArrayList<>();   // list of questions the quiz taker got wrong

    // list of questions in the quiz
    List<Question> quizQuestions = new ArrayList<>();

    CountDownTimer cdt;   // manager for count down timer that is on each card

    // timer variables
    long totalSeconds = 60;    // 1 min timer
    long warningSeconds = 10;   // after 10 seconds remaining timer will flash red
    long intervalSeconds = 1;   // counts down every second

    // formatting tool for timer
    SimpleDateFormat timerFormat = new SimpleDateFormat("m:ss");

    TextView cardTimerTV;   // timer TV that is on each card

    // reference to spinner
    TextInputLayout answerSpinner;



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
        manager.setVisibleCount(2);    // 2 cards visible at a time
        manager.setDirections(Direction.HORIZONTAL);    // swiping horizontally
        manager.setCanScrollVertical(false);    // cannot scroll vertically

        // we need to retrieve the questions that were sent from the previous screen
        Bundle extras = getIntent().getBundleExtra("QUIZ_QUESTIONS");
        quizQuestions = extras.getParcelableArrayList("QUIZ_QUESTIONS");

        // initialize the card stack view with the appropriate questions
        mcQuestionCardAdapter = new QuestionCardAdapter(quizQuestions, TakeQuizActivity.this);
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
        // enable spinner on card appearence
        AutoCompleteTextView currCardSpinner = view.findViewById(R.id.answerTV);

        currCardSpinner.setEnabled(true);

        // get reference to the timer
        cardTimerTV = view.findViewById(R.id.timerTV);

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

    /**
     * Handles all action that is taken when a user swipes a card or if time expires for question
     * @param view view object corresponding to current card
     * @param position where we are in list of cards (as an index)
     */
    @Override
    public void onCardDisappeared(View view, int position) {
        // get reference to relevant UI element (spinner)
        answerSpinner = view.findViewById(R.id.answerSP);

        // current question in quiz
        Question currentQuestion = quizQuestions.get(position);

        if (!answerSpinner.getEditText().getText().toString().isEmpty()) {
            // character corresponding to selected choice
            char selectedOption = answerSpinner.getEditText().getText().toString().charAt(0);

            String selectedAnswer = "";

            switch (selectedOption) {
                // if A is selected
                case 'A':
                    // want to get the answer that corresponds to that choice
                    EditText answer1ET = view.findViewById(R.id.answer1ET);
                    selectedAnswer = answer1ET.getText().toString();
                    break;
                // if B is selected
                case 'B':
                    // want to get the answer that corresponds to that choice
                    EditText answer2ET = view.findViewById(R.id.answer2ET);
                    selectedAnswer = answer2ET.getText().toString();
                    break;
                // if C is selected
                case 'C':
                    // want to get the answer that corresponds to that choice
                    EditText answer3ET = view.findViewById(R.id.answer3ET);
                    selectedAnswer = answer3ET.getText().toString();
                    break;
                // if D is selected
                case 'D':
                    // want to get the answer that corresponds to that choice
                    EditText answer4ET = view.findViewById(R.id.answer4ET);
                    selectedAnswer = answer4ET.getText().toString();
                    break;
            }

            // correct answer
            String correctAnswer = currentQuestion.getCorrectAnswer();

            // user selected incorrect answer for question
            if (!selectedAnswer.equals(correctAnswer)) {
                // add to our list of wrong questions
                wrongQuestions.add(currentQuestion);

                // display popup
                Toast.makeText(getApplicationContext(), "Incorrect!",
                        Toast.LENGTH_SHORT).show();
            } else {
                // display popup
                Toast.makeText(getApplicationContext(), "Correct!",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            // user did not select an answer, so they are wrong
            wrongQuestions.add(currentQuestion);

            Toast.makeText(getApplicationContext(), "Incorrect, no option chosen!", Toast.LENGTH_SHORT).show();
        }

        // if we reached last card
        if (position == quizQuestions.size() - 1) {
            Toast.makeText(getApplicationContext(), "You finished the quiz!", Toast.LENGTH_SHORT).show();
            if (wrongQuestions.size() != 0) {
                takeToPostQuizActivity(wrongQuestions);
            } else {
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                Toast.makeText(getApplicationContext(), "You got a perfect score! You will be " +
                        "taken back to the generate quiz screen momentarily.", Toast.LENGTH_LONG).show();
            }
        }
        cdt.cancel(); // cancel timer; card has been swiped
    }

    /**
     * Helper method to take us to the post quiz activity
     * @param wrongQuestions questions the user got wrong in their quiz
     */
    private void takeToPostQuizActivity(List<Question> wrongQuestions) {
        // intent taking us to the PostQuizActivity
        Intent intent = new Intent(getApplicationContext(), PostQuizActivity.class);

        // using Bundle to send over questions
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("INCORRECT_QUESTIONS", (ArrayList<? extends Parcelable>) wrongQuestions);

        // put Bundle into Intent
        intent.putExtra("INCORRECT_QUESTIONS", bundle);
        startActivity(intent);
    }
}