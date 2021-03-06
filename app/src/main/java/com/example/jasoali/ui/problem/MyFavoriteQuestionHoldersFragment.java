package com.example.jasoali.ui.problem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jasoali.MainActivity;
import com.example.jasoali.R;
import com.example.jasoali.api.DBConnection;

public class MyFavoriteQuestionHoldersFragment extends Fragment {
    public QuestionHolderRecyclerViewAdapter adapter;
    private RecyclerView questionHolders;
    private SearchView searchField;
    private final DBConnection dbConnection = new DBConnection(MainActivity.getHandler());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("نشان‌شده‌ها");
        adapter = new QuestionHolderRecyclerViewAdapter(getActivity());
        dbConnection.getAllFavouriteQuestionsHolder(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_questions, container, false);
        questionHolders = view.findViewById(R.id.favorite_question_holders_fragment_recycler_view);
        questionHolders.setLayoutManager(new LinearLayoutManager(getActivity()));
        questionHolders.setAdapter(adapter);

        searchField = view.findViewById(R.id.favorite_question_holders_fragment_search_field);
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