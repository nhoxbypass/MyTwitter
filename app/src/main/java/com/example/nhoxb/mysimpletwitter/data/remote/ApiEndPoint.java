package com.example.nhoxb.mysimpletwitter.data.remote;

/**
 * Created by tom on 7/2/18.
 */
public final class ApiEndPoint {
    public static final String BASE_URL = "https://api.twitter.com/1.1/";
    public static final String REST_CALLBACK_URL = "oauth://mysimpletweet";
    public static final String TWITTER_VERIFY_USER_URL = "account/verify_credentials.json";
    public static final String TWITTER_GET_HOME_TIMELINE_URL = "statuses/home_timeline.json";
    public static final String TWITTER_GET_MENTION_TIMELINE_URL = "statuses/mentions_timeline.json";
    public static final String TWITTER_GET_USER_RETWEET_URL = "statuses/retweets_of_me.json";
    public static final String TWITTER_GET_USER_FAV_URL = "favorites/list.json";
    public static final String TWITTER_GET_USER_TIMELINE_URL = "statuses/user_timeline.json";
    public static final String TWITTER_UPDATE_STATUS_URL = "statuses/update.json";
    public static final String TWITTER_RETWEET_STATUS_URL = "statuses/retweet/";
    public static final String TWITTER_UNRETWEET_STATUS_URL = "statuses/unretweet/";
    public static final String TWITTER_FAV_STATUS_URL = "favorites/create.json";
    public static final String TWITTER_UNFAV_STATUS_URL = "favorites/destroy.json";
    public static final String TWITTER_GET_INTERESTING_LIST_URL = "?nojsoncallback=1&method=flickr.interestingness.getList";
}
