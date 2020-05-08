package com.djaphar.babysitter.Fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.djaphar.babysitter.Activities.MainActivity;
import com.djaphar.babysitter.R;
import com.djaphar.babysitter.SupportClasses.Adapters.EventChildrenRecyclerViewAdapter;
import com.djaphar.babysitter.SupportClasses.Adapters.MealDialogRecyclerViewAdapter;
import com.djaphar.babysitter.SupportClasses.ApiClasses.Child;
import com.djaphar.babysitter.SupportClasses.ApiClasses.Event;
import com.djaphar.babysitter.SupportClasses.ApiClasses.Food;
import com.djaphar.babysitter.SupportClasses.ApiClasses.Meal;
import com.djaphar.babysitter.SupportClasses.ApiClasses.Ration;
import com.djaphar.babysitter.SupportClasses.OtherClasses.MyFragment;
import com.djaphar.babysitter.SupportClasses.OtherClasses.ViewDriver;
import com.djaphar.babysitter.ViewModels.EventViewModel;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EventFragment extends MyFragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, View.OnClickListener {

    private EventViewModel eventViewModel;
    private MainActivity mainActivity;
    private Context context;
    private RecyclerView eventChildrenRecyclerView;
    private ConstraintLayout eventContainer;
    private ScrollView eventSv, eventChildrenListSv;
    private LinearLayout eatingEventContainer, eventDateContainer, scheduleArriveContainer, scheduleLeaveContainer, sleepStartContainer, sleepEndContainer,
            eatingFoodContainer, eatingDenialContainer;
    private TextView eatingEventContent, eventDateContent, scheduleArriveContent, scheduleLeaveContent, sleepStartContent, sleepEndContent,
            eatingFoodContent, eatingDenialContent;
    private EditText otherEd;
    private Button scheduleSaveBtn, eatingSaveBtn, sleepingSaveBtn, otherSaveBtn;
    private Calendar calendar;
    private ArrayList<Food> foods;
    private ArrayList<Ration> tempRations = new ArrayList<>(), tempDenied = new ArrayList<>();
    private int curYear, curMonth, curDayOfMonth, mealType;
    private Boolean sleepStart, arrive;
    private Event currentEvent;
    private Child currentChild;
    private HashMap<String, String> authHeader = new HashMap<>();

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        View root = inflater.inflate(R.layout.fragment_event, container, false);
        eventChildrenRecyclerView = root.findViewById(R.id.event_children_recycler_view);
        eventChildrenListSv = root.findViewById(R.id.event_children_list_sv);
        eventContainer = root.findViewById(R.id.event_container);
        eventSv = root.findViewById(R.id.event_sv);
        eatingEventContainer = root.findViewById(R.id.eating_event_container);
        eventDateContainer = root.findViewById(R.id.event_date_container);
        scheduleArriveContainer = root.findViewById(R.id.schedule_arrive_container);
        scheduleLeaveContainer = root.findViewById(R.id.schedule_leave_container);
        sleepStartContainer = root.findViewById(R.id.sleep_start_container);
        sleepEndContainer = root.findViewById(R.id.sleep_end_container);
        eatingFoodContainer = root.findViewById(R.id.eating_food_container);
        eatingDenialContainer = root.findViewById(R.id.eating_denial_container);
        eatingEventContent = root.findViewById(R.id.eating_event_content);
        eventDateContent = root.findViewById(R.id.event_date_content);
        scheduleArriveContent = root.findViewById(R.id.schedule_arrive_content);
        scheduleLeaveContent = root.findViewById(R.id.schedule_leave_content);
        sleepStartContent = root.findViewById(R.id.sleep_start_content);
        sleepEndContent = root.findViewById(R.id.sleep_end_content);
        eatingFoodContent = root.findViewById(R.id.eating_food_content);
        eatingDenialContent = root.findViewById(R.id.eating_denial_content);
        otherEd = root.findViewById(R.id.other_ed);
        scheduleSaveBtn = root.findViewById(R.id.schedule_save_btn);
        eatingSaveBtn = root.findViewById(R.id.eating_save_btn);
        sleepingSaveBtn = root.findViewById(R.id.sleeping_save_btn);
        otherSaveBtn = root.findViewById(R.id.other_save_btn);
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

        eventViewModel.getEvent().observe(getViewLifecycleOwner(), currentEvent -> {
            if (currentEvent == null) {
                return;
            }
            this.currentEvent = currentEvent;
            mealType = 1;
            setEventOptions();
        });

        eventViewModel.getFoods().observe(getViewLifecycleOwner(), foods -> {
            if (foods == null) {
                return;
            }
            this.foods = foods;
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

        eatingEventContainer.setOnClickListener(lView -> {
            View inflatedView = View.inflate(context, R.layout.meal_event_dialog, null);
            AlertDialog dialog = new AlertDialog.Builder(mainActivity).setView(inflatedView).create();
            dialog.show();
            inflatedView.findViewById(R.id.breakfast).setOnClickListener(mealTv -> {
                mealType = 1;
                setMealTvOptions();
                dialog.cancel();
            });
            inflatedView.findViewById(R.id.lunch).setOnClickListener(mealTv -> {
                mealType = 2;
                setMealTvOptions();
                dialog.cancel();
            });
            inflatedView.findViewById(R.id.snack).setOnClickListener(mealTv -> {
                mealType = 3;
                setMealTvOptions();
                dialog.cancel();
            });
        });

        eatingFoodContainer.setOnClickListener(lView -> {
            tempRations.clear();
            View inflatedView = View.inflate(context, R.layout.recycler_meal_dialog, null);
            RecyclerView mealDialogRecyclerView = inflatedView.findViewById(R.id.meal_dialog_recycler_view);
            ArrayList<Ration> rations = new ArrayList<>();
            boolean emptyMeal = true;
            for (Meal meal : currentEvent.getMeals()) {
                if (meal.getType() == mealType) {
                    rations = meal.getRations();
                    emptyMeal = false;
                    break;
                }
            }
            if (emptyMeal) {
                currentEvent.getMeals().add(new Meal(new ArrayList<>(), mealType));
            }
            mealDialogRecyclerView.setAdapter(new MealDialogRecyclerViewAdapter(foods, rations, this, false));
            mealDialogRecyclerView.setNestedScrollingEnabled(false);
            mealDialogRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            new AlertDialog.Builder(mainActivity)
                    .setView(inflatedView)
                    .setTitle(R.string.eating_food_text)
                    .setNegativeButton(R.string.cancel_button, (dialogInterface, i) -> dialogInterface.cancel())
                    .setPositiveButton(R.string.ok_button, (dialogInterface, i) -> {
                        for (Meal meal : currentEvent.getMeals()) {
                            if (meal.getType() == mealType) {
                                meal.getRations().clear();
                                meal.getRations().addAll(0, tempRations);
                                tempRations.clear();
                                break;
                            }
                        }
                        setMealTvOptions();
                    })
                    .show();
        });

        eatingDenialContainer.setOnClickListener(lView -> {
            boolean emptyMeal = true;
            for (Meal meal : currentEvent.getMeals()) {
                if (meal.getType() == mealType && meal.getRations().size() > 0) {
                    emptyMeal = false;
                    break;
                }
            }
            if (!emptyMeal) {
                tempDenied.clear();
                View inflatedView = View.inflate(context, R.layout.recycler_meal_dialog, null);
                RecyclerView mealDialogRecyclerView = inflatedView.findViewById(R.id.meal_dialog_recycler_view);
                ArrayList<Ration> rations = new ArrayList<>();
                ArrayList<Food> foods = new ArrayList<>();
                for (Meal meal : currentEvent.getMeals()) {
                    if (meal.getType() == mealType) {
                        for (Ration ration : meal.getRations()) {
                            foods.add(new Food(ration.getFoodId(), ration.getName()));
                            if (ration.getDenial()) {
                                rations.add(ration);
                            }
                        }
                    }
                }
                mealDialogRecyclerView.setAdapter(new MealDialogRecyclerViewAdapter(foods, rations, this, true));
                mealDialogRecyclerView.setNestedScrollingEnabled(false);
                mealDialogRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                new AlertDialog.Builder(mainActivity)
                        .setView(inflatedView)
                        .setTitle(R.string.eating_denial_text)
                        .setNegativeButton(R.string.cancel_button, (dialogInterface, i) -> dialogInterface.cancel())
                        .setPositiveButton(R.string.ok_button, (dialogInterface, i) -> {
                            for (Meal meal : currentEvent.getMeals()) {
                                if (meal.getType() == mealType) {
                                    for (Ration ration : meal.getRations()) {
                                        ration.setDenial(false);
                                        for (Ration r : tempDenied) {
                                            if (ration.getFoodId().equals(r.getFoodId())) {
                                                ration.setDenial(true);
                                                break;
                                            }
                                        }
                                    }
                                    break;
                                }
                            }
                            tempDenied.clear();
                            setMealTvOptions();
                        })
                        .show();
            }
        });

        scheduleSaveBtn.setOnClickListener(this);
        eatingSaveBtn.setOnClickListener(this);
        sleepingSaveBtn.setOnClickListener(this);
        otherSaveBtn.setOnClickListener(this);
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
        currentChild = child;
        setCalendarOptions(curYear, curMonth, curDayOfMonth);
        eventViewModel.requestEvent(authHeader, currentChild.getChildId(), getDateForRequest());
        eventDateContent.setText(DateFormat.getDateInstance().format(calendar.getTime()));
        String fullName = currentChild.getName() + " " + currentChild.getSurname();
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

    private void setEventOptions() {
        setEventTv(currentEvent.getHasCome(), scheduleArriveContent);
        setEventTv(currentEvent.getHasGone(), scheduleLeaveContent);
        setEventTv(currentEvent.getAsleep(), sleepStartContent);
        setEventTv(currentEvent.getAwoke(), sleepEndContent);
        String comment = currentEvent.getComment();
        if (comment == null) {
            comment = "";
        }
        otherEd.setText(comment);
        setMealTvOptions();
    }

    private void setEventTv(String value, TextView view) {
        if (value == null || value.equals("")) {
            value = getString(R.string.some_field_is_null);
        }
        view.setText(value);
    }

    private void setMealTvOptions() {
        switch (mealType) {
            case 1:
                eatingEventContent.setText(R.string.breakfast);
                break;
            case 2:
                eatingEventContent.setText(R.string.lunch);
                break;
            case 3:
                eatingEventContent.setText(R.string.snack);
                break;
        }

        ArrayList<Ration> rations = new ArrayList<>();
        ArrayList<Meal> meals = currentEvent.getMeals();
        if (meals != null) {
            for (Meal meal : currentEvent.getMeals()) {
                if (meal.getType() == mealType) {
                    rations = meal.getRations();
                    break;
                }
            }
        }

        StringBuilder foodContent = new StringBuilder();
        ArrayList<Ration> denied = new ArrayList<>();
        for (int i = 0; i < rations.size(); i++) {
            Ration ration  = rations.get(i);
            foodContent.append(ration.getName());
            if (i != rations.size() - 1) {
                foodContent.append("\n");
            }
            if (ration.getDenial()) {
                denied.add(ration);
            }
        }
        StringBuilder deniedContent = new StringBuilder();
        for (int i = 0; i < denied.size(); i++) {
            deniedContent.append(denied.get(i).getName());
            if (i != denied.size() - 1) {
                deniedContent.append("\n");
            }
        }
        eatingFoodContent.setText(foodContent.toString());
        eatingDenialContent.setText(deniedContent.toString());
    }

    public void setCheckedFood(Food food, boolean checked, boolean denied) {
        Ration ration = new Ration(food.getName(), food.getFoodId(), false);
        if (denied) {
            ration.setDenial(checked);
            if (checked) {
                tempDenied.add(ration);
            } else {
                for (Ration r : tempDenied) {
                    if (r.getFoodId().equals(ration.getFoodId())) {
                        tempDenied.remove(r);
                        break;
                    }
                }
            }
        } else {
            for (Meal meal : currentEvent.getMeals()) {
                if (meal.getType() == mealType) {
                    for (Ration r : meal.getRations()) {
                        if (r.getFoodId().equals(ration.getFoodId())) {
                            ration.setDenial(r.getDenial());
                            break;
                        }
                    }
                }
            }
            if (checked) {
                tempRations.add(ration);
            } else {
                for (Ration r : tempRations) {
                    if (r.getFoodId().equals(ration.getFoodId())) {
                        tempRations.remove(r);
                        break;
                    }
                }
            }
        }
    }

    private String getDateForRequest() {
        @SuppressLint("DefaultLocale") String date =
                String.format("%04d", calendar.get(Calendar.YEAR)) + "-" +
                String.format("%02d", calendar.get(Calendar.MONTH) + 1) + "-" +
                String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));
        return date;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        setCalendarOptions(year, month, dayOfMonth);
        eventViewModel.requestEvent(authHeader, currentChild.getChildId(), getDateForRequest());
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
               currentEvent.setHasCome(pickedTime);
               scheduleArriveContent.setText(pickedTime);
           } else {
               currentEvent.setHasGone(pickedTime);
               scheduleLeaveContent.setText(pickedTime);
           }
        } else if (sleepStart) {
            currentEvent.setAsleep(pickedTime);
            sleepStartContent.setText(pickedTime);
        } else {
            currentEvent.setAwoke(pickedTime);
            sleepEndContent.setText(pickedTime);
        }
    }

    @Override
    public void onClick(View view) {
        currentEvent.setComment(otherEd.getText().toString());
        eventViewModel.requestUpdateEvent(authHeader, currentEvent, currentChild.getChildId(), getDateForRequest());
    }
}
