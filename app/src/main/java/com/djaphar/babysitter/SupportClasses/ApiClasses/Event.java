package com.djaphar.babysitter.SupportClasses.ApiClasses;

import java.util.ArrayList;

public class Event {

    private String child_id, date, has_come, has_gone, asleep, awoke, comment;
    private ArrayList<Meal> meals;

    public Event(String child_id, String date, String has_come, String has_gone, String asleep, String awoke, String comment,
                 ArrayList<Meal> meals) {
        this.child_id = child_id;
        this.date = date;
        this.has_come = has_come;
        this.has_gone = has_gone;
        this.asleep = asleep;
        this.awoke = awoke;
        this.comment = comment;
        this.meals = meals;
    }

    public String getChildId() {
        return child_id;
    }

    public String getDate() {
        return date;
    }

    public String getHasCome() {
        return has_come;
    }

    public String getHasGone() {
        return has_gone;
    }

    public String getAsleep() {
        return asleep;
    }

    public String getAwoke() {
        return awoke;
    }

    public String getComment() {
        return comment;
    }

    public ArrayList<Meal> getMeals() {
        return meals;
    }

    public void setChildId(String child_id) {
        this.child_id = child_id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setHasCome(String has_come) {
        this.has_come = has_come;
    }

    public void setHasGone(String has_gone) {
        this.has_gone = has_gone;
    }

    public void setAsleep(String asleep) {
        this.asleep = asleep;
    }

    public void setAwoke(String awoke) {
        this.awoke = awoke;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setMeals(ArrayList<Meal> meals) {
        this.meals = meals;
    }
}
