package com.example.nhoxb.mysimpletwitter.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nhoxb on 10/30/2016.
 */
public class Profile
{
    @SerializedName("id")
    private long id;
    @SerializedName("name")
    private String name;
    @SerializedName("screen_name")
    private String screenName;
    @SerializedName("description")
    private String description;
    @SerializedName("url")
    private String url;
    @SerializedName("profile_image_url")
    private String avatarUrl;
    @SerializedName("profile_background_image_url")
    private String coverUrl;
}
