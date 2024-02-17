package com.example.kataquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

/**
 * Main landing page after sign in
 */
public class DashboardActivity extends AppCompatActivity {

    FirebaseHelper firebaseHelper = new FirebaseHelper();   // reference to DB layer
    TextView displayWelcomeTV;  // reference to UI element

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