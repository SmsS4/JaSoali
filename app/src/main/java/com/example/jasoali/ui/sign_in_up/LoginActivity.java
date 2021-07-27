package com.example.jasoali.ui.sign_in_up;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jasoali.MainActivity;
import com.example.jasoali.R;
import com.example.jasoali.api.DBConnection;

import java.lang.ref.WeakReference;

public class LoginActivity extends AppCompatActivity {
    public static final int LOGIN_SUCCESSFUL_RESULT_CODE = 0;
    public static final int LOGIN_FAILED_RESULT_CODE = 1;
    private int requestsCount;
    private ProgressBar progressBar;
    private DBConnection db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        configLoginButton();
        configRegisterLink();
        hideActionBar();
        initFields();
    }

    private void initFields() {
        Handler handler = new LoginHandler(Looper.myLooper(), this);
        db = new DBConnection(handler);
        progressBar = findViewById(R.id.loginProgressBar);
        setRequestsCount(0);
    }

    private void hideActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();
    }

    private void configLoginButton() {
        EditText usernameEditText = findViewById(R.id.loginUsernameEditText);
        EditText emailEditText = findViewById(R.id.loginEmailEditText);
        EditText passwordEditText = findViewById(R.id.loginPasswordEditText);

        Button loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                addRequestsCount(+1);
                db.login(username, password, email);
            }
        });
    }

    private void configRegisterLink() {
        SpannableString ss = new SpannableString("حساب کاربری ندارید؟ بسازید!");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        String pattern = "بسازید";
        int beginPos = ss.toString().indexOf(pattern);
        int endPos = beginPos + pattern.length();
        ss.setSpan(clickableSpan, beginPos, endPos, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        TextView textView = findViewById(R.id.registerLink);
        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setHighlightColor(Color.TRANSPARENT);
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

    private void showError(String text) {
        @SuppressLint("ShowToast") Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        toast.show();
    }

    private void endLogin() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private static class LoginHandler extends Handler {
        WeakReference<LoginActivity> mActivity;

        public LoginHandler(@NonNull Looper looper, LoginActivity activity) {
            super(looper);
            this.mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            LoginActivity activity = mActivity.get();
            if (activity == null) return;
            activity.addRequestsCount(-1);
            if (msg.what == LOGIN_SUCCESSFUL_RESULT_CODE) {
                activity.endLogin();
            } else {
                String text = (String) msg.obj;
                activity.showError(text);
            }
        }
    }
}