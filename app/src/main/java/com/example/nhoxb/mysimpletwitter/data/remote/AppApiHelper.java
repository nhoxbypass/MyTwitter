package com.example.nhoxb.mysimpletwitter.data.remote;

import android.content.Context;

import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * Created by tom on 7/2/18.
 */
public class AppApiHelper implements ApiHelper {
    private TwitterClient twitterClient;

    public AppApiHelper(Context context) {
        super();
        twitterClient = (TwitterClient) TwitterClient.getInstance(TwitterClient.class, context);
    }

    @Override
    public void getInterestingList(AsyncHttpResponseHandler handler) {
        twitterClient.getInterestingList(handler);
    }

    @Override
    public void verifyUser(AsyncHttpResponseHandler handler) {
        twitterClient.verifyUser(handler);
    }

    @Override
    public void getHomeTimeline(int page, AsyncHttpResponseHandler handler) {
        twitterClient.getHomeTimeline(page, handler);
    }

    @Override
    public void getMentionTimeline(int page, AsyncHttpResponseHandler handler) {
        twitterClient.getMentionTimeline(page, handler);
    }

    @Override
    public void getUserRetweet(int count, AsyncHttpResponseHandler handler) {
        twitterClient.getUserRetweet(count, handler);
    }

    @Override
    public void getUserFavourite(int count, AsyncHttpResponseHandler handler) {
        twitterClient.getUserFavourite(count, handler);
    }

    @Override
    public void getUserTimeline(int count, AsyncHttpResponseHandler handler) {
        twitterClient.getUserTimeline(count, handler);
    }

    @Override
    public void updateStatus(String body, String id, AsyncHttpResponseHandler handler) {
        twitterClient.updateStatus(body, id, handler);
    }

    @Override
    public void retweetStatus(long statusId, AsyncHttpResponseHandler handler) {
        twitterClient.retweetStatus(statusId, handler);
    }

    @Override
    public void unRetweetStatus(long statusId, AsyncHttpResponseHandler handler) {
        twitterClient.unRetweetStatus(statusId, handler);
    }

    @Override
    public void favouriteStatus(long statusId, AsyncHttpResponseHandler handler) {
        twitterClient.favouriteStatus(statusId, handler);
    }

    @Override
    public void unFavouriteStatus(long statusId, AsyncHttpResponseHandler handler) {
        twitterClient.unFavouriteStatus(statusId, handler);
    }
}
