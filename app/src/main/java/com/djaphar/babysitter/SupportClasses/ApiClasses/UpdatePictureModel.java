package com.djaphar.babysitter.SupportClasses.ApiClasses;

public class UpdatePictureModel {

    private String id, profile, photo_base64;

    public UpdatePictureModel(String id, String profile, String photo_base64) {
        this.id = id;
        this.profile = profile;
        this.photo_base64 = photo_base64;
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

    public void setId(String id) {
        this.id = id;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public void setPhotoBase64(String photo_base64) {
        this.photo_base64 = photo_base64;
    }
}
