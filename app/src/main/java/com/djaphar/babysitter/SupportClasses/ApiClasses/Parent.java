package com.djaphar.babysitter.SupportClasses.ApiClasses;

public class Parent {

    private String name, patronymic, surname, relation_degree, photo_link, phone;

    public Parent(String name, String patronymic, String surname, String relation_degree, String photo_link, String phone) {
        this.name = name;
        this.patronymic = patronymic;
        this.surname = surname;
        this.relation_degree = relation_degree;
        this.photo_link = photo_link;
        this.phone = phone;
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

    public String getRelationDegree() {
        return relation_degree;
    }

    public String getPhotoLink() {
        return photo_link;
    }

    public String getPhone() {
        return phone;
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

    public void setRelationDegree(String relation_degree) {
        this.relation_degree = relation_degree;
    }

    public void setPhotoLink(String photo_link) {
        this.photo_link = photo_link;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
