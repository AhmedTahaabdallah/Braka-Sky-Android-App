package com.ahmedtaha.barakasky;

public class FindFriends {
    public String fullname,profileimage,status;

    public FindFriends(){

    }
    public FindFriends(String fullname, String profileimage, String status) {
        this.fullname = fullname;
        this.profileimage = profileimage;
        this.status = status;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
