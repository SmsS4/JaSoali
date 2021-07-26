package com.example.jasoali.ui.problem;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jasoali.R;
import com.example.jasoali.api.DBConnection;
import com.example.jasoali.models.QuestionsHolder;

import java.util.ArrayList;


public class SearchFragment extends Fragment {
    private QuestionHolderRecyclerViewAdapter adapter;
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