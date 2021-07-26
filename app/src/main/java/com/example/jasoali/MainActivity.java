package com.example.jasoali;

//import com.parse.Parse;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;


import com.example.jasoali.ui.problem.SearchFragment;
import com.example.jasoali.ui.problem.ShowQuestionsHolderFragment;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
//import com.parse.ParseObject;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public LinearProgressIndicator progressIndicator;
    private SearchFragment searchFragment = new SearchFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /// hides keyboard on start
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .enableLocalDataStore()
                .build());

        setContentView(R.layout.activity_main);

        progressIndicator = findViewById(R.id.progress_bar);
        progressIndicator.setVisibility(View.VISIBLE);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, searchFragment).commit();

//        ParseObject firstObject = new ParseObject("FirstClass");
//        firstObject.put("message", "Hey ! First message from android. Parse is now connected");
//        firstObject.saveInBackground(e -> {
//            if (e != null) {
//                Log.e("MainActivity", e.getLocalizedMessage());
//            } else {
//                Log.d("MainActivity", "Object saved.");
//            }
//        });


//        ArrayList<ParseObject> tmp = new ArrayList<>();
//
//        ParseQuery<ParseObject> query = ParseQuery.getQuery("FirstClass");
//        query.findInBackground(new FindCallback<ParseObject>() {
//            public void done(List<ParseObject> scoreList, ParseException e) {
//                if (e == null) {
//                    tmp.add(scoreList.get(0));
//                    tmp.add(scoreList.get(1));
//                    Log.d("score", "Retrieved " + scoreList.size() + " scores");
//                    for (ParseObject parseObject : tmp) {
//                        Log.e("Salam", parseObject.getParseFile("image").getUrl());
//                        Log.e("Salam", parseObject.getParseFile("image").getName());
//                        try {
//                            Log.e("Salam", Arrays.toString(parseObject.getParseFile("image").getData()));
//                        } catch (ParseException parseException) {
//                            parseException.printStackTrace();
//                        }
//                    }
//                } else {
//                    Log.d("score", "Error: " + e.getMessage());
//                }
//            }
//        });
        ShowQuestionsHolderFragment fragInfo = ShowQuestionsHolderFragment.newInstance(0);
//        ShowQuestionsHolderFragment fragInfo = ShowQuestionsHolderFragment.newAddQuestionsInstance();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragInfo);
        transaction.commit();
//
//        BottomNavigationView navView = findViewById(R.id.nav_view);
////        Passing each menu ID as a set of Ids because each
////        menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(navView, navController);
    }

}