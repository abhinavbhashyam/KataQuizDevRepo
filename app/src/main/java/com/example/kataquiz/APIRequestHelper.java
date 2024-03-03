package com.example.kataquiz;

import android.app.Activity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class that manages all interaction with API (fetching/retrieving data)
 */
public class APIRequestHelper {
    // request queue
    RequestQueue requestQueue;

    /**
     * Initializes the request queue
     * @param requestingActivity the activity we are requesting data from
     */
    public APIRequestHelper(Activity requestingActivity) {
        requestQueue = Volley.newRequestQueue(requestingActivity);
    }

    /**
     * Finds the questions for a specific quiz from the API
     * @param url the url we are using to access the questions
     * @param callback callback used to manage asynch nature of Volley
     */
    public void getQuestionsForQuiz(String url, APIRequestCallback callback) {
        List<Question> questionsInQuiz = new ArrayList<>();

        // request for data (GET request)
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    // get access to the array of categories
                    JSONArray questions = (JSONArray) response.get("results");

                    // loop over each question (i.e. result)
                    for (int i = 0; i < questions.length(); i++) {
                        // current question (as a JSONObject)
                        JSONObject currQuestion = (JSONObject) questions.get(i);

                        // type of question
                        Type type;

                        type = (currQuestion.get("type").equals("boolean")) ? (Type.TRUE_FALSE) : (Type.MULTIPLE_CHOICE);

                        // difficulty of question
                        Difficulty difficulty;

                        if (currQuestion.get("difficulty").equals("easy")) {
                            // easy difficulty
                            difficulty = Difficulty.EASY;
                        } else if (currQuestion.get("difficulty").equals("medium")) {
                            // medium difficulty
                            difficulty = Difficulty.MEDIUM;
                        } else {
                            // hard difficulty
                            difficulty = Difficulty.HARD;
                        }

                        // category of question
                        String category = (String) currQuestion.get("category");

                        // content of question
                        String questionContent = (String) currQuestion.get("question");

                        // correct answer (only one)
                        String correctAnswer = (String) currQuestion.get("correct_answer");

                        // list of incorrect answers (one or more)
                        List<String> incorrectAnswers = new ArrayList<String>();

                        // incorrect answers as JSONArray
                        JSONArray incorrectAnswersJSONArray = (JSONArray) currQuestion.get("incorrect_answers");

                        // populate our list of incorrect answers (need to do it with a loop since
                        // we can't directly cast from JSONArray to List)
                        for (int j = 0; j < incorrectAnswersJSONArray.length(); j++) {
                            incorrectAnswers.add((String) incorrectAnswersJSONArray.get(j));
                        }
                        // create question and add it to the list
                        questionsInQuiz.add(new Question(type, difficulty, category, questionContent, correctAnswer, incorrectAnswers));

                    }

                    callback.onCallbackQuiz(questionsInQuiz);

                } catch (Exception e) {
                    // in case of error (i.e. possible error in JSON parsing)
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // in case of error
                error.printStackTrace();
            }
        });

        // add to request queue
        requestQueue.add(request);

    }

    // interface for callback to handle asynch nature of Volley tasks
    public interface APIRequestCallback {
        void onCallbackQuiz(List<Question> questionsForQuiz);
    }

}
