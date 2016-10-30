package com.example.nhoxb.mysimpletwitter.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.util.List;

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

    public int getRetweetCount() {
        return retweetCount;
    }

    public List<Media> getMedia()
    {
        Gson gson = new Gson();
        List<Media> mediaList = gson.fromJson(entities.getAsJsonArray("media"), new TypeToken<List<Media>>(){}.getType());
        return mediaList;
    }

    @SerializedName("text")
    private String body;
    @SerializedName("id")
    private long uid;
    @SerializedName("user")
    private User user;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("retweet_count")
    private int retweetCount;
    @SerializedName("entities")
    private JsonObject entities;
}
