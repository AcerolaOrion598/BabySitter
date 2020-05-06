package com.djaphar.babysitter.SupportClasses.ApiClasses;

public class Bill {

    private String theme;
    private Child child;
    private Boolean status;
    private Float price;
    private Integer id;

    public Bill(String theme, Child child, Boolean status, Float price, Integer id) {
        this.theme = theme;
        this.child = child;
        this.status = status;
        this.price = price;
        this.id = id;
    }

    public String getTheme() {
        return theme;
    }

    public Child getChild() {
        return child;
    }

    public Boolean getStatus() {
        return status;
    }

    public Float getPrice() {
        return price;
    }

    public Integer getId() {
        return id;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void setChild(Child child) {
        this.child = child;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
