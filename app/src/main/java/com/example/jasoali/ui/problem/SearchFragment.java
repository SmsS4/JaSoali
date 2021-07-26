package com.example.jasoali.ui.problem;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jasoali.R;
import com.example.jasoali.api.DBConnection;
import com.example.jasoali.models.Category;
import com.example.jasoali.models.CategoryType;
import com.example.jasoali.models.QuestionsHolder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;


public class SearchFragment extends Fragment {
    public QuestionHolderRecyclerViewAdapter adapter;
    private RecyclerView questionHolders;
    private SearchView searchField;
    private ArrayList<QuestionsHolder> mQuestionsHolders = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getActivity().setTitle("جستجو بین جاسوالی‌ها");
        super.onCreate(savedInstanceState);
        adapter = new QuestionHolderRecyclerViewAdapter(getActivity());
        DBConnection.getInstance().getAllQuestionsHolder(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        questionHolders = view.findViewById(R.id.question_holder_recycler_view);
        questionHolders.setLayoutManager(new LinearLayoutManager(getActivity()));
        questionHolders.setAdapter(adapter);

        TextInputEditText inputCourse = view.findViewById(R.id.input_course);
        TextInputEditText inputProfessor = view.findViewById(R.id.input_professor);
        TextInputEditText inputDepartment = view.findViewById(R.id.input_department);
        TextInputEditText inputUniversity = view.findViewById(R.id.input_university);
        TextInputEditText inputTerm = view.findViewById(R.id.input_term);

        Button searchButton = view.findViewById(R.id.search_fragment_button);
        searchButton.setOnClickListener(v -> {
            ArrayList<Category> categories = new ArrayList<>();

            if (inputCourse != null)
                categories.add(new Category(CategoryType.COURSE, inputCourse.getText().toString()));

            if (inputProfessor != null)
                categories.add(new Category(CategoryType.PROFESSOR, inputProfessor.getText().toString()));

            if (inputDepartment != null)
                categories.add(new Category(CategoryType.DEPARTMENT, inputDepartment.getText().toString()));

            if (inputUniversity != null)
                categories.add(new Category(CategoryType.UNIVERSITY, inputUniversity.getText().toString()));

            if (inputTerm != null)
                categories.add(new Category(CategoryType.TERM, inputTerm.getText().toString()));


            Log.e("INPUT", inputCourse.getText().toString());
            Log.e("INPUT", inputDepartment.getText().toString());
            Log.e("INPUT", inputTerm.getText().toString());
            DBConnection.getInstance().getQuestionsHolderByCategories(categories, adapter);
            Log.e("CLICK", "click");
        });

        searchField = view.findViewById(R.id.search_fragment_search_field);
        searchField.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return true;
            }
        });
        return view;
    }
}