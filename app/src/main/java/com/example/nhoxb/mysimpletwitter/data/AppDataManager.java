package com.example.nhoxb.mysimpletwitter.data;

import android.content.Context;

import com.example.nhoxb.mysimpletwitter.data.remote.ApiHelper;
import com.example.nhoxb.mysimpletwitter.data.remote.AppApiHelper;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * Created by tom on 7/2/18.
 */
public class AppDataManager implements DataManager {

    ApiHelper apiHelper;

    public AppDataManager(Context context) {
        super();
        apiHelper = new AppApiHelper(context);
    }

    @Override
    public void getInterestingList(AsyncHttpResponseHandler handler) {
        apiHelper.getInterestingList(handler);
    }

    @Override
    public void verifyUser(AsyncHttpResponseHandler handler) {
        apiHelper.verifyUser(handler);
    }

    @Override
    public void getHomeTimeline(int page, AsyncHttpResponseHandler handler) {
        apiHelper.getHomeTimeline(page, handler);
    }

    @Override
    public void getMentionTimeline(int page, AsyncHttpResponseHandler handler) {
        apiHelper.getMentionTimeline(page, handler);
    }

    @Override
    public void getUserRetweet(int count, AsyncHttpResponseHandler handler) {
        apiHelper.getUserRetweet(count, handler);
    }

    @Override
    public void getUserFavourite(int count, AsyncHttpResponseHandler handler) {
        apiHelper.getUserFavourite(count, handler);
    }

    @Override
    public void getUserTimeline(int count, AsyncHttpResponseHandler handler) {
        apiHelper.getUserTimeline(count, handler);
    }

    @Override
    public void updateStatus(String body, String id, AsyncHttpResponseHandler handler) {
        apiHelper.updateStatus(body, id, handler);
    }

    @Override
    public void retweetStatus(long statusId, AsyncHttpResponseHandler handler) {
        apiHelper.retweetStatus(statusId, handler);
    }

    @Override
    public void unRetweetStatus(long statusId, AsyncHttpResponseHandler handler) {
        apiHelper.unRetweetStatus(statusId, handler);
    }

    @Override
    public void favouriteStatus(long statusId, AsyncHttpResponseHandler handler) {
        apiHelper.favouriteStatus(statusId, handler);
    }

    @Override
    public void unFavouriteStatus(long statusId, AsyncHttpResponseHandler handler) {
        apiHelper.unFavouriteStatus(statusId, handler);
    }
}
