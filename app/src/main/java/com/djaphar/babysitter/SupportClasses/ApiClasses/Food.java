package com.djaphar.babysitter.SupportClasses.ApiClasses;

public class Food {

    private String food_id, name;

    public Food(String food_id, String name) {
        this.food_id = food_id;
        this.name = name;
    }

    public String getFoodId() {
        return food_id;
    }

    public String getName() {
        return name;
    }

    public void setFoodId(String food_id) {
        this.food_id = food_id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
