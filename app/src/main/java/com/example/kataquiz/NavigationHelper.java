package com.example.kataquiz;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

/**
 * Helper class that contains methods that are called when user is transitioning between activities
 * /extra utility methods
 */
public class NavigationHelper {
    /**
     * Helper method to call when we are ready to take the user to dashboard. Displays alert popup.
     */
    public void takeToDashboard(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        // set text for popup
        builder.setMessage("On the next screen, you'll get to generate a quiz of your choice! " +
                        "Questions are fetched from the Open Trivia Database. " +
                        "All quizzes are 10 questions long, so you have the chance of exploring as many questions as possible!")
                .setTitle("Get ready!");

        // add the buttons
        builder.setPositiveButton("READY!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                Intent i = new Intent(activity, DashboardActivity.class);
                activity.startActivity(i);
            }
        });

        builder.setCancelable(false);

        // create & show dialog
        AlertDialog dialog = builder.create();

        dialog.show();
    }

    /**
     * Creates a string in the format [category] - [difficulty] for a certain question
     * @param question the question we are creating the string for
     * @return the string in the format [category] - [difficulty] for a certain question
     */
    public String createCategoryDifficultyString(Question question) {
        // enum difficulty into proper formatted string
        String difficultyString = question.getDifficulty().toString().toLowerCase();

        // initialize category content textview
        return question.getCategory() + " - " + difficultyString.substring(0, 1).toUpperCase() + difficultyString.substring(1);

    }
}
