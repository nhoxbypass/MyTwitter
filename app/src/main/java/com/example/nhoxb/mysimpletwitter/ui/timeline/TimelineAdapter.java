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

    public TimelineAdapter(Context context) {
        mTweetList = new ArrayList<>();
        mContext = context;
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

    public void addTweet(Tweet tweet)
    {
        int position = mTweetList.size();
        mTweetList.add(tweet);
        notifyItemRangeInserted(position, 1);
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
        holder.txtLike.setText(String.valueOf(tweet.getUser().getFavouriteCount()));

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
        @BindView(R.id.btn_item_reply) Button  btnReply;
        @BindView(R.id.btn_item_retweet) Button btnRetweet;
        @BindView(R.id.btn_item_like) ImageButton btnLike;
        @BindView(R.id.tv_count_retweet) TextView txtRetweet;
        @BindView(R.id.tv_count_like) TextView txtLike;
        @BindView(R.id.iv_media)    ImageView media;

        private TwitterClient mClient;
        private int position;

        public TimelineViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            mClient = TwitterApplication.getRestClient();

            btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnLike.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.heart));
                }
            });

            btnReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION)
                    {
                        TweetComposerDialogFragment fragment = TweetComposerDialogFragment.newInstance(mTweetList.get(position).getUser().getScreenName());
                        fragment.setStyle(DialogFragment.STYLE_NORMAL,R.style.Dialog_FullScreen);
                        fragment.show(((Activity) mContext).getFragmentManager(), "fragment_compose_tweet");
                        fragment.setOnTweetedListener(new TweetComposerDialogFragment.TweetComposerDialogListener() {
                            @Override
                            public void onTweeted(String body) {
                                mClient.updateStatus(body, String.valueOf(mTweetList.get(position).getUid()),new JsonHttpResponseHandler()
                                {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        super.onSuccess(statusCode, headers, response);
                                        Gson gson = new Gson();
                                        Tweet tweet = gson.fromJson(response.toString(), Tweet.class);
                                        addTweet(tweet);
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                        super.onFailure(statusCode, headers, responseString, throwable);
                                        Log.e("APP", "retweet failed");
                                    }
                                });
                            }
                        });
                    }
                }
            });

            btnRetweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION)
                    { // Check if an item was deleted, but the user clicked it before the UI removed it
                        mClient.retweetStatus(mTweetList.get(position).getUid(), new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                Gson gson = new Gson();
                                Tweet tweet = gson.fromJson(response.toString(), Tweet.class);
                                addTweet(tweet);
                                Toast.makeText(btnRetweet.getContext(),"Retweeted", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                super.onFailure(statusCode, headers, responseString, throwable);
                                Log.e("APP", "retweet failed");
                            }
                        });
                    }
                }
            });
        }
    }
}
