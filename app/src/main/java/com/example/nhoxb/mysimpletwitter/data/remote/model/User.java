package com.example.nhoxb.mysimpletwitter.data.remote.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nhoxb on 10/29/2016.
 */
public class User implements Parcelable {

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
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("screen_name")
    private String screenName;
    @SerializedName("followers_count")
    private int followerCount;
    @SerializedName("friends_count")
    private int friendCount;
    @SerializedName("favourites_count")
    private int favouriteCount;
    @SerializedName("statuses_count")
    private int statusCount;
    @SerializedName("profile_image_url")
    private String avatarUrl;
    @SerializedName("profile_background_image_url")
    private String coverUrl;
    protected User(Parcel in) {
        id = in.readString();
        name = in.readString();
        screenName = in.readString();
        followerCount = in.readInt();
        friendCount = in.readInt();
        favouriteCount = in.readInt();
        statusCount = in.readInt();
        avatarUrl = in.readString();
        coverUrl = in.readString();
    }

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

    public int getFollowerCount() {
        return followerCount;
    }

    public int getFriendCount() {
        return friendCount;
    }

    public int getStatusCount() {
        return statusCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(screenName);
        parcel.writeInt(followerCount);
        parcel.writeInt(friendCount);
        parcel.writeInt(favouriteCount);
        parcel.writeInt(statusCount);
        parcel.writeString(avatarUrl);
        parcel.writeString(coverUrl);
    }
}
