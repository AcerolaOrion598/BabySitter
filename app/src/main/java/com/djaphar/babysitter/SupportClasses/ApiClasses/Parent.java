package com.djaphar.babysitter.SupportClasses.ApiClasses;

public class Parent {

    private String name, patronymic, surname, role, phoneNum, photoUrl;

    public Parent(String name, String patronymic, String surname, String role, String phoneNum, String photoUrl) {
        this.name = name;
        this.patronymic = patronymic;
        this.surname = surname;
        this.role = role;
        this.phoneNum = phoneNum;
        this.photoUrl = photoUrl;
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

    public String getRole() {
        return role;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getPhotoUrl() {
        return photoUrl;
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

    public void setRole(String role) {
        this.role = role;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
