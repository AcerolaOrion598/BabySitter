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
import com.djaphar.babysitter.SupportClasses.ApiClasses.Child;
import com.djaphar.babysitter.SupportClasses.ApiClasses.Food;
import com.djaphar.babysitter.SupportClasses.OtherClasses.MyFragment;
import com.djaphar.babysitter.SupportClasses.OtherClasses.ViewDriver;
import com.djaphar.babysitter.ViewModels.EventViewModel;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

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
    private LinearLayout eventDateContainer, scheduleArriveContainer, scheduleLeaveContainer, sleepStartContainer,
            sleepEndContainer, eatingFoodContainer, eatingDenialContainer;
    private TextView eventDateContent, scheduleArriveContent, scheduleLeaveContent, sleepStartContent,
            sleepEndContent, eatingFoodContent, eatingDenialContent;
    private Calendar calendar;
    private ArrayList<Food> foods, selectedFoods, tempSelectedFoods = new ArrayList<>(), deniedFoods, tempDeniedFoods = new ArrayList<>();
    private int curYear, curMonth, curDayOfMonth;
    private Boolean sleepStart, arrive;
    private HashMap<String, String> authHeader = new HashMap<>();

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        View root = inflater.inflate(R.layout.fragment_event, container, false);
        eventChildrenRecyclerView = root.findViewById(R.id.event_children_recycler_view);
        eventChildrenListSv = root.findViewById(R.id.event_children_list_sv);
        eventContainer = root.findViewById(R.id.event_container);
        eventSv = root.findViewById(R.id.event_sv);
        eventDateContainer = root.findViewById(R.id.event_date_container);
        scheduleArriveContainer = root.findViewById(R.id.schedule_arrive_container);
        scheduleLeaveContainer = root.findViewById(R.id.schedule_leave_container);
        sleepStartContainer = root.findViewById(R.id.sleep_start_container);
        sleepEndContainer = root.findViewById(R.id.sleep_end_container);
        eatingFoodContainer = root.findViewById(R.id.eating_food_container);
        eatingDenialContainer = root.findViewById(R.id.eating_denial_container);
        eventDateContent = root.findViewById(R.id.event_date_content);
        scheduleArriveContent = root.findViewById(R.id.schedule_arrive_content);
        scheduleLeaveContent = root.findViewById(R.id.schedule_leave_content);
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
            setActionBarTitle(getString(R.string.title_event));
            setBackBtnState(false);
        }
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eventViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user == null) {
                return;
            }
            authHeader.put(getString(R.string.auth_header_key), user.getToken_type() + " " + user.getAccess_token());
            eventViewModel.requestChildrenList(authHeader);
            eventViewModel.requestMyFoods(authHeader);
        });

        eventViewModel.getChildren().observe(getViewLifecycleOwner(), children -> {
            if (children == null) {
                return;
            }
            eventChildrenRecyclerView.setAdapter(new EventChildrenRecyclerViewAdapter(children, this));
            eventChildrenRecyclerView.setNestedScrollingEnabled(false);
            eventChildrenRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        });

        eventViewModel.getFoods().observe(getViewLifecycleOwner(), foods -> {
            if (foods == null) {
                return;
            }
            this.foods = foods;
        });

        eventViewModel.getSelectedMeals().observe(getViewLifecycleOwner(), selectedMeals -> {
            if (selectedMeals == null) {
                return;
            }
            this.selectedFoods = selectedMeals;
            setMealsTv(selectedMeals);
        });

        eventViewModel.getDeniedMeals().observe(getViewLifecycleOwner(), deniedMeals -> {
            if (deniedMeals == null) {
                return;
            }
            this.deniedFoods = deniedMeals;
            setMealsTv(deniedMeals);
        });

        eventDateContainer.setOnClickListener(lView -> new DatePickerDialog(mainActivity, this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show());

        scheduleArriveContainer.setOnClickListener(lView -> {
            arrive = true;
            sleepStart = null;
            new TimePickerDialog(mainActivity, this,
                    Calendar.getInstance().get(Calendar.HOUR), Calendar.getInstance().get(Calendar.MINUTE),
                    android.text.format.DateFormat.is24HourFormat(mainActivity)).show();
        });

        scheduleLeaveContainer.setOnClickListener(lView -> {
            arrive = false;
            sleepStart = null;
            new TimePickerDialog(mainActivity, this,
                    Calendar.getInstance().get(Calendar.HOUR), Calendar.getInstance().get(Calendar.MINUTE),
                    android.text.format.DateFormat.is24HourFormat(mainActivity)).show();
        });

        sleepStartContainer.setOnClickListener(lView -> {
            sleepStart = true;
            arrive = null;
            new TimePickerDialog(mainActivity, this,
                    Calendar.getInstance().get(Calendar.HOUR), Calendar.getInstance().get(Calendar.MINUTE),
                    android.text.format.DateFormat.is24HourFormat(mainActivity)).show();
        });

        sleepEndContainer.setOnClickListener(lView -> {
            sleepStart = false;
            arrive = null;
            new TimePickerDialog(mainActivity, this,
                    Calendar.getInstance().get(Calendar.HOUR), Calendar.getInstance().get(Calendar.MINUTE),
                    android.text.format.DateFormat.is24HourFormat(mainActivity)).show();
        });

        eatingFoodContainer.setOnClickListener(lView -> {
            if (foods.size() == 0) {
                return;
            }
            View inflatedView = View.inflate(context, R.layout.recycler_meal_dialog, null);
            RecyclerView mealDialogRecyclerView = inflatedView.findViewById(R.id.meal_dialog_recycler_view);
            mealDialogRecyclerView.setAdapter(new MealDialogRecyclerViewAdapter(foods, selectedFoods, this, false));
            mealDialogRecyclerView.setNestedScrollingEnabled(false);
            mealDialogRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            new AlertDialog.Builder(mainActivity)
                    .setView(inflatedView)
                    .setTitle(R.string.eating_food_text)
                    .setNegativeButton(R.string.cancel_button, (dialogInterface, i) -> {
                        tempSelectedFoods.clear();
                        dialogInterface.cancel();
                    })
                    .setPositiveButton(R.string.ok_button, (dialogInterface, i) -> {
                        selectedFoods.clear();
                        selectedFoods.addAll(tempSelectedFoods.subList(0, tempSelectedFoods.size()));
                        tempSelectedFoods.clear();
                        rewriteDeniedMeals();
                        setMealsTv(selectedFoods);
                        setMealsTv(deniedFoods);
                    })
                    .create()
                    .show();
        });

        eatingDenialContainer.setOnClickListener(lView -> {
            if (selectedFoods.size() == 0) {
                return;
            }
            View inflatedView = View.inflate(context, R.layout.recycler_meal_dialog, null);
            RecyclerView mealDialogRecyclerView = inflatedView.findViewById(R.id.meal_dialog_recycler_view);
            mealDialogRecyclerView.setAdapter(new MealDialogRecyclerViewAdapter(selectedFoods, deniedFoods, this, true));
            mealDialogRecyclerView.setNestedScrollingEnabled(false);
            mealDialogRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            new AlertDialog.Builder(mainActivity)
                    .setView(inflatedView)
                    .setTitle(R.string.eating_denial_text)
                    .setNegativeButton(R.string.cancel_button, (dialogInterface, i) -> {
                        tempDeniedFoods.clear();
                        dialogInterface.cancel();
                    })
                    .setPositiveButton(R.string.ok_button, (dialogInterface, i) -> {
                        deniedFoods.clear();
                        deniedFoods.addAll(tempDeniedFoods.subList(0, tempDeniedFoods.size()));
                        tempDeniedFoods.clear();
                        setMealsTv(deniedFoods);
                    })
                    .create()
                    .show();
        });
    }

    private void setActionBarTitle(String title) {
        mainActivity.setActionBarTitle(title);
    }

    private void setBackBtnState(boolean visible) {
        mainActivity.setBackBtnState(visible);
    }

    public boolean everythingIsClosed() {
        return eventContainer.getVisibility() != View.VISIBLE;
    }

    public void backWasPressed() {
        setActionBarTitle(getString(R.string.title_event));
        setBackBtnState(false);
        ViewDriver.toggleChildViewsEnable(eventContainer, false);
        eventChildrenListSv.setVisibility(View.VISIBLE);
        ViewDriver.hideView(eventContainer, R.anim.hide_right_animation, context);
    }

    public void showKidEvent(Child child) {
        setCalendarOptions(curYear, curMonth, curDayOfMonth);
        eventDateContent.setText(DateFormat.getDateInstance().format(calendar.getTime()));
        String fullName = child.getName() + " " + child.getSurname();
        setActionBarTitle(fullName);
        setBackBtnState(true);
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

        if (arrive != null) {
           if (arrive) {
               scheduleArriveContent.setText(pickedTime);
           } else {
               scheduleLeaveContent.setText(pickedTime);
           }
        } else if (sleepStart) {
            sleepStartContent.setText(pickedTime);
        } else {
            sleepEndContent.setText(pickedTime);
        }
    }

    public void setCheckedFood(Food food, boolean checked, boolean denied) {
        if (denied) {
            if (checked) {
                tempDeniedFoods.add(food);
            } else {
                tempDeniedFoods.remove(food);
            }
        } else {
            if (checked) {
                tempSelectedFoods.add(food);
            } else {
                tempSelectedFoods.remove(food);
            }
        }
    }

    private void setMealsTv(ArrayList<Food> foods) {
        StringBuilder mealsString = new StringBuilder();
        if (foods.size() == 0) {
            mealsString.append(getString(R.string.some_field_is_null));
        }

        for (int i = 0; i < foods.size(); i++) {
            mealsString.append(foods.get(i).getName());
            if (i != foods.size() - 1) {
                mealsString.append("\n");
            }
        }

        if (foods == selectedFoods) {
            eatingFoodContent.setText(mealsString);
        } else {
            eatingDenialContent.setText(mealsString);
        }
    }

    private void rewriteDeniedMeals() {
        ArrayList<Food> t = new ArrayList<>(deniedFoods.subList(0, deniedFoods.size()));
        for (Food deniedFood : deniedFoods) {
            int i = 0;
            for (Food selectedFood : selectedFoods) {
                if (deniedFood.getName().equals(selectedFood.getName())) {
                    i++;
                    break;
                }
            }
            if (i > 0) {
                continue;
            }
            t.remove(deniedFood);
        }
        deniedFoods.clear();
        deniedFoods.addAll(t.subList(0, t.size()));
    }
}
