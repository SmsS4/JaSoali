package com.example.jasoali;

//import com.parse.Parse;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;


import com.example.jasoali.api.DBConnection;
import com.example.jasoali.models.Comment;
import com.example.jasoali.ui.problem.SearchFragment;
import com.example.jasoali.ui.problem.ShowQuestionsHolderFragment;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
//import com.parse.ParseObject;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.ref.WeakReference;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public LinearProgressIndicator progressIndicator;
    private final SearchFragment searchFragment = new SearchFragment();
    ShowQuestionsHolderFragment fragInfo = ShowQuestionsHolderFragment.newInstance(0);

    public static MyHandler handler;


    public static class MyHandler extends Handler {

        public int START_PROGRESS_BAR = 1;
        public int STOP_PROGRESS_BAR = 2;
        public int NOTIFY_RECYCLER_VIEW = 3;

        private final WeakReference<MainActivity> mainActivityWeakReference;

        public MyHandler(MainActivity mainActivity) {
            this.mainActivityWeakReference = new WeakReference<>(mainActivity);
        }

        public void sendMessage(int message) {
            Message msg = new Message();
            msg.what = message;
            sendMessage(msg);
        }

        @Override
        public void handleMessage(Message msg) {
            Log.e("HANDLER", String.valueOf(msg.what));
            MainActivity mainActivity = mainActivityWeakReference.get();
            if (msg.what == START_PROGRESS_BAR) {
                mainActivity.progressIndicator.setVisibility(View.VISIBLE);
            }
            if (msg.what == STOP_PROGRESS_BAR) {
                mainActivity.progressIndicator.setVisibility(View.INVISIBLE);
            }
            if (msg.what == NOTIFY_RECYCLER_VIEW) {
                mainActivity.searchFragment.adapter.notifyDataSetChanged();
            }
        }
    }


    public static MyHandler getHandler() {
        return handler;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handler = new MyHandler(this);

        // hides keyboard on start
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setContentView(R.layout.activity_main);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .enableLocalDataStore()
                .build());

        progressIndicator = findViewById(R.id.progress_bar);
        progressIndicator.setVisibility(View.INVISIBLE);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, searchFragment).commit();

//        /// todo remove these
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.replace(R.id.fragment_container, s);
//        transaction.commit();

//        ParseObject firstObject = new ParseObject("FirstClass");
//        firstObject.put("message", "Hey ! First message from android. Parse is now connected");
//        firstObject.saveInBackground(e -> {
//            if (e != null) {
//                Log.e("MainActivity", e.getLocalizedMessage());
//            } else {
//                Log.d("MainActivity", "Object saved.");
//            }
//        });

//        ParseQuery<ParseObject> query = ParseQuery.getQuery("Comment");
//        query.findInBackground(new FindCallback<ParseObject>() {
//            @Override
//            public void done(List<ParseObject> objects, ParseException e) {
//                Log.e("COUNT", String.valueOf(objects.size()));
//                ParseObject parseObject = objects.get(0);
//                ParseUser user = null;
//                try {
//                    user = parseObject.getParseUser("maker").fetch();
//
//                } catch (ParseException parseException) {
//                    parseException.printStackTrace();
//                }
//                Log.e("COUNT", String.valueOf(objects.size()));
//                Log.e("COUNT", user != null ? user.getString("name") : "sss");
//            }
//        });

//        DBConnection.getInstance().addQuestionsHolder(DBConnection.getInstance().getLocalQuestionsHolder());
//        DBConnection.getInstance().addComment(new Comment("alaki", "comment text", "سید علیرضا هاشمی"));
    }
}
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
