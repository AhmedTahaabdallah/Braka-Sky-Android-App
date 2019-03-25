package com.ahmedtaha.barakasky;

public class Replays {
    public String uid, replay,date,time, date_time;
    public Replays(){

    }

    public Replays(String uid, String replay, String date, String time, String date_time) {
        this.uid = uid;
        this.replay = replay;
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

    public String getReplay() {
        return replay;
    }

    public void setReplay(String replay) {
        this.replay = replay;
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
