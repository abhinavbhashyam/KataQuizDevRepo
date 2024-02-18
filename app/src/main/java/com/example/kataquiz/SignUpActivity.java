package com.example.kataquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;


/**
 * This class is used to sign up a User and add them to our database
 */
public class SignUpActivity extends AppCompatActivity {

    // get references to UI elements
    private EditText emailET, passwordET, firstNameET, lastNameET;
    private Switch isSharing; // TODO: take to correct screen depending on value

    FirebaseHelper firebaseHelper = new FirebaseHelper();   // reference to class that interacts w/ DB

    private final String TAG = "SignUpActivity";    // needed for Log messages
    /**
     * Main goal is to initialize UI references
     * @param savedInstanceState unused
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // initialize references to UI
        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        firstNameET = findViewById(R.id.firstNameET);
        lastNameET = findViewById(R.id.lastNameET);
    }

    /**
     * Signs up a user, adding them to the database
     * @param v required for linkage to button
     */
    public void signUp(View v) {
        // retrieve data from fields
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();
        String firstName = firstNameET.getText().toString();
        String lastName = lastNameET.getText().toString();

        // check fields if they are empty, sign up if they are not
        if (email.trim().isEmpty() || password.trim().isEmpty() || firstName.trim().isEmpty() ||
                lastName.trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Make sure to fill out all fields!",
                    Toast.LENGTH_SHORT).show();
        } else {
            try {
                // create user using mAuth reference
                firebaseHelper.getmAuth().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // log appropriate message is task was successful
                                    Log.i(TAG, "Account successfully created with email " + email);

                                    // reference to currently signed in user
                                    FirebaseUser currUser = firebaseHelper.getmAuth().getCurrentUser();

                                    // add the user to the database
                                    firebaseHelper.addUserToFirestore(new User(currUser.getUid(),
                                            firstName, lastName, email, password));

                                    // Toast message to user since sign up was successful
                                    Toast.makeText(getApplicationContext(), "Welcome, " + firstName
                                            + " " + lastName + "!", Toast.LENGTH_SHORT).show();

                                    // take to appropriate screen after sign up/login
                                    new Navigation().takeToDashboard(SignUpActivity.this);
                                } else {
                                    // user was not created successfully (usually due to duplicate email)
                                    Log.d(TAG, "Sign up unsuccessful");
                                    Toast.makeText(getApplicationContext(),  "Try a different email/password.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            } catch (Exception e){
                // an unusual error has occurred
                Toast.makeText(getApplicationContext(), "An error has occured. Try restarting the " +
                        "application", Toast.LENGTH_SHORT).show();
            }
        }

    }

}