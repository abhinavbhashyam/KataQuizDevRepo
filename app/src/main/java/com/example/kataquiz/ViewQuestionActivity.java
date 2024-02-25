package com.example.kataquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ViewQuestionActivity extends AppCompatActivity {
    // get references to UI elements
    TextView questionNameTV, questionDiffAndCategoryTV;

    EditText answer1ET, answer2ET, answer3ET, answer4ET;

    TextView answer1TV, answer3TV, answer4TV;

    // question that the user has chosen to view (from previous screen)
    Question selectedQuestion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_question);

        // initialize UI references
        questionNameTV = findViewById(R.id.pqQuestionNameTV);
        questionDiffAndCategoryTV = findViewById(R.id.pqDisplayDiffAndCategoryTV);

        answer1ET = findViewById(R.id.pqAnswer1ET);
        answer2ET = findViewById(R.id.pqAnswer2ET);
        answer3ET = findViewById(R.id.pqAnswer3ET);
        answer4ET = findViewById(R.id.pqAnswer4ET);

        answer1TV = findViewById(R.id.pqChoiceATV);
        answer3TV = findViewById(R.id.pqChoiceCTV);
        answer4TV = findViewById(R.id.pqChoiceDTV);

        // receive data from intent
        selectedQuestion = getIntent().getParcelableExtra("ITEM");

        // set UI appropriately
        questionNameTV.setText(selectedQuestion.getQuestion());

        // enum difficulty into proper formatted string
        String difficultyString = selectedQuestion.getDifficulty().toString().toLowerCase();

        // initialize category content textview
        questionDiffAndCategoryTV.setText(selectedQuestion.getCategory() + " - " +
                difficultyString.substring(0, 1).toUpperCase() + difficultyString.substring(1));

        // set UI based on type of question
        if (selectedQuestion.getType() == Type.MULTIPLE_CHOICE) {
            List<String> answerChoices = new ArrayList<String>();

            answerChoices.add(selectedQuestion.getCorrectAnswer());

            answerChoices.addAll(selectedQuestion.getIncorrectAnswers());

            answer1ET.setText(answerChoices.get(0));
            answer2ET.setText(answerChoices.get(1));
            answer3ET.setText(answerChoices.get(2));
            answer4ET.setText(answerChoices.get(3));


        } else {
            // figure out if true or false was the right answer
            Boolean correctAnswer = (selectedQuestion.getCorrectAnswer().equals("True")) ? (true) : (false);

            // answer1ET will contain the correct answer
            answer1ET.setText(correctAnswer.toString().substring(0, 1).toUpperCase() +
                    correctAnswer.toString().substring(1));

            // answer2ET contains incorrect answer
            answer2ET.setText(((Boolean) !correctAnswer).toString().substring(0, 1).toUpperCase() +
                    ((Boolean)!correctAnswer).toString().substring(1));

            answer3ET.setVisibility(View.INVISIBLE);
            answer4ET.setVisibility(View.INVISIBLE);
            answer3TV.setVisibility(View.INVISIBLE);
            answer4TV.setVisibility(View.INVISIBLE);

        }

        // bold correct answer (first answer in list of choices)
        answer1ET.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
        answer1TV.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));




    }
}