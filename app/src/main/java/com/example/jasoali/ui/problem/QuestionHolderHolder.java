package com.example.jasoali.ui.problem;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.jasoali.R;
import com.google.android.material.card.MaterialCardView;

public class QuestionHolderHolder extends RecyclerView.ViewHolder {
    TextView title;
    TextView term;
    TextView course;
    TextView university;
    TextView professor;
    public MaterialCardView card;

    QuestionHolderHolder(View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.question_holder_title);
        term = itemView.findViewById(R.id.question_holder_term);
        course = itemView.findViewById(R.id.question_holder_course_name);
        university = itemView.findViewById(R.id.question_holder_university);
        professor = itemView.findViewById(R.id.question_holder_professor);
        card = itemView.findViewById(R.id.question_holder_card);
        card.setOnClickListener(view -> {
            //todo: go to view question holder page
            Log.e("Card", "salam!");
        });
    }

}