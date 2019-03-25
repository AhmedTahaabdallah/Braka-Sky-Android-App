package com.ahmedtaha.barakasky;

public class SentFriendsRequests {
    public String request_type;

    public SentFriendsRequests() {

    }
    public SentFriendsRequests(String request_type) {
        this.request_type = request_type;
    }

    public String getRequest_type() {
        return request_type;
    }

    public void setRequest_type(String request_type) {
        this.request_type = request_type;
    }
}
