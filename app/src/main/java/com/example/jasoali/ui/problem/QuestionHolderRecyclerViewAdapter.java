package com.example.jasoali.ui.problem;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jasoali.R;
import com.example.jasoali.models.Category;
import com.example.jasoali.models.CategoryType;
import com.example.jasoali.models.QuestionsHolder;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class QuestionHolderRecyclerViewAdapter
        extends RecyclerView.Adapter<QuestionHolderHolder> {

    private List<QuestionsHolder> mData = new ArrayList<>();
    private List<QuestionsHolder> mDataCopy = new ArrayList<>();
    private final LayoutInflater mInflater;
    public ItemClickListener mClickListener;


    // data is passed into the constructor
    public QuestionHolderRecyclerViewAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }


    public void replaceData(List<QuestionsHolder> newData) {
        mData = newData;
        mDataCopy.addAll(newData);
        notifyDataSetChanged();
    }


    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public QuestionHolderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.fragment_question_holder, parent, false);
        return new QuestionHolderHolder(view);
    }


    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(@NonNull QuestionHolderHolder holder, int position) {
        QuestionsHolder questionsHolder = mData.get(position);
        holder.title.setText(questionsHolder.getTitle());
        for (Category category : questionsHolder.getCategories()) {
            if (category.getType() == CategoryType.COURSE)
                holder.course.setText(category.getValue());

            if (category.getType() == CategoryType.PROFESSOR)
                holder.professor.setText(category.getValue());

            if (category.getType() == CategoryType.TERM)
                holder.term.setText(category.getValue());

            if (category.getType() == CategoryType.UNIVERSITY)
                holder.university.setText(category.getValue());
        }
    }


    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView term;
        TextView courseName;
        TextView university;
        TextView professor;
        MaterialCardView card;

        ViewHolder(View itemView, int a) {
            super(itemView);
            title = itemView.findViewById(R.id.question_holder_title);
            term = itemView.findViewById(R.id.question_holder_term);
            courseName = itemView.findViewById(R.id.question_holder_course_name);
            university = itemView.findViewById(R.id.question_holder_university);
            professor = itemView.findViewById(R.id.question_holder_professor);
            card = itemView.findViewById(R.id.question_holder_card);
            card.setOnClickListener(view -> {
//                    if (view.equals(deleteButton)) {
//                        mClickListener.onDeleteButtonClick(getAdapterPosition());
//                        mData.remove(getAdapterPosition());
//                        notifyItemRemoved(getAdapterPosition());
//                        notifyItemRangeChanged(getAdapterPosition(), mData.size());
//                    } else if (mClickListener != null) {
//                        mClickListener.onItemClick(view, getAdapterPosition());
//                    }
                Log.e("On Click!", "salam");
            });
        }

    }

    public void filter(String text) {
        mData.clear();
        if (text.isEmpty()) {
            mData.addAll(mDataCopy);
        } else {
            text = text.toLowerCase();
            for (QuestionsHolder questionsHolder : mDataCopy) {
                if (questionsHolder.getTitle().toLowerCase().contains(text)) {
                    mData.add(questionsHolder);
                }
            }
        }
        notifyDataSetChanged();
    }

    // convenience method for getting data at click position
    public QuestionsHolder getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onDeleteButtonClick(int position);

        void onItemClick(View view, int position);
    }
}