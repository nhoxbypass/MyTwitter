package com.example.nhoxb.mysimpletwitter.ui.timeline;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nhoxb.mysimpletwitter.R;
import com.example.nhoxb.mysimpletwitter.activity.TimelineActivity;
import com.example.nhoxb.mysimpletwitter.model.Media;
import com.example.nhoxb.mysimpletwitter.model.Tweet;
import com.example.nhoxb.mysimpletwitter.rest.TwitterApplication;
import com.example.nhoxb.mysimpletwitter.rest.TwitterClient;
import com.example.nhoxb.mysimpletwitter.ui.base.TweetComposerDialogFragment;
import com.example.nhoxb.mysimpletwitter.utils.Utils;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by nhoxb on 10/29/2016.
 */
public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.TimelineViewHolder>
{
    List<Tweet> mTweetList;
    Context mContext;
    private OnItemTweetClickListener mListener;
    private TwitterClient mClient;
    private Gson mGson;

    public TimelineAdapter(Context context) {
        mTweetList = new ArrayList<>();
        mContext = context;
        mClient = TwitterApplication.getRestClient();
        mGson = new Gson();
    }

    public interface OnItemTweetClickListener
    {
        public void onClick(Tweet tweet);
    }

    public void setOnItemClickListener(OnItemTweetClickListener listener)
    {
        mListener = listener;
    }

    public void setTweet( List<Tweet> tweetList)
    {
        mTweetList.clear();
        mTweetList.addAll(tweetList);
        notifyDataSetChanged();
    }

    public void addTweet(List<Tweet> tweetList)
    {
        int position = mTweetList.size();
        mTweetList.addAll(tweetList);
        notifyItemRangeInserted(position, tweetList.size());
    }

    public void addTweetOnTop(Tweet tweet)
    {
        mTweetList.add(0,tweet);
       notifyItemInserted(0);
    }

    public void clear() {
        mTweetList.clear();
        notifyDataSetChanged();
    }


    @Override
    public TimelineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_tweet, parent, false);

        return new TimelineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TimelineViewHolder holder, int position) {
        Tweet tweet = mTweetList.get(position);

        holder.txtBody.setText(tweet.getBody());
        holder.txtName.setText(tweet.getUser().getName());
        holder.txtScreenName.setText("@" + tweet.getUser().getScreenName());
        holder.txtTime.setText(Utils.getRelativeTimeAgo(tweet.getCreatedAt()));
        holder.txtRetweet.setText(String.valueOf(tweet.getRetweetCount()));
        holder.txtLike.setText(String.valueOf(tweet.getFavouriteCount()));

        Glide.with(mContext)
                .load(tweet.getUser().getAvatarUrl())
                .bitmapTransform(new RoundedCornersTransformation(mContext,8,4))
                .into(holder.avatar);

        List<Media> mediaList = tweet.getMedia();
        if (mediaList != null && mediaList.size() > 0)
        {
            holder.media.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(mediaList.get(0).getMediaUrl())
                    .bitmapTransform(new RoundedCornersTransformation(mContext,4,2))
                    .into(holder.media);
        }

        setupIcon(tweet, holder);


    }

    private void setupIcon(Tweet tweet, TimelineViewHolder viewholder)
    {
        if (tweet.isRetweeted())
            viewholder.btnRetweet.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.retweeted));
        else
            viewholder.btnRetweet.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.unretweet));

        if (tweet.isFavourited())
            viewholder.btnLike.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.heart));
        else
            viewholder.btnLike.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.heart_outline));

    }

    @Override
    public int getItemCount() {
        return mTweetList.size();
    }

    class TimelineViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.iv_avatar) ImageView avatar;
        @BindView(R.id.tv_username) TextView txtName;
        @BindView(R.id.tv_user_screenname) TextView txtScreenName;
        @BindView(R.id.tv_tweet_body) TextView txtBody;
        @BindView(R.id.tv_time) TextView txtTime;
        @BindView(R.id.btn_item_reply) ImageButton  btnReply;
        @BindView(R.id.btn_item_retweet) ImageButton btnRetweet;
        @BindView(R.id.btn_item_like) ImageButton btnLike;
        @BindView(R.id.tv_count_retweet) TextView txtRetweet;
        @BindView(R.id.tv_count_like) TextView txtLike;
        @BindView(R.id.iv_media)    ImageView media;
        @BindView(R.id.item_tweet_container) RelativeLayout container;

        private int position;

        public TimelineViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    position = getAdapterPosition(); // gets item position
                    if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                        Tweet tweet = mTweetList.get(position);
                        // We can access the data within the views
                        mListener.onClick(tweet);
                    }
                }
            });

            btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION)
                    {
                        final Tweet mCurrTweet = mTweetList.get(position);
                        if (!mCurrTweet.isFavourited()) {
                            mClient.favouriteStatus(mCurrTweet.getUid(), new JsonHttpResponseHandler()
                            {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    super.onSuccess(statusCode, headers, response);
                                    btnLike.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.heart));
                                    Tweet tweet = mGson.fromJson(response.toString(), Tweet.class);
                                    mTweetList.set(position, tweet);
                                    txtLike.setText(String.valueOf(tweet.getFavouriteCount()));

                                }
                            });
                        }
                        else
                        {
                            mClient.unFavouriteStatus(mCurrTweet.getUid(), new JsonHttpResponseHandler()
                            {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response)
                                {
                                    super.onSuccess(statusCode, headers, response);
                                    btnLike.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.heart_outline));
                                    Tweet tweet = mGson.fromJson(response.toString(), Tweet.class);
                                    mTweetList.set(position, tweet);
                                    txtLike.setText(String.valueOf(tweet.getFavouriteCount()));
                                }
                            });

                        }
                    }

                }
            });

            btnReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION)
                    {
                        Tweet tweet = mTweetList.get(position);
                        TweetComposerDialogFragment.showReplyComposer((Activity)mContext, tweet.getUser().getScreenName(), tweet.getUid(), new JsonHttpResponseHandler()
                        {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                Gson gson = new Gson();
                                Tweet tweet = gson.fromJson(response.toString(), Tweet.class);
                                addTweetOnTop(tweet);
                                //mRecyclerView.scrollToPosition(0);
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                super.onFailure(statusCode, headers, responseString, throwable);
                                Log.e("APP", "reply failed");
                            }
                        });
                    }
                }
            });

            //Retweet
            btnRetweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION)
                    { // Check if an item was deleted, but the user clicked it before the UI removed it
                        final Tweet mCurrTweet = mTweetList.get(position);

                        if (!mCurrTweet.isRetweeted())
                        {
                            mClient.retweetStatus(mCurrTweet.getUid(), new JsonHttpResponseHandler()
                            {
                                @Override
                                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                    super.onFailure(statusCode, headers, responseString, throwable);
                                    Log.e("APP", "unretweet failed");
                                }

                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response)
                                {
                                    super.onSuccess(statusCode, headers, response);
                                    Toast.makeText(btnRetweet.getContext(),"Đã tweet lại", Toast.LENGTH_SHORT).show();
                                    btnRetweet.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.retweeted));

                                    //Set new
                                    Tweet tweet = mGson.fromJson(response.toString(), Tweet.class);
                                    addTweetOnTop(tweet);
                                    //mRecyclerView.scrollToPosition(0);
                                    txtRetweet.setText(String.valueOf(mCurrTweet.getRetweetCount() + 1));
                                }
                            });
                        }
                        else
                        {
                            mClient.unRetweetStatus(mCurrTweet.getUid(), new JsonHttpResponseHandler()
                            {
                                @Override
                                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                    super.onFailure(statusCode, headers, responseString, throwable);
                                    Log.e("APP", "unretweet failed");
                                }

                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    super.onSuccess(statusCode, headers, response);
                                    btnRetweet.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.unretweet));

                                    //Set new
                                    Tweet tweet = mGson.fromJson(response.toString(), Tweet.class);
                                    mTweetList.set(position,tweet);
                                    txtRetweet.setText(String.valueOf(mCurrTweet.getRetweetCount() - 1));
                                }
                            });
                        }
                    }
                }
            });
        }
    }
}
