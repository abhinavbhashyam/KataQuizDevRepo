package com.example.kataquiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.checkerframework.checker.units.qual.N;

import java.util.List;

/**
 * Custom adapter for our list view
 */
public class QuestionListAdapter extends ArrayAdapter<Question> {
    NavigationHelper navigationHelper;  // reference to navigation class
    /**
     * Constructor for custom adapter
     * @param context application context, passed to super constructor
     * @param questions array of Questions we are displaying
     */
    public QuestionListAdapter(Context context, List<Question> questions)
    {
        // call to super constructor
        super(context, 0, questions);
        navigationHelper = new NavigationHelper();
    }

    /**
     * Override view generation to generate custom layout for each cell in the ListView
     * @param position index at which we're modifying the cell in our ListView
     * @param convertView our modified cell view
     * @param parent parent ViewGroup
     * @return our modified view for this item
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // get current cell question
        Question question = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.question_cell, parent, false);
        }

        // generate view/modify cell
        TextView questionContent = convertView.findViewById(R.id.cellTitleTV);
        TextView categoryContent = convertView.findViewById(R.id.cellDescTV);

        // set with name of question
        questionContent.setText(question.getQuestion());

        // initialize category content textview
        categoryContent.setText(navigationHelper.createCategoryDifficultyString(question));

        return convertView;
    }


}
