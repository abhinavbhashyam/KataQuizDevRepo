package com.example.kataquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Main landing page after sign in
 */
public class DashboardActivity extends AppCompatActivity {

    FirebaseHelper firebaseHelper = new FirebaseHelper();   // reference to DB layer
    TextView displayWelcomeTV;  // reference to UI element

    // reference to API request layer
    APIRequestHelper apiRequestHelper;

    // arrays for the different items that appear under the spinners
    String[] categories = {"Any Category", "General Knowledge", "Entertainment: Books",
            "Entertainment: Film", "Entertainment: Music", "Entertainment: Musicals & Theatres",
            "Entertainment: Television", "Entertainment: Video Games", "Entertainment: Board Games",
            "Science & Nature", "Science: Computers", "Science: Mathematics", "Mythology", "Sports",
            "Geography", "History", "Politics", "Art", "Celebrities", "Animals", "Vehicles",
            "Entertainment: Comics", "Science: Gadgets", "Entertainment: Japanese Anime & Manga",
            "Entertainment: Cartoon & Animations"};

    Map<String, Integer> categoryIDMapping;

    String[] difficulties = {"Any Difficulty", "Easy", "Medium", "Hard"};

    String[] types = {"Any Type", "Multiple Choice", "True / False"};

    // get questions using API
    List<Question> quizQuestions;

    // references to spinners
    TextInputLayout categorySpinner;
    TextInputLayout difficultySpinner;
    TextInputLayout typeSpinner;

    /**
     * Initialize appropriate UI references and display welcome message by retrieving
     * currently signed in user
     * @param savedInstanceState unused
     */
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // initialize API helper class reference
        apiRequestHelper = new APIRequestHelper(DashboardActivity.this);

        // initialize spinners
        spinnerInitialization(findViewById(R.id.categoryTV), categories);
        spinnerInitialization(findViewById(R.id.difficultyTV), difficulties);
        spinnerInitialization(findViewById(R.id.typeTV), types);

        // once spinners have been created, initialize references to them
        categorySpinner = findViewById(R.id.categorySP);
        difficultySpinner = findViewById(R.id.difficultySP);
        typeSpinner = findViewById(R.id.typeSP);

        // reference to welcome TV
        displayWelcomeTV = findViewById(R.id.displayWelcomeTV);

        // retrieve the mapping from categories to their ID (used in the process of generating URL)
        apiRequestHelper.getCategoryIDMapping(new APIRequestHelper.APIRequestCallback() {
            @Override
            public void onCallbackMapping(Map<String, Integer> mapping) {
                categoryIDMapping = mapping;
            }
        });

        // get the uid of the currently signed in user
        String uid = firebaseHelper.getmAuth().getCurrentUser().getUid();

        // read the data corresponding to this user
        firebaseHelper.readUser(uid, new FirebaseHelper.FirestoreUserCallback() {
            @Override
            public void onCallbackReadUser(User u) {
                // get current user
                User currentUser = u;

                // display welcome message
                String welcomeMessage = "Welcome, " + currentUser.getFirstName() + " " + currentUser.getLastName() + "!";

                SpannableString nameBolded = new SpannableString(welcomeMessage);

                nameBolded.setSpan(new StyleSpan(Typeface.BOLD), 8, nameBolded.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                displayWelcomeTV.setText(nameBolded);
            }
        });
    }

    /**
     * Generates a quiz for the user
     * @param v unused
     */
    public void generateQuiz(View v) {
        // get the entries in the spinners
        String categorySpinnerEntry = categorySpinner.getEditText().getText().toString();
        String difficultySpinnerEntry = difficultySpinner.getEditText().getText().toString();
        String typeSpinnerEntry = typeSpinner.getEditText().getText().toString();

        // check entries for emptiness
        if (categorySpinnerEntry.isEmpty() || difficultySpinnerEntry.isEmpty() || typeSpinnerEntry.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Fill in all fields!",
                    Toast.LENGTH_SHORT).show();
        } else {
            // this is the url we want to use to fetch the questions
            String url = generateRequestURL(categorySpinnerEntry, difficultySpinnerEntry, typeSpinnerEntry);

            Log.i("BEOFRE RESULT", url);

            apiRequestHelper.getQuestionsForQuiz(url, new APIRequestHelper.APIRequestCallback() {
                @Override
                public void onCallbackQuiz(List<Question> quiz) {
                    quizQuestions = quiz;
                    // log questions
                    Log.i("QUIZ", Arrays.deepToString(quiz.toArray()));

                    if (quiz.size() == 0) {
                        Toast.makeText(getApplicationContext(), "Try different settings! There were no " +
                                        "questions for the settings you selected.",
                                Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }
    }

    /**
     * Helper method to generate the URL that retrieves the questions in a user's quiz
     * @param category the category the user selected
     * @param difficulty the difficulty the user selected
     * @param type the type the user selected
     * @return the URL that will be used to retrieve the questions in a user's quiz
     */
    private String generateRequestURL(String category, String difficulty, String type) {
        // url
        String url = "https://opentdb.com/api.php?amount=10&";

        Log.i("IN GENERATE", String.valueOf(categoryIDMapping.size()));
        // add to url
        if (!category.equals("Any Category")) {
            // here we don't want the category name, but its respective ID
            url += "category=" + categoryIDMapping.get(category) + "&";
        }

        if (!difficulty.equals("Any Difficulty")) {
            url += "difficulty=" + difficulty.toLowerCase() + "&";
        }

        if (!type.equals("Any Type")) {
            if (type.equals("True / False")) {
                url += "type=boolean&";
            } else {
                url += "type=multiple&";
            }
        }

        // return resultant url (with trailing & chopped off)
        return url.substring(0, url.length() - 1);
    }
    /**
     * Signs a user out of the application
     * @param v unused
     */
    public void signOut(View v) {
        firebaseHelper.getmAuth().signOut();
        startActivity(new Intent(getApplicationContext(), SignInActivity.class));
    }

    /**
     * Helper method that helps us create the spinners on the UI
     * @param spinnerTextView AutoCompleteTextView corresponding to the spinner
     * @param spinnerContent the list of items that are part of the spinner
     */
    private void spinnerInitialization(AutoCompleteTextView spinnerTextView, String[] spinnerContent) {
        // initialize adapter for items in spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, spinnerContent);

        // set the adapter
        spinnerTextView.setAdapter(adapter);

        spinnerTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // do nothing
            }
        });

    }

}