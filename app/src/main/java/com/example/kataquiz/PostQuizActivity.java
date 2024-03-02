package com.example.kataquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.List;

/**
 * Activity that happens after user finishes quiz
 */
public class PostQuizActivity extends AppCompatActivity {

    TextView postQuizInfoTV;    // ref to topmost textview on display

    ListView wrongQuestionsListView;  // reference to list view containing wrong questions

    ArrayAdapter<Question> listAdapter; // ref to adapter for list view

    /**
     * Populates listview with the questions the user got wrong
     * @param savedInstanceState unused
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_quiz);

        // initialize reference
        postQuizInfoTV = findViewById(R.id.postQuizInfoTV);

        // retrieve array list of wrong questions
        Bundle extras = getIntent().getBundleExtra("INCORRECT_QUESTIONS");
        List<Question> wrongQuestions = extras.getParcelableArrayList("INCORRECT_QUESTIONS");

        // display all wrong questions in a list view
        listAdapter = new QuestionListAdapter(
                getApplicationContext(), wrongQuestions);


        wrongQuestionsListView = (ListView) findViewById(R.id.wrongQuestionsLV);
        wrongQuestionsListView.setAdapter(listAdapter);

        wrongQuestionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // user can click on individual questions they got wrong
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ViewQuestionActivity.class);

                // send over the question the user clicked to ViewQuestionActivity
                intent.putExtra("ITEM", wrongQuestions.get(i));
                startActivity(intent);

            }
        });

    }

    /**
     * Takes a user back to the quiz generation screen so they can continue generating quizzes
     * @param v the current view
     */
    public void goBackToGenerate(View v) {
        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
    }
}