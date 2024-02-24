package com.example.kataquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main landing page after sign in
 */
public class DashboardActivity extends AppCompatActivity {

    FirebaseHelper firebaseHelper;   // reference to DB layer

    // reference to API request layer
    APIRequestHelper apiRequestHelper;

    // mapping from ID of category to category name
    String categoriesJsonString = "{\"trivia_categories\":[{\"id\":9,\"name\":\"General Knowledge\"}," +
            "{\"id\":10,\"name\":\"Entertainment: Books\"},{\"id\":11,\"name\":\"Entertainment: Film\"}," +
            "{\"id\":12,\"name\":\"Entertainment: Music\"},{\"id\":13,\"name\":\"Entertainment: Musicals & Theatres\"}" +
            ",{\"id\":14,\"name\":\"Entertainment: Television\"},{\"id\":15,\"name\":\"Entertainment: Video Games\"}," +
            "{\"id\":16,\"name\":\"Entertainment: Board Games\"},{\"id\":17,\"name\":\"Science & Nature\"}," +
            "{\"id\":18,\"name\":\"Science: Computers\"},{\"id\":19,\"name\":\"Science: Mathematics\"}," +
            "{\"id\":20,\"name\":\"Mythology\"},{\"id\":21,\"name\":\"Sports\"},{\"id\":22,\"name\":\"Geography\"}," +
            "{\"id\":23,\"name\":\"History\"},{\"id\":24,\"name\":\"Politics\"},{\"id\":25,\"name\":\"Art\"}," +
            "{\"id\":26,\"name\":\"Celebrities\"},{\"id\":27,\"name\":\"Animals\"},{\"id\":28,\"name\":\"Vehicles\"}," +
            "{\"id\":29,\"name\":\"Entertainment: Comics\"},{\"id\":30,\"name\":\"Science: Gadgets\"}," +
            "{\"id\":31,\"name\":\"Entertainment: Japanese Anime & Manga\"}," +
            "{\"id\":32,\"name\":\"Entertainment: Cartoon & Animations\"}]}";


    // arrays for the different items that appear under the spinners
    String[] categories = {"Any Category", "General Knowledge", "Entertainment: Books",
            "Entertainment: Film", "Entertainment: Music", "Entertainment: Musicals & Theatres",
            "Entertainment: Television", "Entertainment: Video Games", "Entertainment: Board Games",
            "Science & Nature", "Science: Computers", "Science: Mathematics", "Mythology", "Sports",
            "Geography", "History", "Politics", "Art", "Celebrities", "Animals", "Vehicles",
            "Entertainment: Comics", "Science: Gadgets", "Entertainment: Japanese Anime & Manga",
            "Entertainment: Cartoon & Animations"};

    String[] difficulties = {"Any Difficulty", "Easy", "Medium", "Hard"};

    String[] types = {"Any Type", "Multiple Choice", "True / False"};

    // mapping so we can figure out the category ID from the category name
    Map<String, Integer> categoryIDMapping;

    TextView displayWelcomeTV;  // reference to UI element

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

        // initialize main DB helper class reference
        firebaseHelper = new FirebaseHelper();

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
        try {
            categoryIDMapping = getCategoryIDMapping(categoriesJsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // get the uid of the currently signed in user
        String uid = firebaseHelper.getmAuth().getCurrentUser().getUid();

        // read the data corresponding to this user
        firebaseHelper.readUser(uid, new FirebaseHelper.FirestoreUserCallback() {
            @Override
            public void onCallbackReadUser(User currentUser) {
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

            apiRequestHelper.getQuestionsForQuiz(url, new APIRequestHelper.APIRequestCallback() {
                @Override
                public void onCallbackQuiz(List<Question> quizQuestions) {
                    if (quizQuestions.size() == 0) {
                        Toast.makeText(getApplicationContext(), "Try different settings! There were no " +
                                        "questions for the settings you selected.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // otherwise, we are ready to display quiz
                        takeToQuizActivity(quizQuestions);
                    }

                }
            });

        }
    }

    /**
     * Brings us to the activity where the user will take quiz
     * @param questionsForQuiz questions that will compose the user's quiz
     */
    private void takeToQuizActivity(List<Question> questionsForQuiz) {
        // we want to have the user take their quiz
        Intent intent = new Intent(getApplicationContext(), TakeQuizActivity.class);

        // we will use a Bundle object to send our Questions to the next screen
        Bundle bundle = new Bundle();


        bundle.putParcelableArrayList("QUIZ_QUESTIONS", (ArrayList<? extends Parcelable>) questionsForQuiz);

        // put this Bundle in the Intent, and send to TakeQuizActivity
        intent.putExtra("QUIZ_QUESTIONS", bundle);
        startActivity(intent);

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

    /**
     * Will return a mapping from category names to their respective category IDs
     * @param json json of the mapping (from API) as string
     * @return a mapping from category names to their respective category IDs
     * *note* exception will never be thrown
     */
    private Map<String, Integer> getCategoryIDMapping(String json) throws JSONException {
        // initialize result
        Map<String, Integer> categoryMapping = new HashMap<>();

        // convert String to JSON object
        JSONObject mappingAsJSON = new JSONObject(json);

        JSONArray categories = (JSONArray) mappingAsJSON.get("trivia_categories");

        // loop over each category (which is of type JSONObject)
        for (int i = 0; i < categories.length(); i++) {
            JSONObject currCategory = (JSONObject) categories.get(i);

            // add to our mapping
            categoryMapping.put((String) currCategory.get("name"), (Integer) currCategory.get("id"));
        }

        return categoryMapping;
    }

}