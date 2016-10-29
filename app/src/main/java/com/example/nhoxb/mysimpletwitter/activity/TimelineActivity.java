package com.example.nhoxb.mysimpletwitter.activity;

import android.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.nhoxb.mysimpletwitter.R;
import com.example.nhoxb.mysimpletwitter.model.Tweet;
import com.example.nhoxb.mysimpletwitter.rest.TwitterApplication;
import com.example.nhoxb.mysimpletwitter.rest.TwitterClient;
import com.example.nhoxb.mysimpletwitter.ui.base.DividerItemDecoration;
import com.example.nhoxb.mysimpletwitter.ui.base.EndlessRecyclerViewScrollListener;
import com.example.nhoxb.mysimpletwitter.ui.base.TweetComposerDialogFragment;
import com.example.nhoxb.mysimpletwitter.ui.timeline.TimelineAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    private TwitterClient mClient;
    private Gson gson;
    private RecyclerView mRecyclerView;
    private TimelineAdapter mTimelineAdapter;
    private LinearLayoutManager layoutManager;
    private SearchView mSearchView;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list_tweet);
        mTimelineAdapter = new TimelineAdapter();
        layoutManager = new LinearLayoutManager(TimelineActivity.this);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mTimelineAdapter);
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadMoreTweet(page);
            }
        });

        mClient = TwitterApplication.getRestClient();
        gson = new Gson();

        populateTimeline();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline,menu);
        MenuItem itemSearchView = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(itemSearchView);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                mSearchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_compose:
                showComposer();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void populateTimeline()
    {
        mClient.getHomeTimeline(new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Log.v("APP","Load success.");
                List<Tweet> mTweetList = gson.fromJson(response.toString(), new TypeToken<List<Tweet>>(){}.getType() );
                mTimelineAdapter.setTweet(mTweetList);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
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
                List<Tweet> mTweetList = gson.fromJson(response.toString(), new TypeToken<List<Tweet>>(){}.getType() );
                mTimelineAdapter.addTweet(mTweetList);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void showComposer() {
        FragmentManager fm = getSupportFragmentManager();
        TweetComposerDialogFragment composerDialogFragment = TweetComposerDialogFragment.newInstance();
        composerDialogFragment.setStyle(DialogFragment.STYLE_NORMAL,R.style.Dialog_FullScreen);
        composerDialogFragment.show(getFragmentManager(),"fragment_compose_tweet");
        composerDialogFragment.setOnTweetedListener(new TweetComposerDialogFragment.TweetComposerDialogListener() {
            @Override
            public void onTweeted(String body)
            {
                mClient.updateStatus(body, new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Tweet tweet = gson.fromJson(response.toString(),Tweet.class);
                        mTimelineAdapter.addTweet(tweet);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                    }
                });
            }
        });
    }

}
