package com.example.nhoxb.mysimpletwitter.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nhoxb.mysimpletwitter.R;
import com.example.nhoxb.mysimpletwitter.model.Tweet;
import com.example.nhoxb.mysimpletwitter.rest.TwitterApplication;
import com.example.nhoxb.mysimpletwitter.ui.base.TweetComposerDialogFragment;
import com.example.nhoxb.mysimpletwitter.ui.timeline.TimelineAdapter;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;
import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class DetailActivity extends AppCompatActivity {

    private Tweet mCurrTweet;
    @BindView(R.id.iv_detail_logo) ImageView logo;
    @BindView(R.id.tv_detail_name) TextView txtName;
    @BindView(R.id.tv_detail_screenname)   TextView txtScreenName;
    @BindView(R.id.tv_detail_body)      TextView txtBody;
    @BindView(R.id.iv_detail_media)    ImageView media;
    @BindView(R.id.tv_detail_date)      TextView txtDate;
    @BindView(R.id.tv_detail_retweet_count) TextView txtRetweetCount;
    @BindView(R.id.tv_detail_like_count)    TextView txtLikeCount;
    @BindView(R.id.btn_detail_reply) ImageButton btnReply;
    @BindView(R.id.btn_detail_retweet) ImageButton btnRetweet;
    @BindView(R.id.btn_detail_like) ImageButton btnLike;
    @BindView(R.id.btn_detail_message) ImageButton btnMessage;
    @BindView(R.id.btn_detail_share) ImageButton btnShare;
    @BindView(R.id.toolbar)  Toolbar mToolbar;
    ShareActionProvider mShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        //Toolbar
        setSupportActionBar(mToolbar);
        Drawable backArrow = ContextCompat.getDrawable(this,R.drawable.ic_arrow_back);
        backArrow.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        mToolbar.setTitle("Tweet");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mCurrTweet = getIntent().getExtras().getParcelable(TimelineActivity.KEY_TWEET_DETAIL);

        if (mCurrTweet != null)
        {
            Glide.with(this)
                    .load(mCurrTweet.getUser().getAvatarUrl())
                    .bitmapTransform(new RoundedCornersTransformation(this, 4, 2))
                    .into(logo);
            txtName.setText(mCurrTweet.getUser().getName());
            txtScreenName.setText("@" + mCurrTweet.getUser().getScreenName());
            txtBody.setText(mCurrTweet.getBody());
            txtDate.setText(mCurrTweet.getCreatedAt());
            txtRetweetCount.setText(String.valueOf(mCurrTweet.getRetweetCount()));
            txtLikeCount.setText(String.valueOf(mCurrTweet.getFavouriteCount()));

            if (mCurrTweet.getMedia() == null || mCurrTweet.getMedia().isEmpty()) {
                media.setVisibility(View.GONE);
            }
            else
            {
                Glide.with(this)
                        .load(mCurrTweet.getMedia().get(0).getMediaUrl())
                        .bitmapTransform(new RoundedCornersTransformation(this, 8, 4))
                        .into(media);
                media.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            Toast.makeText(DetailActivity.this, "Error happened!", Toast.LENGTH_SHORT).show();
        }

        //Hook
        btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TweetComposerDialogFragment.showReplyComposer(DetailActivity.this, mCurrTweet.getUser().getScreenName(), mCurrTweet.getUid(), new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Toast.makeText(DetailActivity.this, "Replied", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        Log.e("APP", "retweet failed");
                    }
                });
            }
        });

        btnRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TwitterApplication.getRestClient().retweetStatus(mCurrTweet.getUid(), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Toast.makeText(btnRetweet.getContext(),"Retweeted", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        Log.e("APP", "retweet failed");
                    }
                });
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail,menu);

        MenuItem item = menu.findItem(R.id.item_share);
        mShare = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");

        // pass in the URL currently being used by the WebView
        shareIntent.putExtra(Intent.EXTRA_TEXT,mCurrTweet.getUrl());

        mShare.setShareIntent(shareIntent);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
