package com.example.jasoali.ui.sign_in_up;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jasoali.R;
import com.example.jasoali.api.DBConnection;
import com.example.jasoali.models.User;

import java.lang.ref.WeakReference;

public class RegisterActivity extends AppCompatActivity {
    public static final int REGISTER_SUCCESSFUL_RESULT_CODE = 0;
    public static final int REGISTER_FAILED_RESULT_CODE = 1;

    private int requestsCount;
    private ProgressBar progressBar;
    private DBConnection db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        configRegisterButton();
        hideActionBar();
        initFields();
    }

    private void hideActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();
    }

    private void initFields() {
        Handler handler = new RegisterActivity.RegisterHandler(Looper.myLooper(), this);
        db = new DBConnection(handler);
        progressBar = findViewById(R.id.registerProgressBar);
        setRequestsCount(0);
    }

    private void configRegisterButton() {
        EditText nameEditText = findViewById(R.id.registerNameEditText);
        EditText usernameEditText = findViewById(R.id.registerUsernameEditText);
        EditText emailEditText = findViewById(R.id.registerEmailEditText);
        EditText passwordEditText = findViewById(R.id.registerPasswordEditText);
        EditText password2EditText = findViewById(R.id.registerPassword2EditText);

        Button registerBtn = findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(v -> {
            String password = passwordEditText.getText().toString();
            String repeatPassword = password2EditText.getText().toString();
            if (!password.equals(repeatPassword)) {
                System.out.println(password + " " + repeatPassword);
                showError("عدم تطابق رمزهای عبور");
                return;
            }

            String name = nameEditText.getText().toString();
            String username = usernameEditText.getText().toString();
            String email = emailEditText.getText().toString();

            User user = new User("", username, password, email, name, false);
            db.register(user);
        });
    }

    private void showError(String text) {
        @SuppressLint("ShowToast") Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        toast.show();
    }

    private void addRequestsCount(int cnt) {
        setRequestsCount(requestsCount + cnt);
    }

    private void setRequestsCount(int cnt) {
        requestsCount = cnt;
        if (requestsCount == 0)
            progressBar.setVisibility(View.INVISIBLE);
        else
            progressBar.setVisibility(View.VISIBLE);
    }

    private static class RegisterHandler extends Handler {
        WeakReference<RegisterActivity> mActivity;

        public RegisterHandler(@NonNull Looper looper, RegisterActivity activity) {
            super(looper);
            this.mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            RegisterActivity activity = mActivity.get();
            if (activity == null) return;
            activity.addRequestsCount(-1);
            if (msg.what == REGISTER_SUCCESSFUL_RESULT_CODE) {
                activity.showError("حساب شما افتتاح شد.");
                activity.finish();
            } else {
                String text = (String) msg.obj;
                activity.showError(text);
            }
        }
    }
}