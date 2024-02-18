package com.example.kataquiz;

import java.util.Arrays;
import java.util.List;

enum Type {
    MULTIPLE_CHOICE,
    TRUE_FALSE
}

enum Difficulty {
    EASY,
    MEDIUM,
    HARD
}
public class Question {
    // instance variables
    private Type type;
    private Difficulty difficulty;
    private String category;
    private String question;
    private String correctAnswer;
    private List<String> incorrectAnswers;

    /**
     * Constructor for a Question object
     * @param type mc or true/false
     * @param difficulty easy, medium, or hard
     * @param category category of question
     * @param question actual content of question
     * @param correctAnswer single correct answer
     * @param incorrectAnswers list of incorrect answers
     */
    public Question(Type type, Difficulty difficulty, String category, String question, String correctAnswer, List<String> incorrectAnswers) {
        this.type = type;
        this.difficulty = difficulty;
        this.category = category;
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.incorrectAnswers = incorrectAnswers;
    }

    /**
     * Get type
     * @return the type
     */
    public Type getType() {
        return type;
    }

    /**
     * Set type
     * @param type new type
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * Get difficulty
     * @return the difficulty
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * Set difficulty
     * @param difficulty the new difficulty
     */
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Get category
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Set category
     * @param category the new category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Get question content
     * @return the question content
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Set question content
     * @param question the new question content
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * Get correct answer
     * @return the correct answer
     */
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    /**
     * Set correct answer
     * @param correctAnswer the new correct answer
     */
    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    /**
     * Get incorrect answers
     * @return the incorrect answers
     */
    public List<String> getIncorrectAnswers() {
        return incorrectAnswers;
    }

    /**
     * Set incorrect answers
     * @param incorrectAnswers the new incorrect answers
     */
    public void setIncorrectAnswers(List<String> incorrectAnswers) {
        this.incorrectAnswers = incorrectAnswers;
    }

    /**
     * To string method for a question object
     * @return string representation of a Question
     */
    public String toString() {
        return "{" + this.type.toString() + ", " + this.difficulty.toString() + ", " + this.category + ", " +
                this.question + ", " + this.correctAnswer + ", " + Arrays.deepToString(this.incorrectAnswers.toArray()) + "}";
    }
}
