package com.djaphar.babysitter.SupportClasses.ApiClasses;

public class BillPostModel {

    private String bill_id, child_id, theme, comment;
    private Float sum;
    private Boolean status;

    public BillPostModel(String bill_id, String child_id, String theme, String comment, Float sum, Boolean status) {
        this.bill_id = bill_id;
        this.child_id = child_id;
        this.theme = theme;
        this.comment = comment;
        this.sum = sum;
        this.status = status;
    }

    public String getBillId() {
        return bill_id;
    }

    public String getChildId() {
        return child_id;
    }

    public String getTheme() {
        return theme;
    }

    public String getComment() {
        return comment;
    }

    public Float getSum() {
        return sum;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setBillId(String bill_id) {
        this.bill_id = bill_id;
    }

    public void setChildId(String child_id) {
        this.child_id = child_id;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setSum(Float sum) {
        this.sum = sum;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
