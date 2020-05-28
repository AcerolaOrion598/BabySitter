package com.djaphar.babysitter.SupportClasses.ApiClasses;

public class UpdatePictureModel {

    private String id, profile, photo_base64, description;

    public UpdatePictureModel(String id, String profile, String photo_base64, String description) {
        this.id = id;
        this.profile = profile;
        this.photo_base64 = photo_base64;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getProfile() {
        return profile;
    }

    public String getPhotoBase64() {
        return photo_base64;
    }

    public String getDescription() {
        return description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public void setPhotoBase64(String photo_base64) {
        this.photo_base64 = photo_base64;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
