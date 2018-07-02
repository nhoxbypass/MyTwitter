package com.example.nhoxb.mysimpletwitter.data.remote.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nhoxb on 10/30/2016.
 */
public class Media implements Parcelable {
    public static final Creator<Media> CREATOR = new Creator<Media>() {
        @Override
        public Media createFromParcel(Parcel in) {
            return new Media(in);
        }

        @Override
        public Media[] newArray(int size) {
            return new Media[size];
        }
    };
    @SerializedName("id")
    private long id;
    @SerializedName("media_url")
    private String mediaUrl;
    @SerializedName("type")
    private String type;

    protected Media(Parcel in) {
        id = in.readLong();
        mediaUrl = in.readString();
        type = in.readString();
    }

    public long getId() {
        return id;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public String getType() {
        return type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(mediaUrl);
        parcel.writeString(type);
    }
}
