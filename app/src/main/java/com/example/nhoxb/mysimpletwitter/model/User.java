package com.example.nhoxb.mysimpletwitter.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nhoxb on 10/29/2016.
 */
public class User implements Parcelable{
    protected User(Parcel in) {
        id = in.readString();
        name = in.readString();
        avatarUrl = in.readString();
        coverUrl = in.readString();
        screenName = in.readString();
        favouriteCount = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

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

    public int getFavouriteCount() {
        return favouriteCount;
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
    @SerializedName("favourites_count")
    private int favouriteCount;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(avatarUrl);
        parcel.writeString(coverUrl);
        parcel.writeString(screenName);
        parcel.writeInt(favouriteCount);
    }
}
