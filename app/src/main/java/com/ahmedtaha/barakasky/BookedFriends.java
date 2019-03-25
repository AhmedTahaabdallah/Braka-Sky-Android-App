package com.ahmedtaha.barakasky;

public class BookedFriends {
    private String date_time;
    public BookedFriends(){

    }

    public BookedFriends(String date_time) {
        this.date_time = date_time;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }
}
