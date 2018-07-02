package com.example.nhoxb.mysimpletwitter.data.remote.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by nhoxb on 10/29/2016.
 */
public class Tweet implements Parcelable {
    public static final Creator<Tweet> CREATOR = new Creator<Tweet>() {
        @Override
        public Tweet createFromParcel(Parcel in) {
            return new Tweet(in);
        }

        @Override
        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };
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
    @SerializedName("favorite_count")
    private int favouriteCount;
    @SerializedName("entities")
    private JsonObject entities;
    @SerializedName("retweeted")
    private boolean retweeted;
    @SerializedName("favorited")
    private boolean favourited;
    //
    private List<Media> mediaList;
    private String url;
    protected Tweet(Parcel in) {
        Gson gson = new Gson();

        body = in.readString();
        uid = in.readLong();
        user = in.readParcelable(User.class.getClassLoader());
        createdAt = in.readString();
        retweetCount = in.readInt();
        favouriteCount = in.readInt();
        retweeted = in.readByte() != 0;
        favourited = in.readByte() != 0;
        entities = gson.fromJson(in.readString(), JsonObject.class);
        mediaList = in.createTypedArrayList(Media.CREATOR);
    }

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

    public int getFavouriteCount() {
        return favouriteCount;
    }

    public List<Media> getMedia() {
        Gson gson = new Gson();
        mediaList = gson.fromJson(entities.getAsJsonArray("media"), new TypeToken<List<Media>>() {
        }.getType());
        return mediaList;
    }

    public boolean isRetweeted() {
        return retweeted;
    }

    public boolean isFavourited() {
        return favourited;
    }

    public String getUrl() {
        JsonArray array = entities.getAsJsonArray("urls");

        if (array != null && !array.isJsonNull() && array.size() > 0)
            return entities.getAsJsonArray("urls").get(0).getAsJsonObject().get("url").getAsString();
        else
            return "google.com";
    }

    public void setRetweet(boolean isRetweeted) {
        if (isRetweeted) {
            retweetCount++;
            retweeted = true;
        } else {
            retweetCount--;
            retweeted = false;
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(body);
        parcel.writeLong(uid);
        parcel.writeParcelable(user, i);
        parcel.writeString(createdAt);
        parcel.writeInt(retweetCount);
        parcel.writeInt(favouriteCount);
        parcel.writeByte((byte) (retweeted ? 1 : 0));
        parcel.writeByte((byte) (favourited ? 1 : 0));
        parcel.writeString(entities.toString());
        parcel.writeTypedList(mediaList);
    }
}
