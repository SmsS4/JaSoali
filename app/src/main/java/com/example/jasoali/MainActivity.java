package com.example.jasoali;

//import com.parse.Parse;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;


import com.example.jasoali.api.DBConnection;
import com.example.jasoali.models.Comment;
import com.example.jasoali.models.TextQuestion;
import com.example.jasoali.ui.problem.SearchFragment;
import com.example.jasoali.ui.problem.ShowQuestionTextFragment;
import com.example.jasoali.ui.problem.ShowQuestionsHolderFragment;
import com.example.jasoali.ui.profile.ProfileFragment;
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

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public LinearProgressIndicator progressIndicator;
    private final SearchFragment searchFragment = new SearchFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        //
        progressIndicator = findViewById(R.id.progress_bar);
        progressIndicator.setVisibility(View.VISIBLE);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, searchFragment).commit();

//        // todo remove these
//        try {
//            TextQuestion textQuestion = new TextQuestion(
//                    "یک عنوان خوب",
//                    "دهان شماخروجی افکار شماست\n" +
//                            "\n" +
//                            "وقتی باز میشود سریعترین راه برای درک شخصیتتان دراختیار مخاطب قرار میگیرد\n" +
//                            "\n" +
//                            "مواظب ویترین شعورتان باشید،\n" +
//                            "\n" +
//                            "تا افکار و وجودتان خریدارهای بیشتر داشته باشد" +"زندگی همچون بادکنکی است در دستان کودکی\n" +
//                            "که همیشه ترس از ترکیدن آن لذت داشتنش را از بین میبرد" + "زندگی به من یاد داده\n" +
//                            "برای داشتن آرامش و آسایش\n" +
//                            "امروز را با خدا قدم بر دارم\n" +
//                            "و فردا را به او بسپارم" + "می\u200Cتوان با این خدا پرواز کرد\n" +
//                            "سفره دل را برایش باز کرد\n" +
//                            "می\u200Cتوان درباره گل حرف زد\n" +
//                            "صاف و ساده مثل بلبل حرف زد\n" +
//                            "چکه چکه مثل باران راز گفت\n" +
//                            "می\u200Cتوان با او صمیمی حرف زد" + "مهم نیست که کجا و\n" +
//                            "\n" +
//                            "چجوری زندگی میکنی !\n" +
//                            "\n" +
//                            "مهم اینه که تو قلبت\n" +
//                            "\n" +
//                            "احساس خوشبختی کنی\n" +
//                            "\n" +
//                            "و چشمت به زندگی کسی نباشه"
//            );
//            ShowQuestionTextFragment fragInfo = ShowQuestionTextFragment.newInstance(textQuestion);
//            FragmentTransaction transaction = getFragmentManager().beginTransaction();
//            transaction.replace(R.id.fragment_container, fragInfo);
//            transaction.commit();
//        }catch (Exception e){
//
//        }
//        ShowQuestionsHolderFragment fragInfo = ShowQuestionsHolderFragment.newInstance(0);
////        ShowQuestionsHolderFragment fragInfo = ShowQuestionsHolderFragment.newAddQuestionsInstance();
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.replace(R.id.fragment_container, fragInfo);
//        transaction.commit();
//        ProfileFragment fragInfo = ProfileFragment.newInstance();
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.replace(R.id.fragment_container, fragInfo);
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
