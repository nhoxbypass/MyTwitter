package com.example.nhoxb.mysimpletwitter.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nhoxb on 10/29/2016.
 */
public class User {
    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public String getScreenName() {
        return screenName;
    }



    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("profile_image_url")
    private String avatarUrl;
    @SerializedName("profile_background_image_url")
    private String coverUrl;
    @SerializedName("screen_name")
    private String screenName;
}
