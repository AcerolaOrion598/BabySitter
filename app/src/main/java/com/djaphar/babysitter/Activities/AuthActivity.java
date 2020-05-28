package com.djaphar.babysitter.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.djaphar.babysitter.R;
import com.djaphar.babysitter.ViewModels.AuthViewModel;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.action_bar);
            TextView actionBarTitle = findViewById(R.id.action_bar_title);
            actionBarTitle.setText(R.string.login_title);
        }

        EditText loginEd = findViewById(R.id.login_ed);
        EditText passwordEd = findViewById(R.id.password_ed);
        Button loginBtn = findViewById(R.id.login_btn);

        AuthViewModel authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        authViewModel.getUser().observe(this, user -> {
            if (user == null) {
                return;
            }
            startMainActivity();
        });

        loginBtn.setOnClickListener(lView -> authViewModel.loginRequest(loginEd.getText().toString(), passwordEd.getText().toString()));
    }

    private void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
