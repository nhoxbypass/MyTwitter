package com.example.nhoxb.mysimpletwitter.ui.detail;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.nhoxb.mysimpletwitter.R;
import com.example.nhoxb.mysimpletwitter.TwitterApplication;
import com.example.nhoxb.mysimpletwitter.data.DataManager;
import com.example.nhoxb.mysimpletwitter.data.remote.model.Tweet;
import com.example.nhoxb.mysimpletwitter.ui.custom.TweetComposerDialogFragment;
import com.example.nhoxb.mysimpletwitter.ui.timeline.TimelineFragment;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class DetailActivity extends AppCompatActivity {

    public static final String TAG = DetailActivity.class.getSimpleName();
    @BindView(R.id.iv_detail_logo)
    ImageView logo;
    @BindView(R.id.tv_detail_name)
    TextView txtName;
    @BindView(R.id.tv_detail_screenname)
    TextView txtScreenName;
    @BindView(R.id.tv_detail_body)
    TextView txtBody;
    @BindView(R.id.iv_detail_media)
    ImageView media;
    @BindView(R.id.tv_detail_date)
    TextView txtDate;
    @BindView(R.id.tv_detail_retweet_count)
    TextView txtRetweetCount;
    @BindView(R.id.tv_detail_like_count)
    TextView txtLikeCount;
    @BindView(R.id.btn_detail_reply)
    ImageButton btnReply;
    @BindView(R.id.btn_detail_retweet)
    ImageButton btnRetweet;
    @BindView(R.id.btn_detail_like)
    ImageButton btnLike;
    @BindView(R.id.btn_detail_message)
    ImageButton btnMessage;
    @BindView(R.id.btn_detail_share)
    ImageButton btnShare;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private Tweet mCurrTweet;
    private ShareActionProvider mShare;
    private DataManager dataManager;
    private Gson mGson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        //Toolbar
        setSupportActionBar(mToolbar);
        Drawable backArrow = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back);
        backArrow.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        dataManager = TwitterApplication.getDataManager();
        mGson = new Gson();

        mToolbar.setTitle("Tweet");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mCurrTweet = getIntent().getExtras().getParcelable(TimelineFragment.KEY_TWEET_DETAIL);
        if (mCurrTweet != null) {
            updateUi(mCurrTweet);

        } else {
            Toast.makeText(DetailActivity.this, "Error happened!", Toast.LENGTH_SHORT).show();
        }

        setupEventListeners();
    }

    private void updateUi(Tweet currTweet) {
        Glide.with(this)
                .load(currTweet.getUser().getAvatarUrl())
                .apply(new RequestOptions().transform(new RoundedCornersTransformation(2, 4, RoundedCornersTransformation.CornerType.ALL)))
                .into(logo);
        txtName.setText(currTweet.getUser().getName());
        txtScreenName.setText("@" + currTweet.getUser().getScreenName());
        txtBody.setText(currTweet.getBody());
        txtDate.setText(currTweet.getCreatedAt());
        txtRetweetCount.setText(String.valueOf(currTweet.getRetweetCount()));
        txtLikeCount.setText(String.valueOf(currTweet.getFavouriteCount()));

        if (currTweet.getMedia() == null || currTweet.getMedia().isEmpty()) {
            media.setVisibility(View.GONE);
        } else {
            Glide.with(this)
                    .load(currTweet.getMedia().get(0).getMediaUrl())
                    .apply(new RequestOptions().transform(new RoundedCornersTransformation(4, 8, RoundedCornersTransformation.CornerType.ALL)))
                    .into(media);
            media.setVisibility(View.VISIBLE);
        }

        //Set icon
        setButtonIcons(currTweet);
    }

    private void setupEventListeners() {
        btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TweetComposerDialogFragment.showReplyComposer(DetailActivity.this, mCurrTweet.getUser().getScreenName(), mCurrTweet.getUid(), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Toast.makeText(DetailActivity.this, "Replied", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        Log.e(TAG, "unretweet failed");
                    }
                });
            }
        });

        btnRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mCurrTweet.isRetweeted()) {
                    dataManager.retweetStatus(mCurrTweet.getUid(), new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            Toast.makeText(btnRetweet.getContext(), "Đã tweet lại", Toast.LENGTH_SHORT).show();
                            btnRetweet.setImageDrawable(ContextCompat.getDrawable(DetailActivity.this, R.drawable.retweeted));
                            mCurrTweet.setRetweet(true);
                            txtRetweetCount.setText(String.valueOf(mCurrTweet.getRetweetCount()));
                            //mCurrTweet = mGson.fromJson(response.toString(), Tweet.class);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            Log.e(TAG, "retweet failed");
                        }
                    });
                } else {
                    dataManager.unRetweetStatus(mCurrTweet.getUid(), new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            btnRetweet.setImageDrawable(ContextCompat.getDrawable(DetailActivity.this, R.drawable.unretweet));
                            mCurrTweet.setRetweet(false);
                            txtRetweetCount.setText(String.valueOf(mCurrTweet.getRetweetCount()));
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            Log.e(TAG, "unretweet failed");
                        }
                    });
                }

            }
        });

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mCurrTweet.isFavourited()) {
                    dataManager.favouriteStatus(mCurrTweet.getUid(), new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            btnLike.setImageDrawable(ContextCompat.getDrawable(DetailActivity.this, R.drawable.heart));
                            mCurrTweet = mGson.fromJson(response.toString(), Tweet.class);
                            txtLikeCount.setText(String.valueOf(mCurrTweet.getFavouriteCount()));

                        }
                    });
                } else {
                    dataManager.unFavouriteStatus(mCurrTweet.getUid(), new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            btnLike.setImageDrawable(ContextCompat.getDrawable(DetailActivity.this, R.drawable.heart_outline));
                            mCurrTweet = mGson.fromJson(response.toString(), Tweet.class);
                            txtLikeCount.setText(String.valueOf(mCurrTweet.getFavouriteCount()));

                        }
                    });

                }
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void setButtonIcons(Tweet currTweet) {
        if (currTweet.isRetweeted()) {
            btnRetweet.setImageDrawable(ContextCompat.getDrawable(DetailActivity.this, R.drawable.retweeted));
        } else
            btnRetweet.setImageDrawable(ContextCompat.getDrawable(DetailActivity.this, R.drawable.unretweet));

        if (currTweet.isFavourited()) {
            btnLike.setImageDrawable(ContextCompat.getDrawable(DetailActivity.this, R.drawable.heart));
        } else
            btnLike.setImageDrawable(ContextCompat.getDrawable(DetailActivity.this, R.drawable.heart_outline));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);

        MenuItem item = menu.findItem(R.id.item_share);
        mShare = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");

        // pass in the URL currently being used by the WebView
        shareIntent.putExtra(Intent.EXTRA_TEXT, mCurrTweet.getUrl());

        mShare.setShareIntent(shareIntent);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
