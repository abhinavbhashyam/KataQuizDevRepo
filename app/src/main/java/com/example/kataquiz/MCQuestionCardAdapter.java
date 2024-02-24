package com.example.kataquiz;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kataquiz.databinding.CardBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Adapter allowing us to generate our card stack
 */
public class MCQuestionCardAdapter extends RecyclerView.Adapter<MCQuestionCardAdapter.myViewHolder> {
    List<Question> questionList; // the list of Questions to display
    Activity displayingActivity;    // activity on which card stack is being displayed

    /**
     * Constructor for adapter
     * @param questionList list of Questions to display within card stack
     * @param displayingActivity the activity on which card stack is being displayed
     */
    public MCQuestionCardAdapter(List<Question> questionList, Activity displayingActivity) {
        this.questionList = questionList;
        this.displayingActivity = displayingActivity;
    }

    /**
     * Creates a viewHolder object for each card
     * @param parent parent view
     * @param viewType
     * @return viewHolder object (for each card)
     */
    @NonNull
    @Override
    public MCQuestionCardAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        CardBinding binding = CardBinding.inflate(li);
        return new myViewHolder(binding);
    }

    /**
     * Binds data to view
     * @param holder viewHolder corresponding to a card
     * @param position index in list of cards
     */
    @Override
    public void onBindViewHolder(@NonNull MCQuestionCardAdapter.myViewHolder holder, int position) {
        Question cardItem = questionList.get(position);

        // set UI elements accordingly
        holder.binding.progress.setMax(questionList.size());
        holder.binding.progress.setProgress(position + 1);
        holder.binding.cardQuestionNameTV.setText(cardItem.getQuestion());

        // adapter to help us populate the spinners
        ArrayAdapter<String> adapter;

        // now, depending on the type of the question, we want to set up the UI differently
        if (cardItem.getType() == Type.MULTIPLE_CHOICE) {
            // assemble a list of all the answer choices
            List<String> answerChoices = new ArrayList<String>();

            answerChoices.add(cardItem.getCorrectAnswer());

            answerChoices.addAll(cardItem.getIncorrectAnswers());

            // shuffle up the answer choices so correct answer is placed at a random spot
            Collections.shuffle(answerChoices);

            // set answer options accordingly
            holder.binding.answer1ET.setText(answerChoices.get(0));
            holder.binding.answer2ET.setText(answerChoices.get(1));
            holder.binding.answer3ET.setText(answerChoices.get(2));
            holder.binding.answer4ET.setText(answerChoices.get(3));

            // make sure these fields are visible
            holder.binding.answer3ET.setVisibility(View.VISIBLE);
            holder.binding.choiceCTV.setVisibility(View.VISIBLE);
            holder.binding.answer4ET.setVisibility(View.VISIBLE);
            holder.binding.choiceDTV.setVisibility(View.VISIBLE);

            // set spinner options properly
            adapter = new ArrayAdapter<String>(displayingActivity, R.layout.list_item, new String[]{"A", "B", "C", "D"});
            holder.binding.answerTV.setAdapter(adapter);
        } else {
            holder.binding.answer1ET.setText("True");
            holder.binding.answer2ET.setText("False");
            holder.binding.answer3ET.setVisibility(View.INVISIBLE);
            holder.binding.choiceCTV.setVisibility(View.INVISIBLE);
            holder.binding.answer4ET.setVisibility(View.INVISIBLE);
            holder.binding.choiceDTV.setVisibility(View.INVISIBLE);

            adapter = new ArrayAdapter<String>(displayingActivity, R.layout.list_item, new String[]{"A", "B"});
            holder.binding.answerTV.setAdapter(adapter);
        }

        // only want user to be able to enter data on appearance of card (see TakeQuizActivity)
        holder.binding.answerTV.setText(null);
        holder.binding.answerTV.setEnabled(false);

    }

    /**
     * Returns the number of elements in our list
     * @return the number of elements in list
     */
    @Override
    public int getItemCount() {
        return questionList.size();
    }

    /**
     * ViewHolder class, used to create a viewHolder for each card (incorporates binding
     * so we can easily reference UI elements defined in xml)
     */
    public static class myViewHolder extends RecyclerView.ViewHolder {
        CardBinding binding;

        public myViewHolder(@NonNull CardBinding binding) {
            super(binding.getRoot());
            this.binding = binding; // note: ViewHolder incorporates binding
        }
    }
}
