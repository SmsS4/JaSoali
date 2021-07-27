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

import com.example.jasoali.MainActivity;
import com.example.jasoali.R;
import com.example.jasoali.models.Category;
import com.example.jasoali.models.CategoryType;
import com.example.jasoali.models.QuestionsHolder;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class QuestionHolderRecyclerViewAdapter
        extends RecyclerView.Adapter<QuestionHolderHolder> {

    private final List<QuestionsHolder> mData = new ArrayList<>();
    private final List<QuestionsHolder> mDataCopy = new ArrayList<>();
    private final LayoutInflater mInflater;
    public ItemClickListener mClickListener;



    public QuestionHolderRecyclerViewAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }


    public void replaceData(List<QuestionsHolder> newData) {
        mData.clear();
        mDataCopy.clear();
        mData.addAll(newData);
        mDataCopy.addAll(newData);
    }



    @NonNull
    @Override
    public QuestionHolderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.fragment_question_holder, parent, false);
        return new QuestionHolderHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull QuestionHolderHolder holder, int position) {
        QuestionsHolder questionsHolder = mData.get(position);
        holder.title.setText(questionsHolder.getTitle());
        holder.card.setOnClickListener(v -> {
            ShowQuestionsHolderFragment fragInfo = ShowQuestionsHolderFragment.newInstance(questionsHolder.getId(),holder.itemView.getContext());
            MainActivity activity = (MainActivity) holder.itemView.getContext();
            activity.showInfoFragment(fragInfo);
        });
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



    @Override
    public int getItemCount() {
        return mData.size();
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



    public QuestionsHolder getItem(int id) {
        return mData.get(id);
    }



    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }



    public interface ItemClickListener {
        void onDeleteButtonClick(int position);

        void onItemClick(View view, int position);
    }
}