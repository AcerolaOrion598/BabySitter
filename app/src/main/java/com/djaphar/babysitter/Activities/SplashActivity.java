package com.djaphar.babysitter.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.djaphar.babysitter.ViewModels.AuthViewModel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AuthViewModel authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        authViewModel.getUser().observe(this, user -> {
            if (user == null) {
                startNextActivity(new Intent(this, AuthActivity.class));
            } else {
                startNextActivity(new Intent(this, MainActivity.class));
            }
        });
    }

    private void startNextActivity(Intent intent) {
        startActivity(intent);
        finish();
    }
}
