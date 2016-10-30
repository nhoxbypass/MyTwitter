package com.example.nhoxb.mysimpletwitter.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nhoxb on 10/30/2016.
 */
public class Media {
    public long getId() {
        return id;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public String getType() {
        return type;
    }

    @SerializedName("id")
    private long id;
    @SerializedName("media_url")
    private String mediaUrl;
    @SerializedName("type")
    private String type;
}
