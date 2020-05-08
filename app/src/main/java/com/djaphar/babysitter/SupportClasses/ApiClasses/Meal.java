package com.djaphar.babysitter.SupportClasses.ApiClasses;

import java.util.ArrayList;

public class Meal {

    private ArrayList<Ration> rations;
    private Integer type;

    public Meal(ArrayList<Ration> rations, Integer type) {
        this.rations = rations;
        this.type = type;
    }

    public ArrayList<Ration> getRations() {
        return rations;
    }

    public Integer getType() {
        return type;
    }

    public void setRations(ArrayList<Ration> rations) {
        this.rations = rations;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
