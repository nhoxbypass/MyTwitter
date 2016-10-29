package com.example.nhoxb.mysimpletwitter.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nhoxb on 10/29/2016.
 */
public class Tweet {
    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public User getUser() {
        return user;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    @SerializedName("text")
    private String body;
    @SerializedName("id")
    private long uid;
    @SerializedName("user")
    private User user;
    @SerializedName("created_at")
    private String createdAt;
}
