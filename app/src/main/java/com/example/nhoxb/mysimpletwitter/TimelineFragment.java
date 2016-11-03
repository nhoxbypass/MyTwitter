package com.example.nhoxb.mysimpletwitter;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nhoxb.mysimpletwitter.activity.DetailActivity;
import com.example.nhoxb.mysimpletwitter.activity.TimelineActivity;
import com.example.nhoxb.mysimpletwitter.model.Tweet;
import com.example.nhoxb.mysimpletwitter.rest.TwitterApplication;
import com.example.nhoxb.mysimpletwitter.rest.TwitterClient;
import com.example.nhoxb.mysimpletwitter.ui.base.DividerItemDecoration;
import com.example.nhoxb.mysimpletwitter.ui.base.EndlessRecyclerViewScrollListener;
import com.example.nhoxb.mysimpletwitter.ui.timeline.TimelineAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;


/**
 * A simple {@link Fragment} subclass.
 */
public class TimelineFragment extends Fragment {

    public static final String KEY_TWEET_DETAIL = "tweet_detail";
    public static final int HOME_TIMELINE = 0;
    public static final int MENTIONS_TIMELINE = 1;
    public static final String KEY_FRAGMENT_TYPE = "fragment_type";
    RecyclerView mRecyclerView;
    private TimelineAdapter mTimelineAdapter;
    private LinearLayoutManager layoutManager;
    private TwitterClient mClient;
    private Context mContext;
    private Gson mGson;
    private int mType;

    public TimelineFragment() {
        // Required empty public constructor
    }

    public static TimelineFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt(KEY_FRAGMENT_TYPE, type);
        TimelineFragment fragment = new TimelineFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_timeline, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_list_tweet);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mClient = TwitterApplication.getRestClient();
        mGson = new Gson();

        mType = getArguments().getInt(KEY_FRAGMENT_TYPE,HOME_TIMELINE);


        //Recycler View, Adapter
        mTimelineAdapter = new TimelineAdapter(getContext());
        layoutManager = new LinearLayoutManager(getContext());
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST);
        mRecyclerView.setLayoutManager(layoutManager);
        mTimelineAdapter.setOnItemClickListener(new TimelineAdapter.OnItemTweetClickListener() {
            @Override
            public void onClick(Tweet tweet) {
                Bundle extras = new Bundle();
                extras.putParcelable(KEY_TWEET_DETAIL, tweet);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mTimelineAdapter);
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadMoreTweet(page);
            }
        });

        loadTweets(mType);
    }

    private void loadTweets(int mType) {
        switch (mType)
        {
            case HOME_TIMELINE:
                populateHomeTimeline();
                break;

            case MENTIONS_TIMELINE:
                populateMentionTimeline();
                break;

            default:
                Log.e("APP TWEET", "Error loading tweets");
                break;
        }
    }


    private void populateHomeTimeline()
    {
        mClient.getHomeTimeline(new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Log.v("APP","Load timeline success.");
                List<Tweet> mTweetList = mGson.fromJson(response.toString(), new TypeToken<List<Tweet>>(){}.getType() );
                mTimelineAdapter.setTweet(mTweetList);
                EventBus.getDefault().post(new FragmentMessageEvent(true));

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.v("APP","Load timeline failed.");
                EventBus.getDefault().post(new FragmentMessageEvent(true));
            }
        });
    }

    private void populateMentionTimeline()
    {
        mClient.getMentionTimeline(25, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.v("APP","Load mention success.");
                List<Tweet> mTweetList = mGson.fromJson(response.toString(), new TypeToken<List<Tweet>>(){}.getType() );
                mTimelineAdapter.setTweet(mTweetList);
                EventBus.getDefault().post(new FragmentMessageEvent(true));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.v("APP","Load mention failed.");
                EventBus.getDefault().post(new FragmentMessageEvent(true));
            }
        });
    }

    private void loadMoreTweet(final int page)
    {
        mClient.getHomeTimeline(page, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Log.v("APP","Load success. Page: " + page);
                List<Tweet> mTweetList = mGson.fromJson(response.toString(), new TypeToken<List<Tweet>>(){}.getType() );
                mTimelineAdapter.addTweet(mTweetList);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e("APP", "Load more failed");
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onActivityMessageEvent(TimelineActivity.ActivityMessageEvent event)
    {
        if (event.isPullToRefresh)
        {
            populateHomeTimeline();
        }

        if (event.isAddTweetToTop)
        {
            mTimelineAdapter.addTweetOnTop(event.getTweet());
            mRecyclerView.smoothScrollToPosition(0);
        }
    }

    public static class FragmentMessageEvent
    {
        public FragmentMessageEvent(boolean isFinishRefresh)
        {
            this.isFinishRefresh = isFinishRefresh;
        }

        public boolean isFinishRefresh() {
            return isFinishRefresh;
        }

        boolean isFinishRefresh = false;
    }


}
