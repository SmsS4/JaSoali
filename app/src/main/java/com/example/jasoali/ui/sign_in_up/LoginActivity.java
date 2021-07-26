package com.example.jasoali.ui.sign_in_up;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jasoali.R;
import com.example.jasoali.api.DBConnection;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        configLoginButton();
    }

    private void configLoginButton() {
        EditText usernameEditText = findViewById(R.id.loginUsernameEditText);
        EditText emailEditText = findViewById(R.id.loginEmailEditText);
        EditText passwordEditText = findViewById(R.id.loginPasswordEditText);

        Button loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.toString();
                String email = emailEditText.toString();
                String password = passwordEditText.toString();
                DBConnection db = new DBConnection(null);
                db.login(username, password, email);
            }
        });
    }
}