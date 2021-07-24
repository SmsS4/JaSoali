//package com.example.jasoali.ui.problem;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.SearchView;
//import android.widget.Toast;
//
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.jasoali.R;
//import com.example.jasoali.models.QuestionsHolder;
//
//import java.util.ArrayList;
//
//
//public class SearchFragment extends Fragment implements QuestionHolderRecyclerViewAdapter.ItemClickListener {
//    private QuestionHolderRecyclerViewAdapter adapter;
//    private RecyclerView bookmarks;
//    private SearchView searchView;
//    private ArrayList<QuestionsHolder> questionsHolders = new ArrayList<>();
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        // fetch from db and but in questionHolders
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);
//        bookmarks = view.findViewById(R.id.bookmarks);
//        bookmarks.setLayoutManager(new LinearLayoutManager(getActivity()));
//        adapter = new MyRecyclerViewAdapter(getActivity(), locations);
//        adapter.setClickListener(this);
//        bookmarks.setAdapter(adapter);
//        searchView = view.findViewById(R.id.bookmark_search_view);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                adapter.filter(query);
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                adapter.filter(newText);
//                return true;
//            }
//        });
//
//        return view;
//    }
//
//    @Override
//    public void onDeleteButtonClick(int position) {
//        dbHelper.removeLocation(db, locations.get(position));
//        Toast.makeText(getActivity(), "Location Removed", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onItemClick(View view, int position) {
//        MyLocation clickedLocation = locations.get(position);
//        getFragmentManager().beginTransaction().remove(this).commit();
//        ((MainActivity) getActivity())
//                .navbar.getMenu().getItem(1).setChecked(true);
//        ((MainActivity) getActivity())
//                .travelToLocation(clickedLocation.getLatitude(), clickedLocation.getLongitude());
//    }
//}