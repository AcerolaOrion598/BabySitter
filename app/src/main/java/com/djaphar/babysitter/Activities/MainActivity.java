package com.djaphar.babysitter.Activities;

import android.os.Bundle;
import android.widget.TextView;

import com.djaphar.babysitter.R;
import com.djaphar.babysitter.SupportClasses.OtherClasses.MyFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private TextView actionBarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.actoin_bar);
            actionBarTitle = findViewById(R.id.action_bar_title);
        }
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public void onBackPressed() {
        MyFragment currentFragment = null;
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            currentFragment = (MyFragment) navHostFragment.getChildFragmentManager().getFragments().get(0);
        }

        if (currentFragment == null) {
            return;
        }

        if (currentFragment.everythingIsClosed()) {
            super.onBackPressed();
        } else {
            currentFragment.backWasPressed();
        }
    }

    public void setActionBarTitle(String title) {
        actionBarTitle.setText(title);
    }
}
