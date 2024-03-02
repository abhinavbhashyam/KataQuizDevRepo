package com.example.kataquiz;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

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
public class Question implements Parcelable {
    // instance variables
    private Type type;
    private Difficulty difficulty;
    private String category;
    private String question;
    private String correctAnswer;
    private List<String> incorrectAnswers;

    /**
     * Default constructor
     */
    public Question() {
    }

    /**
     * Constructor for a Question object
     * @param type mc or true/false
     * @param difficulty easy, medium, or hard
     * @param category category of question
     * @param question actual content of question
     * @param correctAnswer single correct answer
     * @param incorrectAnswers list of incorrect answers
     */
    public Question(Type type, Difficulty difficulty, String category, String question,
                    String correctAnswer, List<String> incorrectAnswers) {
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

    /*
    Below code is for Parcelable; allows us to send objects of Question type across activities
     */
    /**
     * Interface that must be implemented and provided as a public CREATOR field that generates
     * instances of this Parcelable class from a Parcel.
     */
    public static final Parcelable.Creator<Question> CREATOR = new Parcelable.Creator<Question>() {
        /**
         * Creates a new instance of the Parcelable (Question) class, instantiating it from the
         * given Parcel
         * @param parcel the Parcel whose data had been previously written by Parcelable.writeToParcel()
         * @return a new instance of the Parcelable (Question) class, instantiating it from the
         *         given Parcel
         */
        @Override
        public Question createFromParcel(Parcel parcel) {
            return new Question(parcel);
        }

        /**
         * Returns an array of the Parcelable class with every entry initialized to null
         * @param size size of the array
         * @return an array of the Parcelable class with every entry initialized to null
         */
        @Override
        public Question[] newArray(int size) {
            return new Question[0];
        }
    };

    /**
     * Used to unpack a Parcelable Question object
     * @param parcel the parcel we are unpacking from
     */
    public Question(Parcel parcel) {
        question = parcel.readString();
        type = Type.valueOf(parcel.readString());
        category = parcel.readString();
        difficulty = Difficulty.valueOf(parcel.readString());
        correctAnswer = parcel.readString();
        incorrectAnswers = parcel.createStringArrayList();
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable instance's marshaled
     * representation
     * @return an int representing kinds of special objects contained in this Parcelable instance's marshaled
     * representation
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flattens this object into a parcel
     * @param dest the Parcel we are flattening this object into (pack)
     * @param flags unused
     */
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeString(type.name());
        dest.writeString(category);
        dest.writeString(difficulty.name());
        dest.writeString(correctAnswer);
        dest.writeStringList(incorrectAnswers);
    }
}
