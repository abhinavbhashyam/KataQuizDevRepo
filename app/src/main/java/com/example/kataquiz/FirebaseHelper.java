package com.example.kataquiz;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Purpose of class is to manage all interaction with the Firestore database
 */
public class FirebaseHelper {
    private FirebaseAuth mAuth; // authentication
    private FirebaseFirestore db; // reference to database

    /**
     * Constructor to initialize instance variables
     */
    public FirebaseHelper() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    /**
     * Getter method that gives us an authentication instance
     * @return an instance of FirebaseAuth, allowing us access to authentication activities
     */
    public FirebaseAuth getmAuth() { return mAuth; }

    /**
     * Adds a User to our database
     * @param toAdd this is the User we are adding
     */
    public void addUserToFirestore(User toAdd) {
        String TAG = "addUserToFirestore";

        // we will add this User to our Users collection, w/ appropriate ID
        db.collection("Users").document(toAdd.getUID())
            .set(toAdd) // initialize this document with User data
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.i(TAG, "Account added to database");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Unable to add account to database");
                    }
                });

    }

    /**
     * Reads a user object from the database
     * @param uid the unique UID of this user
     * @param callback used to call back the caller to return data
     */
    public void readUser(String uid, FirestoreUserCallback callback) {
        // get document corresponding to this user
        DocumentReference docRef = db.collection("Users").document(uid);

        // take a document snapshot to gain access to data
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                // when data is retrieved, call back the caller method
                callback.onCallbackReadUser(documentSnapshot.toObject(User.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Below are callback interfaces that are required to handle async nature of Firestore tasks
     *
     */
    public interface FirestoreUserCallback {
        default void onCallbackReadUser(User currUser) {}

    }
}

