package com.example.nhoxb.mysimpletwitter.data.remote;

import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * Created by tom on 7/2/18.
 */
public interface ApiHelper {
    void getInterestingList(AsyncHttpResponseHandler handler);

    void verifyUser(AsyncHttpResponseHandler handler);

    void getHomeTimeline(int page, AsyncHttpResponseHandler handler);

    void getMentionTimeline(int page, AsyncHttpResponseHandler handler);

    void getUserRetweet(int count, AsyncHttpResponseHandler handler);

    void getUserFavourite(int count, AsyncHttpResponseHandler handler);

    void getUserTimeline(int count, AsyncHttpResponseHandler handler);

    void updateStatus(String body, String id, AsyncHttpResponseHandler handler);

    void retweetStatus(long statusId, AsyncHttpResponseHandler handler);

    void unRetweetStatus(long statusId, AsyncHttpResponseHandler handler);

    void favouriteStatus(long statusId, AsyncHttpResponseHandler handler);

    void unFavouriteStatus(long statusId, AsyncHttpResponseHandler handler);
}
