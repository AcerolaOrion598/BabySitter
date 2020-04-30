package com.djaphar.babysitter.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.djaphar.babysitter.Activities.MainActivity;
import com.djaphar.babysitter.R;
import com.djaphar.babysitter.SupportClasses.Adapters.EventChildrenRecyclerViewAdapter;
import com.djaphar.babysitter.SupportClasses.Adapters.MealDialogRecyclerViewAdapter;
import com.djaphar.babysitter.SupportClasses.ApiClasses.Kid;
import com.djaphar.babysitter.SupportClasses.ApiClasses.Meal;
import com.djaphar.babysitter.SupportClasses.OtherClasses.MyFragment;
import com.djaphar.babysitter.SupportClasses.OtherClasses.ViewDriver;
import com.djaphar.babysitter.ViewModels.EventViewModel;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EventFragment extends MyFragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private EventViewModel eventViewModel;
    private MainActivity mainActivity;
    private Context context;
    private RecyclerView eventChildrenRecyclerView;
    private ConstraintLayout eventContainer;
    private ScrollView eventSv, eventChildrenListSv;
    private LinearLayout eventDateContainer, sleepStartContainer, sleepEndContainer, eatingFoodContainer, eatingDenialContainer;
    private TextView eventDateContent, sleepStartContent, sleepEndContent, eatingFoodContent, eatingDenialContent;
    private Calendar calendar;
    private ArrayList<Meal> meals, selectedMeals, tempSelectedMeals = new ArrayList<>(), deniedMeals, tempDeniedMeals = new ArrayList<>();
    private int curYear, curMonth, curDayOfMonth;
    private boolean sleepStart;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        View root = inflater.inflate(R.layout.fragment_event, container, false);
        eventChildrenRecyclerView = root.findViewById(R.id.event_children_recycler_view);
        eventChildrenListSv = root.findViewById(R.id.event_children_list_sv);
        eventContainer = root.findViewById(R.id.event_container);
        eventSv = root.findViewById(R.id.event_sv);
        eventDateContainer = root.findViewById(R.id.event_date_container);
        sleepStartContainer = root.findViewById(R.id.sleep_start_container);
        sleepEndContainer = root.findViewById(R.id.sleep_end_container);
        eatingFoodContainer = root.findViewById(R.id.eating_food_container);
        eatingDenialContainer = root.findViewById(R.id.eating_denial_container);
        eventDateContent = root.findViewById(R.id.event_date_content);
        sleepStartContent = root.findViewById(R.id.sleep_start_content);
        sleepEndContent = root.findViewById(R.id.sleep_end_content);
        eatingFoodContent = root.findViewById(R.id.eating_food_content);
        eatingDenialContent = root.findViewById(R.id.eating_denial_content);
        context = getContext();
        calendar = Calendar.getInstance();
        curYear = calendar.get(Calendar.YEAR);
        curMonth = calendar.get(Calendar.MONTH);
        curDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.setActionBarTitle(getString(R.string.title_event));
        }
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eventViewModel.getKids().observe(getViewLifecycleOwner(), kids -> {
            if (kids == null) {
                return;
            }
            eventChildrenRecyclerView.setAdapter(new EventChildrenRecyclerViewAdapter(kids, this));
            eventChildrenRecyclerView.setNestedScrollingEnabled(false);
            eventChildrenRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        });

        eventViewModel.getMeals().observe(getViewLifecycleOwner(), meals -> {
            if (meals == null) {
                return;
            }
            this.meals = meals;
        });

        eventViewModel.getSelectedMeals().observe(getViewLifecycleOwner(), selectedMeals -> {
            if (selectedMeals == null) {
                return;
            }
            this.selectedMeals = selectedMeals;
            setMealsTv(selectedMeals);
        });

        eventViewModel.getDeniedMeals().observe(getViewLifecycleOwner(), deniedMeals -> {
            if (deniedMeals == null) {
                return;
            }
            this.deniedMeals = deniedMeals;
            setMealsTv(deniedMeals);
        });

        eventDateContainer.setOnClickListener(lView -> new DatePickerDialog(mainActivity, this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show());

        sleepStartContainer.setOnClickListener(lView -> {
            sleepStart = true;
            new TimePickerDialog(mainActivity, this,
                    Calendar.getInstance().get(Calendar.HOUR), Calendar.getInstance().get(Calendar.MINUTE),
                    android.text.format.DateFormat.is24HourFormat(mainActivity)).show();
        });

        sleepEndContainer.setOnClickListener(lView -> {
            sleepStart = false;
            new TimePickerDialog(mainActivity, this,
                    Calendar.getInstance().get(Calendar.HOUR), Calendar.getInstance().get(Calendar.MINUTE),
                    android.text.format.DateFormat.is24HourFormat(mainActivity)).show();
        });

        eatingFoodContainer.setOnClickListener(lView -> {
            if (meals.size() == 0) {
                return;
            }
            View inflatedView = View.inflate(context, R.layout.recycler_meal_dialog, null);
            RecyclerView mealDialogRecyclerView = inflatedView.findViewById(R.id.meal_dialog_recycler_view);
            mealDialogRecyclerView.setAdapter(new MealDialogRecyclerViewAdapter(meals, selectedMeals, this, false));
            mealDialogRecyclerView.setNestedScrollingEnabled(false);
            mealDialogRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            new AlertDialog.Builder(mainActivity)
                    .setView(inflatedView)
                    .setTitle(R.string.eating_food_text)
                    .setNegativeButton(R.string.cancel_button, (dialogInterface, i) -> {
                        tempSelectedMeals.clear();
                        dialogInterface.cancel();
                    })
                    .setPositiveButton(R.string.ok_button, (dialogInterface, i) -> {
                        selectedMeals.clear();
                        selectedMeals.addAll(tempSelectedMeals.subList(0, tempSelectedMeals.size()));
                        tempSelectedMeals.clear();
                        rewriteDeniedMeals();
                        setMealsTv(selectedMeals);
                        setMealsTv(deniedMeals);
                    })
                    .create()
                    .show();
        });

        eatingDenialContainer.setOnClickListener(lView -> {
            if (selectedMeals.size() == 0) {
                return;
            }
            View inflatedView = View.inflate(context, R.layout.recycler_meal_dialog, null);
            RecyclerView mealDialogRecyclerView = inflatedView.findViewById(R.id.meal_dialog_recycler_view);
            mealDialogRecyclerView.setAdapter(new MealDialogRecyclerViewAdapter(selectedMeals, deniedMeals, this, true));
            mealDialogRecyclerView.setNestedScrollingEnabled(false);
            mealDialogRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            new AlertDialog.Builder(mainActivity)
                    .setView(inflatedView)
                    .setTitle(R.string.eating_denial_text)
                    .setNegativeButton(R.string.cancel_button, (dialogInterface, i) -> {
                        tempDeniedMeals.clear();
                        dialogInterface.cancel();
                    })
                    .setPositiveButton(R.string.ok_button, (dialogInterface, i) -> {
                        deniedMeals.clear();
                        deniedMeals.addAll(tempDeniedMeals.subList(0, tempDeniedMeals.size()));
                        tempDeniedMeals.clear();
                        setMealsTv(deniedMeals);
                    })
                    .create()
                    .show();
        });
    }

    public boolean everythingIsClosed() {
        return eventContainer.getVisibility() != View.VISIBLE;
    }

    public void backWasPressed() {
        mainActivity.setActionBarTitle(getString(R.string.title_event));
        ViewDriver.toggleChildViewsEnable(eventContainer, false);
        eventChildrenListSv.setVisibility(View.VISIBLE);
        ViewDriver.hideView(eventContainer, R.anim.hide_right_animation, context);
    }

    public void showKidEvent(Kid kid) {
        setCalendarOptions(curYear, curMonth, curDayOfMonth);
        eventDateContent.setText(DateFormat.getDateInstance().format(calendar.getTime()));
        String fullName = kid.getName() + " " + kid.getSurname();
        mainActivity.setActionBarTitle(fullName);
        eventSv.scrollTo(0, eventSv.getTop());
        ViewDriver.toggleChildViewsEnable(eventContainer, true);
        Animation animation = ViewDriver.showView(eventContainer, R.anim.show_right_animation, context);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                eventChildrenListSv.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        setCalendarOptions(year, month, dayOfMonth);
        eventDateContent.setText(DateFormat.getDateInstance().format(calendar.getTime()));
    }

    private void setCalendarOptions(int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        @SuppressLint("DefaultLocale") String pickedTime = String.format("%02d:%02d", hour, minute);
        if (sleepStart) {
            sleepStartContent.setText(pickedTime);
        } else {
            sleepEndContent.setText(pickedTime);
        }
    }

    public void setCheckedFood(Meal meal, boolean checked, boolean denied) {
        if (denied) {
            if (checked) {
                tempDeniedMeals.add(meal);
            } else {
                tempDeniedMeals.remove(meal);
            }
        } else {
            if (checked) {
                tempSelectedMeals.add(meal);
            } else {
                tempSelectedMeals.remove(meal);
            }
        }
    }

    private void setMealsTv(ArrayList<Meal> meals) {
        StringBuilder mealsString = new StringBuilder();
        if (meals.size() == 0) {
            mealsString.append("Н/д");
        }

        for (int i = 0; i < meals.size(); i++) {
            mealsString.append(meals.get(i).getFoodName());
            if (i != meals.size() - 1) {
                mealsString.append("\n");
            }
        }

        if (meals == selectedMeals) {
            eatingFoodContent.setText(mealsString);
        } else {
            eatingDenialContent.setText(mealsString);
        }
    }

    private void rewriteDeniedMeals() {
        ArrayList<Meal> t = new ArrayList<>(deniedMeals.subList(0, deniedMeals.size()));
        for (Meal deniedMeal : deniedMeals) {
            int i = 0;
            for (Meal selectedMeal : selectedMeals) {
                if (deniedMeal.getFoodName().equals(selectedMeal.getFoodName())) {
                    i++;
                    break;
                }
            }
            if (i > 0) {
                continue;
            }
            t.remove(deniedMeal);
        }
        deniedMeals.clear();
        deniedMeals.addAll(t.subList(0, t.size()));
    }
}
