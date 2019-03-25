package com.ahmedtaha.barakasky;

public class Friends {
    public String date, date_time;
    public Friends(){

    }

    public Friends(String date,  String date_time) {
        this.date = date;
        this.date_time = date_time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }
}
