package com.ahmedtaha.barakasky;

public class MainComments {
    public String uid, comment,date,time, date_time;
    public MainComments(){

    }

    public MainComments(String uid, String comment, String date, String time, String date_time) {
        this.uid = uid;
        this.comment = comment;
        this.date = date;
        this.time = time;
        this.date_time = date_time;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
