package com.djaphar.babysitter.SupportClasses.ApiClasses;

import java.util.ArrayList;

public class Kid {

    private String name, patronymic, surname, age, bloodType, photoUrl;
    private Integer locker;
    private ArrayList<Parent> parents;

    public Kid(String name, String patronymic, String surname, String age, String bloodType, String photoUrl, Integer locker, ArrayList<Parent> parents) {
        this.name = name;
        this.patronymic = patronymic;
        this.surname = surname;
        this.age = age;
        this.bloodType = bloodType;
        this.photoUrl = photoUrl;
        this.locker = locker;
        this.parents = parents;
    }

    public String getName() {
        return name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public String getSurname() {
        return surname;
    }

    public String getAge() {
        return age;
    }

    public String getBloodType() {
        return bloodType;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public Integer getLocker() {
        return locker;
    }

    public ArrayList<Parent> getParents() {
        return parents;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setLocker(Integer locker) {
        this.locker = locker;
    }

    public void setParents(ArrayList<Parent> parents) {
        this.parents = parents;
    }
}
