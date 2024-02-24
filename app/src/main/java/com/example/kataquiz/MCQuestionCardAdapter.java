package com.example.kataquiz;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kataquiz.databinding.McCardBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Adapter allowing us to generate our card stack
 */
public class MCQuestionCardAdapter extends RecyclerView.Adapter<MCQuestionCardAdapter.myViewHolder> {
    List<Question> questionList; // the list of Questions to display

    /**
     * Constructor for adapter
     * @param questionList list of Questions to display within card stack
     */
    public MCQuestionCardAdapter(List<Question> questionList) {
        this.questionList = questionList;
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
        McCardBinding binding = McCardBinding.inflate(li);
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
        holder.binding.progressMC.setMax(questionList.size());
        holder.binding.progressMC.setProgress(position + 1);
        holder.binding.mcCardQuestionNameTV.setText(cardItem.getQuestion());

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
        } else {
            Log.i("TYPE", cardItem.getType().toString());
            holder.binding.answer1ET.setText("TRUE");
            holder.binding.answer2ET.setText("FALSE");
            holder.binding.answer3ET.setVisibility(View.GONE);
            holder.binding.choiceCTV.setVisibility(View.GONE);
            holder.binding.answer4ET.setVisibility(View.GONE);
            holder.binding.choiceDTV.setVisibility(View.GONE);
        }

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
        McCardBinding binding;

        public myViewHolder(@NonNull McCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding; // note: ViewHolder incorporates binding
        }
    }
}
