package com.example.nhoxb.mysimpletwitter.data.remote;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

public class TwitterClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;

    public static final String REST_CONSUMER_KEY = "RA1wMFcUuDGy4vyu2TQGPtUaa";
    public static final String REST_CONSUMER_SECRET = "zHBdjJDLaVw4OLK0zAtDMEmFTBasjru12aC0v593BYZB4g9UrL";
    // Change this (here and in manifest)

    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, ApiEndPoint.BASE_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, ApiEndPoint.REST_CALLBACK_URL);
    }

    public void getInterestingList(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl(ApiEndPoint.TWITTER_GET_INTERESTING_LIST_URL);
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("format", "json");
        client.get(apiUrl, params, handler);
    }

    /* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
     * 	  i.e getApiUrl("statuses/home_timeline.json");
     * 2. Define the parameters to pass to the request (query or body)
     *    i.e RequestParams params = new RequestParams("foo", "bar");
     * 3. Define the request method and make a call to the client
     *    i.e client.get(apiUrl, params, handler);
     *    i.e client.post(apiUrl, params, handler);
     */

    public void verifyUser(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl(ApiEndPoint.TWITTER_VERIFY_USER_URL);
        RequestParams params = new RequestParams();
        params.put("include_entities", true);
        params.put("skip_status", true);
        params.put("include_email", true);
        getClient().get(apiUrl, params, handler);
    }

    public void getHomeTimeline(int page, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl(ApiEndPoint.TWITTER_GET_HOME_TIMELINE_URL);
        RequestParams params = new RequestParams();
        params.put("page", String.valueOf(page));
        params.put("since_id", "1");
        getClient().get(apiUrl, params, handler);
    }

    public void getMentionTimeline(int page, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl(ApiEndPoint.TWITTER_GET_MENTION_TIMELINE_URL);
        RequestParams params = new RequestParams();
        params.put("page", String.valueOf(page));
        params.put("since_id", "1");
        getClient().get(apiUrl, params, handler);
    }

    public void getUserRetweet(int count, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl(ApiEndPoint.TWITTER_GET_USER_RETWEET_URL);
        RequestParams params = new RequestParams();
        params.put("count", String.valueOf(count));
        getClient().get(apiUrl, params, handler);
    }

    public void getUserFavourite(int count, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl(ApiEndPoint.TWITTER_GET_USER_FAV_URL);
        RequestParams params = new RequestParams();
        params.put("count", String.valueOf(count));
        getClient().get(apiUrl, params, handler);
    }

    public void getUserTimeline(int count, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl(ApiEndPoint.TWITTER_GET_USER_TIMELINE_URL);
        RequestParams params = new RequestParams();
        params.put("count", String.valueOf(count));
        getClient().get(apiUrl, params, handler);
    }


    public void updateStatus(String body, String id, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl(ApiEndPoint.TWITTER_UPDATE_STATUS_URL);
        RequestParams params = new RequestParams();
        params.put("status", body);
        if (id != null && !id.isEmpty() && !id.equals(" ")) {
            params.put("in_reply_to_status_id", id);
        }
        getClient().post(apiUrl, params, handler);
    }

    public void retweetStatus(long statusId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl(ApiEndPoint.TWITTER_RETWEET_STATUS_URL + statusId + ".json");
        RequestParams params = new RequestParams();
        params.put("id", statusId);
        getClient().post(apiUrl, params, handler);
    }

    public void unRetweetStatus(long statusId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl(ApiEndPoint.TWITTER_UNRETWEET_STATUS_URL + statusId + ".json");
        RequestParams params = new RequestParams();
        params.put("id", statusId);
        getClient().post(apiUrl, params, handler);
    }

    public void favouriteStatus(long statusId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl(ApiEndPoint.TWITTER_FAV_STATUS_URL);
        RequestParams params = new RequestParams();
        params.put("id", statusId);
        getClient().post(apiUrl, params, handler);
    }

    public void unFavouriteStatus(long statusId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl(ApiEndPoint.TWITTER_UNFAV_STATUS_URL);
        RequestParams params = new RequestParams();
        params.put("id", statusId);
        getClient().post(apiUrl, params, handler);
    }
}