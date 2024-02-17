package com.example.kataquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

/**
 * Landing page; either signs in a specific user or takes them to sign up
 */
public class SignInActivity extends AppCompatActivity {

    // references to UI components
    EditText emailET, passwordET;

    FirebaseHelper firebaseHelper = new FirebaseHelper();   // reference to database layer

    private final String TAG = "SignInActivity"; // tag for Logs

    /**
     * Instantiate references to UI elements and take to correct screen
     * @param savedInstanceState unused
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // initialize UI elements
        emailET = findViewById(R.id.signInEmailET);
        passwordET = findViewById(R.id.signInPasswordET);

        // current user is already signed in, take them to dashboard
        if (firebaseHelper.getmAuth().getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
        }
    }

    /**
     * Signs in a user
     * @param v required for linkage to button
     */
    public void signIn(View v) {
        // retrieve email and password
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();

        // check vaildity of data
        if (email.trim().isEmpty() || password.trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter all fields!", Toast.LENGTH_SHORT).show();
        } else {
            // sign in user using auth reference
            firebaseHelper.getmAuth().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // if sign in successful
                                Toast.makeText(getApplicationContext(), "Sign in successful!",
                                        Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                            } else {
                                // else sign in failed
                                Log.d(TAG, "Failed to log in with email " + email + task.getException());
                                Toast.makeText(getApplicationContext(), "Wrong email or password was entered!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    /**
     * Takes user to SignUpActivity
     * @param v unused
     */
    public void takeToSignUp(View v) {
        startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
    }

}