package com.example.nhoxb.mysimpletwitter.activity;

import android.app.DialogFragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.nhoxb.mysimpletwitter.R;
import com.example.nhoxb.mysimpletwitter.ui.timeline.TimelineFragment;
import com.example.nhoxb.mysimpletwitter.model.Tweet;
import com.example.nhoxb.mysimpletwitter.model.User;
import com.example.nhoxb.mysimpletwitter.rest.TwitterApplication;
import com.example.nhoxb.mysimpletwitter.rest.TwitterClient;
import com.example.nhoxb.mysimpletwitter.ui.timeline.TimelinePagerAdapter;
import com.example.nhoxb.mysimpletwitter.ui.base.TweetComposerDialogFragment;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class TimelineActivity extends AppCompatActivity {

    public static final String KEY_PROFILE = "profile";
    private TwitterClient mClient;
    private Gson mGson;
    private TimelinePagerAdapter pagerAdapter;
    //@BindView(R.id.rv_list_tweet) RecyclerView mRecyclerView;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.swipe_refresh) SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.fab_compose) FloatingActionButton mFABCompose;
    @BindView(R.id.tablayout) TabLayout mTabLayout;
    @BindView(R.id.viewpager) ViewPager mViewPager;
    @BindView(R.id.nav_view) NavigationView mNavigationView;
    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    View mHeaderLayout;
    LinearLayout mNavHeaderContainer;
    ImageView mNavHeaderAvatar;
    TextView mNavHeaderName;
    TextView mNavHeaderScreenName;

    private ActionBarDrawerToggle mDrawerToggle;
    private SearchView mSearchView;
    private User mUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        ButterKnife.bind(this);



        //Toolbar
        setSupportActionBar(mToolbar);
        mDrawerToggle = setupDrawerToggle();
        mHeaderLayout = mNavigationView.getHeaderView(0);
        mNavHeaderContainer = (LinearLayout) mHeaderLayout.findViewById(R.id.nav_header_container);
        mNavHeaderAvatar = (ImageView) mHeaderLayout.findViewById(R.id.nav_header_avatar);
        mNavHeaderName = (TextView) mHeaderLayout.findViewById(R.id.nav_header_name);
        mNavHeaderScreenName = (TextView)mHeaderLayout.findViewById(R.id.nav_header_screenname);



        // Tie DrawerLayout events to the ActionBarToggle
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        //
        pagerAdapter = new TimelinePagerAdapter(getSupportFragmentManager(), TimelineActivity.this);
        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mSwipeRefresh.setEnabled(false);
                switch(motionEvent.getAction())
                {
                    case MotionEvent.ACTION_UP:
                        mSwipeRefresh.setEnabled(true);
                        break;
                }

                //True if the listener has consumed the event, false otherwise.
                //we just catch it and do sth, not consume it
                return false;
            }
        });


        //Pull to refresh
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                EventBus.getDefault().post(new ActivityMessageEvent(null, ActivityMessageEvent.PULL_TO_REFRESH));
            }
        });
        mSwipeRefresh.setColorSchemeResources(R.color.googleRed, R.color.googleGreen, R.color.googleBlue, R.color.googleYellow);
        mSwipeRefresh.setProgressViewOffset(true,0,40);
        mSwipeRefresh.setRefreshing(true);




        mFABCompose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showComposer();
            }
        });

        mClient = TwitterApplication.getRestClient();
        mGson = new Gson();

        //get user
        getUserCredential();
        mNavHeaderAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TimelineActivity.this, ProfileActivity.class);
                Bundle extras = new Bundle();
                extras.putParcelable(TimelineActivity.KEY_PROFILE, mUser);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

    }

    private void getUserCredential() {
        mClient.verifyUser(new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                mUser = mGson.fromJson(response.toString(), User.class);
                Log.v("APP TWEET", "Get user credential success");

                Glide.with(TimelineActivity.this)
                        .load(mUser.getCoverUrl())
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                Drawable drawable = new BitmapDrawable(TimelineActivity.this.getResources(),resource);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                                {
                                    mNavHeaderContainer.setBackground(drawable);
                                }
                            }
                        });

                mNavHeaderName.setText(mUser.getName());
                mNavHeaderScreenName.setText(mUser.getScreenName());
                Glide.with(TimelineActivity.this)
                        .load(mUser.getAvatarUrl())
                        .bitmapTransform(new CropCircleTransformation(TimelineActivity.this))
                        .into(mNavHeaderAvatar);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.v("APP TWEET", "Get user credential failed: " + responseString);
            }
        });
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar ,R.string.drawer_open, R.string.drawer_close);
    }


    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFragmentMessageEvent(TimelineFragment.FragmentMessageEvent event)
    {
        if(event.isFinishRefresh())
            mSwipeRefresh.setRefreshing(false);
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
        if (mDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        switch (item.getItemId())
        {
            case R.id.action_compose:
                showComposer();
                return true;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void showComposer() {
        FragmentManager fm = getSupportFragmentManager();
        TweetComposerDialogFragment composerDialogFragment = TweetComposerDialogFragment.newInstance("");
        composerDialogFragment.setStyle(DialogFragment.STYLE_NORMAL,R.style.Dialog_FullScreen);
        composerDialogFragment.show(getFragmentManager(),"fragment_compose_tweet");
        composerDialogFragment.setOnTweetedListener(new TweetComposerDialogFragment.TweetComposerDialogListener() {
            @Override
            public void onTweeted(String body) {
                mClient.updateStatus(body, "", new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Tweet tweet = mGson.fromJson(response.toString(),Tweet.class);
                        EventBus.getDefault().post(new ActivityMessageEvent(tweet, ActivityMessageEvent.ADD_TWEET_TO_TOP));
                        //.mRecyclerView.scrollToPosition(0);
                        Log.v("APP", "Tweeted timeline");

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        Log.e("APP", "Failed timeline");
                    }
                });
            }
        });
    }

    public static class ActivityMessageEvent
    {
        public static final int PULL_TO_REFRESH = 0;
        public static final int ADD_TWEET_TO_TOP = 1;
        private Tweet mTweet;

        public Tweet getTweet() {
            return mTweet;
        }

        public ActivityMessageEvent(Tweet tweet, int type)
        {
            mTweet = tweet;
            if (mTweet != null && type == ADD_TWEET_TO_TOP)
                isAddTweetToTop = true;
            else
                isAddTweetToTop = false;

            if (type == PULL_TO_REFRESH)
                isPullToRefresh = true;
            else
                isPullToRefresh = false;
        }

        public boolean isPullToRefresh = false;
        public boolean isAddTweetToTop = false;
    }

}
