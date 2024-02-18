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

import org.checkerframework.checker.units.qual.A;

import java.sql.Array;

/**
 * Main landing page after sign in
 */
public class DashboardActivity extends AppCompatActivity {

    FirebaseHelper firebaseHelper = new FirebaseHelper();   // reference to DB layer
    TextView displayWelcomeTV;  // reference to UI element

    // TODO: MAKE THIS BETTER
    String[] categories = {"Any Category", "General Knowledge", "Entertainment: Books",
            "Entertainment: Film", "Entertainment: Music", "Entertainment: Musicals & Theatres",
            "Entertainment: Television", "Entertainment: Video Games", "Entertainment: Board Games",
            "Science & Nature", "Science: Computers", "Science: Mathematics", "Mythology", "Sports",
            "Geography", "History", "Politics", "Art", "Celebrities", "Animals", "Vehicles",
            "Entertainment: Comics", "Science: Gadgets", "Entertainment: Japanese Anime & Manga",
            "Entertainment: Cartoon & Animations"};

    String[] difficulties = {"Any Difficulty", "Easy", "Medium", "Hard"};

    String[] types = {"Any Type", "Multiple Choice", "True / False"};

    /**
     * Helper method that helps us create the spinners on the UI
     * @param spinnerTextView AutoCompleteTextView corresponding to the spinner
     * @param spinnerContent the list of items that are part of the spinner
     */
    private void spinnerControl(AutoCompleteTextView spinnerTextView, String[] spinnerContent) {
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
     * Initialize appropriate UI references and display welcome message by retrieving
     * currently signed in user
     * @param savedInstanceState unused
     */
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // initialize spinners
        spinnerControl(findViewById(R.id.categoryTV), categories);
        spinnerControl(findViewById(R.id.difficultyTV), difficulties);
        spinnerControl(findViewById(R.id.typeTV), types);

        // reference to welcome TV
        displayWelcomeTV = findViewById(R.id.displayWelcomeTV);

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
     * Signs a user out of the application
     * @param v unused
     */
    public void signOut(View v) {
        firebaseHelper.getmAuth().signOut();
        startActivity(new Intent(getApplicationContext(), SignInActivity.class));
    }
}