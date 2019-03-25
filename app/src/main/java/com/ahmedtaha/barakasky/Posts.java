package com.ahmedtaha.barakasky;

public class Posts {
    public String date, time, postimage,description, uid, next_date_time, dateandtime;
    public Posts(){

    }
    public Posts(String date, String time, String postimage, String description, String uid, String next_date_time, String dateandtime) {
        this.date = date;
        this.time = time;
        this.postimage = postimage;
        this.description = description;
        this.uid = uid;
        this.next_date_time = next_date_time;
        this.dateandtime = dateandtime;
    }

    public String getDateandtime() {
        return dateandtime;
    }

    public void setDateandtime(String dateandtime) {
        this.dateandtime = dateandtime;
    }

    public String getNext_date_time() {
        return next_date_time;
    }

    public void setNext_date_time(String next_date_time) {
        this.next_date_time = next_date_time;
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



    public String getPostimage() {
        return postimage;
    }

    public void setPostimage(String postimage) {
        this.postimage = postimage;
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
