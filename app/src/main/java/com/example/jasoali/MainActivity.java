package com.example.jasoali;

//import com.parse.Parse;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jasoali.api.DBConnection;
import com.example.jasoali.models.User;
import com.example.jasoali.ui.problem.SearchFragment;
import com.example.jasoali.ui.problem.ShowQuestionsHolderFragment;
import com.example.jasoali.ui.profile.ProfileFragment;
import com.example.jasoali.ui.sign_in_up.RegisterActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.parse.Parse;

import java.io.File;
import java.lang.ref.WeakReference;

//import com.parse.ParseObject;


public class MainActivity extends AppCompatActivity {

    private static MyHandler handler;
    public LinearProgressIndicator progressIndicator;
    ShowQuestionsHolderFragment fragInfo;
    private SearchFragment searchFragment;

    public static MyHandler getHandler() {
        return handler;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handler = new MyHandler(this);
        searchFragment = new SearchFragment();
        fragInfo = ShowQuestionsHolderFragment.newInstance(0);

        // hides keyboard on start
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getSupportActionBar().hide(); //<< this

        setContentView(R.layout.activity_main);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .enableLocalDataStore()
                .build());

        progressIndicator = findViewById(R.id.progress_bar);
        progressIndicator.setVisibility(View.INVISIBLE);

        configNavigationMenu();
        showSearchFragment();


//        Intent intent = new Intent(this, RegisterActivity.class);
//        startActivity(intent);

//        /// todo remove these
//        new DBConnection(null).register(
//                new User(
//                        "a2",
//                        "SmsS132",
//                        "1122qqww",
//                        "smss.lite2@gmail.com",
//                        "سید مهدی صادق شبیری",
//                        true
//                )
//        );
        ProfileFragment fragInfo = ProfileFragment.newInstance();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragInfo);
        transaction.commit();

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

    void showProfileFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, searchFragment).commit();
    }

    void showFavoritesFragment() {
        // TODO
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, searchFragment).commit();
    }

    void showSearchFragment() {
        // TODO
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, searchFragment).commit();
    }

    void configNavigationMenu() {
        BottomNavigationView menu = findViewById(R.id.bottom_navigation);
        menu.setSelectedItemId(R.id.navigation_search);
        menu.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_profile) {
                showProfileFragment();
                return true;
            } else if (item.getItemId() == R.id.navigation_favorites) {
                showFavoritesFragment();
                return true;
            } else if (item.getItemId() == R.id.navigation_search) {
                showSearchFragment();
                return true;
            } else
                return false;
        });
    }

    public static class MyHandler extends Handler {

        public static final int START_PROGRESS_BAR = 1;
        public static final int STOP_PROGRESS_BAR = 2;
        public static final int NOTIFY_RECYCLER_VIEW = 3;
        private final WeakReference<MainActivity> mainActivityWeakReference;

        public MyHandler(MainActivity mainActivity) {
            this.mainActivityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            Log.e("HANDLER", String.valueOf(msg.what));
            MainActivity mainActivity = mainActivityWeakReference.get();
            if (mainActivity == null)
                return;
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
}