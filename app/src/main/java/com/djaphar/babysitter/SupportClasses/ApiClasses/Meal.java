package com.djaphar.babysitter.SupportClasses.ApiClasses;

public class Meal {

    private String foodName;

    public Meal(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }
}
