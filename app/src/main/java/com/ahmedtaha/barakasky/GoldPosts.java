package com.ahmedtaha.barakasky;

public class GoldPosts {
    public String post_key, post_uid, count_gold;

    public GoldPosts() {
    }

    public GoldPosts(String post_key, String post_uid, String count_gold) {
        this.post_key = post_key;
        this.post_uid = post_uid;
        this.count_gold = count_gold;
    }

    public String getPost_key() {
        return post_key;
    }

    public void setPost_key(String post_key) {
        this.post_key = post_key;
    }

    public String getPost_uid() {
        return post_uid;
    }

    public void setPost_uid(String post_uid) {
        this.post_uid = post_uid;
    }

    public String getCount_gold() {
        return count_gold;
    }

    public void setCount_gold(String count_gold) {
        this.count_gold = count_gold;
    }
}
