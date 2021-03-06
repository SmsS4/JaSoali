package com.example.jasoali;

//import com.parse.Parse;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jasoali.api.DBConnection;
import com.example.jasoali.ui.problem.MyFavoriteQuestionHoldersFragment;
import com.example.jasoali.ui.problem.SearchFragment;
import com.example.jasoali.ui.problem.ShowQuestionTextFragment;
import com.example.jasoali.ui.problem.ShowQuestionsHolderFragment;
import com.example.jasoali.ui.profile.ProfileFragment;
import com.example.jasoali.ui.sign_in_up.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.lang.ref.WeakReference;

//import com.parse.ParseObject;


public class MainActivity extends AppCompatActivity {

    private static MyHandler handler;
    public LinearProgressIndicator progressIndicator;
    //    private ShowQuestionsHolderFragment fragInfo;
    private SearchFragment searchFragment;
    private ProfileFragment profileFragment;
    private MyFavoriteQuestionHoldersFragment favoritesFragment;

    public static MyHandler getHandler() {
        return handler;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkUserState();

        handler = new MyHandler(this);
        searchFragment = new SearchFragment();
        profileFragment = ProfileFragment.newInstance(this);
        favoritesFragment = new MyFavoriteQuestionHoldersFragment();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_main);

        progressIndicator = findViewById(R.id.progress_bar);
        progressIndicator.setVisibility(View.INVISIBLE);

        configNavigationMenu();
        showSearchFragment();

     }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    public void checkUserState() {
        DBConnection db = new DBConnection(null);
        if (db.getLocalUser() != null)
            return;
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    void showProfileFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, profileFragment)
                .commit();
    }

    void showFavoritesFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, favoritesFragment)
                .commit();
    }

    public void showSearchFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, searchFragment)
                .commit();
    }

    public void showInfoFragment(ShowQuestionsHolderFragment frag) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, frag)
                .commit();
    }

    public void addInfoFragment(ShowQuestionTextFragment frag) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(frag, "detail")
                .replace(R.id.fragment_container, frag)
                .addToBackStack(null)
                .commit();
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
        public static final int NOTIFY_SEARCH_RECYCLER_VIEW = 3;
        public static final int NOTIFY_FAVORITE_RECYCLER_VIEW = 4;
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
            if (msg.what == NOTIFY_SEARCH_RECYCLER_VIEW) {
                mainActivity.searchFragment.adapter.notifyDataSetChanged();
            }
            if (msg.what == NOTIFY_FAVORITE_RECYCLER_VIEW) {
                mainActivity.favoritesFragment.adapter.notifyDataSetChanged();
            }
        }
    }
}