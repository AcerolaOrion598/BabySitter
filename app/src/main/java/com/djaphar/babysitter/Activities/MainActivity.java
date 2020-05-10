package com.djaphar.babysitter.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.djaphar.babysitter.Fragments.ChildrenFragment;
import com.djaphar.babysitter.R;
import com.djaphar.babysitter.SupportClasses.Adapters.MainDialog;
import com.djaphar.babysitter.SupportClasses.OtherClasses.MyFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity implements MainDialog.MainDialogListener {

    private final static int SELECT_PICTURE_ID = 1;
    private TextView actionBarTitle, backBtn;
    private ConstraintLayout newBtnContainer;
    private ImageView newBtn;

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
            backBtn = findViewById(R.id.back_btn);
            newBtnContainer = findViewById(R.id.new_btn_container);
            backBtn.setOnClickListener(lView -> onBackPressed());
            newBtn = findViewById(R.id.new_btn);
            newBtn.setOnClickListener(lView -> newBtnWasPressed());
        }
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public void onBackPressed() {
        MyFragment currentFragment = getMyFragment();

        if (currentFragment == null) {
            return;
        }

        if (currentFragment.everythingIsClosed()) {
            super.onBackPressed();
            return;
        }
        currentFragment.backWasPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PICTURE_ID && resultCode == RESULT_OK && data != null) {
            ChildrenFragment childrenFragment = null;
            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
            if (navHostFragment != null) {
                childrenFragment = (ChildrenFragment) navHostFragment.getChildFragmentManager().getFragments().get(0);
            }
            if (childrenFragment == null) {
                return;
            }
            childrenFragment.setSelectedPicture(data.getData());
        }
    }

    public void setActionBarTitle(String title) {
        actionBarTitle.setText(title);
    }

    public void setBackBtnState(int visibilityState) {
        backBtn.setVisibility(visibilityState);
    }

    public void setNewBtnState(int visibilityState) {
        newBtnContainer.setVisibility(visibilityState);
    }

    private void newBtnWasPressed() {
        MyFragment myFragment = getMyFragment();
        if (myFragment == null) {
            return;
        }
        myFragment.newBtnWasPressed();
    }

    public void selectPicture() {
        startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), SELECT_PICTURE_ID);
    }

    public void logout() {
        startActivity(new Intent(this, AuthActivity.class));
        finish();
    }

    @Override
    public void returnFieldValue(String fieldValue, View calledView) {
        MyFragment myFragment = getMyFragment();
        if (myFragment == null) {
            return;
        }
        myFragment.returnFieldValue(fieldValue, calledView);
    }

    private MyFragment getMyFragment() {
        MyFragment myFragment = null;
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            myFragment = (MyFragment) navHostFragment.getChildFragmentManager().getFragments().get(0);
        }
        return myFragment;
    }

    public ImageView getNewBtn() {
        return newBtn;
    }
}
