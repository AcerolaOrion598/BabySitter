package com.djaphar.babysitter.SupportClasses.ApiClasses;

public class Ration {

    private String name, food_id;
    private Boolean denial;

    public Ration(String name, String food_id, Boolean denial) {
        this.name = name;
        this.food_id = food_id;
        this.denial = denial;
    }

    public String getName() {
        return name;
    }

    public String getFoodId() {
        return food_id;
    }

    public Boolean getDenial() {
        return denial;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFoodId(String food_id) {
        this.food_id = food_id;
    }

    public void setDenial(Boolean denial) {
        this.denial = denial;
    }
}
