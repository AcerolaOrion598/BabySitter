package com.djaphar.babysitter.SupportClasses.ApiClasses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Child {

    private String child_id, photo_link, blood_type, locker_num, name, surname, patronymic;
    private Integer age;
    private ArrayList<Parent> parents;

    public Child(String child_id, String photo_link, String blood_type, String locker_num, String name,
                 String surname, String patronymic, Integer age, ArrayList<Parent> parents) {
        this.child_id = child_id;
        this.photo_link = photo_link;
        this.blood_type = blood_type;
        this.locker_num = locker_num;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.age = age;
        this.parents = parents;
    }

    public String getChildId() {
        return child_id;
    }

    public String getPhotoLink() {
        return photo_link;
    }

    public String getBloodType() {
        return blood_type;
    }

    public String getLockerNum() {
        return locker_num;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public Integer getAge() {
        return age;
    }

    public ArrayList<Parent> getParents() {
        return parents;
    }

    public void setChildId(String child_id) {
        this.child_id = child_id;
    }

    public void setPhotoLink(String photo_link) {
        this.photo_link = photo_link;
    }

    public void setBloodType(String blood_type) {
        this.blood_type = blood_type;
    }

    public void setLockerNum(String locker_num) {
        this.locker_num = locker_num;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setParents(ArrayList<Parent> parents) {
        this.parents = parents;
    }
}
