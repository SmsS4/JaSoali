package com.example.jasoali.ui.problem;

import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jasoali.R;
import com.example.jasoali.api.DBConnection;
import com.example.jasoali.models.Question;
import com.example.jasoali.models.QuestionsHolder;
import com.example.jasoali.models.User;
import com.example.jasoali.ui.questionsholder.CommentsAdapter;
import com.example.jasoali.ui.questionsholder.QuestionsAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShowQuestionsHolderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowQuestionsHolderFragment extends Fragment {

    public static final String QUESTIONS_HOLDER_ID = "questionsHolderId";

    private QuestionsHolder questionsHolder;
    private User user;
    private DBConnection db = DBConnection.getInstance();

    private EditText title;
    private EditText description;
    private EditText tags;

    private List<Question> questions;

    public ShowQuestionsHolderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ShowQuestionsHolderFragment.
     */
    public static ShowQuestionsHolderFragment newInstance(int questionsHolderId) {
        ShowQuestionsHolderFragment fragment = new ShowQuestionsHolderFragment();
        Bundle args = new Bundle();
        args.putInt(QUESTIONS_HOLDER_ID, questionsHolderId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            int questionsHolderId = getArguments().getInt(QUESTIONS_HOLDER_ID);
            questionsHolder = db.getLocalQuestionsHolder(questionsHolderId);
            user = db.getLocalUser();

        }

        System.out.println("finish");
    }
    private void changeMode(boolean show) {
        title.setEnabled(show);
        description.setEnabled(show);
        tags.setEnabled(show);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_questions_holder, container, false);
        questions = questionsHolder.getQuestions();

        RecyclerView recyclerView = view.findViewById(R.id.questions);
        QuestionsAdapter adapter = new QuestionsAdapter(questions);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));


        RecyclerView recyclerViewComments = view.findViewById(R.id.comments);
        CommentsAdapter adapterComments = new CommentsAdapter(questionsHolder.getComments());
        recyclerViewComments.setAdapter(adapterComments);
        recyclerViewComments.setLayoutManager(new LinearLayoutManager(view.getContext()));

        title = view.findViewById(R.id.titleQH);
        description = view.findViewById(R.id.descriptionQH);
        tags = view.findViewById(R.id.tagQH);
        title.setText(questionsHolder.getTitle());
        description.setText(questionsHolder.getDescription());
        changeMode(false);
        return view;
    }
}