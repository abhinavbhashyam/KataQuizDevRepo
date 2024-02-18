package com.example.kataquiz;

import android.app.Activity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * Returns a mapping of category names to category IDs, used in the process of assembling URL
     * (that is used to generate questions)
     * @return a mapping of category names to category IDs, used in the process of assembling URL
     * (that is used to generate questions)
     */
    public Map<String, Integer> getCategoryIDMapping(APIRequestCallback callback) {
        // url that helps us access categories and their respective IDs
        String url = "https://opentdb.com/api_category.php";

        // mapping of category names to category ID
        Map<String, Integer> categoryMapping = new HashMap<String, Integer>();

        // request for data (GET request)
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    // get access to the array of categories
                    JSONArray categories = (JSONArray) response.get("trivia_categories");

                    // loop over each category (which is of type JSONObject)
                    for (int i = 0; i < categories.length(); i++) {
                        JSONObject currCategory = (JSONObject) categories.get(i);

                        // add to our mapping
                        categoryMapping.put((String) currCategory.get("name"), (Integer) currCategory.get("id"));
                    }

                    callback.onCallbackMapping(categoryMapping);

                } catch (Exception e) {
                    // in case of error
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

        // return result
        return categoryMapping;
    }

    /**
     * Retrieves the questions for a specific quiz from the API
     * @param url the url we are using to access the questions
     * @return the questions for a specific quiz
     */
    public List<Question> getQuestionsForQuiz(String url, APIRequestCallback callback) {
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
                    // in case of error
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

        return questionsInQuiz;

    }

    // interface for callback to handle asynch nature of Volley tasks
    public interface APIRequestCallback {
        default void onCallbackMapping(Map<String, Integer> mapping){}

        default void onCallbackQuiz(List<Question> quiz){}
    }

}
