package com.djaphar.babysitter.SupportClasses.ApiClasses;

public class GalleryPicture {

    private String gallery_id, photo_link, description, timestamp;

    public GalleryPicture(String gallery_id, String photo_link, String description, String timestamp) {
        this.gallery_id = gallery_id;
        this.photo_link = photo_link;
        this.description = description;
        this.timestamp = timestamp;
    }

    public String getGalleryId() {
        return gallery_id;
    }

    public String getPhotoLink() {
        return photo_link;
    }

    public String getDescription() {
        return description;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setGalleryId(String gallery_id) {
        this.gallery_id = gallery_id;
    }

    public void setPhotoLink(String photo_link) {
        this.photo_link = photo_link;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
