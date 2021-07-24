package com.example.jasoali;

//import com.parse.Parse;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;


import com.example.jasoali.ui.problem.ShowQuestionsHolderFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
//import com.parse.ParseObject;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .enableLocalDataStore()
                .build());

        ParseObject firstObject = new ParseObject("FirstClass");
        firstObject.put("message", "Hey ! First message from android. Parse is now connected");
        firstObject.saveInBackground(e -> {
            if (e != null) {
                Log.e("MainActivity", e.getLocalizedMessage());
            } else {
                Log.d("MainActivity", "Object saved.");
            }
        });


        setContentView(R.layout.activity_main);
//        ShowQuestionsHolderFragment fragInfo = ShowQuestionsHolderFragment.newInstance(0);
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.replace(R.id.your_placeholder, fragInfo);
//        transaction.commit();

        BottomNavigationView navView = findViewById(R.id.nav_view);
         Passing each menu ID as a set of Ids because each
         menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

}