package com.djaphar.babysitter.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.TextView;

import com.djaphar.babysitter.Activities.MainActivity;
import com.djaphar.babysitter.R;
import com.djaphar.babysitter.SupportClasses.Adapters.MainDialog;
import com.djaphar.babysitter.SupportClasses.Adapters.MealRecyclerViewAdapter;
import com.djaphar.babysitter.SupportClasses.ApiClasses.Food;
import com.djaphar.babysitter.SupportClasses.OtherClasses.MyFragment;
import com.djaphar.babysitter.SupportClasses.OtherClasses.ViewDriver;
import com.djaphar.babysitter.ViewModels.SettingsViewModel;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SettingsFragment extends MyFragment {

    private SettingsViewModel settingsViewModel;
    private MainActivity mainActivity;
    private Context context;
    private RecyclerView mealRecyclerView;
    private ConstraintLayout settingsContainer, mealsContainer;
    private TextView logoutTv, mealListTv;
    private ImageButton newMealBtn;
    private HashMap<String, String> authHeader = new HashMap<>();

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings,  container, false);
        mealRecyclerView = root.findViewById(R.id.meal_recycler_view);
        settingsContainer = root.findViewById(R.id.settings_container);
        mealsContainer = root.findViewById(R.id.meals_container);
        logoutTv = root.findViewById(R.id.logout_tv);
        mealListTv = root.findViewById(R.id.meal_list_tv);
        newMealBtn = root.findViewById(R.id.new_meal_btn);
        context = getContext();
        mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            setActionBarTitle(getString(R.string.settings));
            setBackBtnState(false);
        }
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        settingsViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                authHeader.put(getString(R.string.auth_header_key), user.getToken_type() + " " + user.getAccess_token());
                return;
            }
            mainActivity.logout();
        });

        settingsViewModel.getFoods().observe(getViewLifecycleOwner(), foods -> {
            if (foods == null) {
                return;
            }
            mealRecyclerView.setAdapter(new MealRecyclerViewAdapter(foods, this));
            mealRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        });

        logoutTv.setOnClickListener(lView -> showStandardDialog(getString(R.string.logout_text), getString(R.string.logout_message), null));

        mealListTv.setOnClickListener(lView -> showFoodsContainer());

        newMealBtn.setOnClickListener(lView -> new MainDialog(getString(R.string.new_meal_title), "", lView)
                .show(getParentFragmentManager(), "dialog"));
    }

    private void setActionBarTitle(String title) {
        mainActivity.setActionBarTitle(title);
    }

    private void setBackBtnState(boolean visible) {
        mainActivity.setBackBtnState(visible);
    }

    public boolean everythingIsClosed() {
        return mealsContainer.getVisibility() != View.VISIBLE;
    }

    public void backWasPressed() {
        settingsContainer.setVisibility(View.VISIBLE);
        setActionBarTitle(getString(R.string.settings));
        setBackBtnState(false);
        ViewDriver.toggleChildViewsEnable(mealsContainer, false);
        ViewDriver.hideView(mealsContainer, R.anim.hide_right_animation, context);
    }

    public void returnFieldValue(String fieldValue, View calledView) {
        if (calledView.getId() == R.id.new_meal_btn) {
            settingsViewModel.requestCreateFood(authHeader, new Food(null, fieldValue));
        }
    }

    public void deleteMeal(Food food) {
        showStandardDialog(getString(R.string.delete_meal_title), getString(R.string.delete_meal_message), food);
    }

    private void showFoodsContainer() {
        settingsViewModel.requestMyFoods(authHeader);
        setActionBarTitle(getString(R.string.meal_list_tv_text));
        setBackBtnState(true);
        ViewDriver.toggleChildViewsEnable(mealsContainer, true);
        ViewDriver.showView(mealsContainer, R.anim.show_right_animation, context).setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                settingsContainer.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
    }

    private void showStandardDialog(String title, String message, Food food) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(R.string.cancel_button, (dialogInterface, i) -> dialogInterface.cancel())
                .setPositiveButton(R.string.ok_button, (dialogInterface, i) -> {
                    if (food == null) {
                        settingsViewModel.logout();
                    } else {
                        settingsViewModel.requestDeleteFood(authHeader, food.getFoodId());
                    }
                })
                .show();
    }
}
