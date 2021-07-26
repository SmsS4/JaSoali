package com.example.jasoali.ui.sign_in_up;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jasoali.R;
import com.example.jasoali.api.DBConnection;
import com.example.jasoali.models.User;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        configRegisterButton();
    }

    private void configRegisterButton() {
        EditText nameEditText = findViewById(R.id.registerNameEditText);
        EditText usernameEditText = findViewById(R.id.registerUsernameEditText);
        EditText emailEditText = findViewById(R.id.registerEmailEditText);
        EditText passwordEditText = findViewById(R.id.registerPasswordEditText);
        EditText password2EditText = findViewById(R.id.registerPassword2EditText);

        Button loginBtn = findViewById(R.id.registerBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = passwordEditText.toString();
                String repeatPassword = password2EditText.toString();
                if (password != repeatPassword) {
                    Toast toast = Toast.makeText(
                            RegisterActivity.this,
                            "عدم تطابق رمزهای عبور",
                            Toast.LENGTH_LONG
                    );
                    return;
                }

                String name = nameEditText.toString();
                String username = usernameEditText.toString();
                String email = emailEditText.toString();

                User user = new User("", username, password, email, name, null, false);
                DBConnection db = new DBConnection(null);
                db.register(user);
            }
        });
    }
}