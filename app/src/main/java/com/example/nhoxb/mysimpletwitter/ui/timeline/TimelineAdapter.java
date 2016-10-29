package com.example.nhoxb.mysimpletwitter.ui.timeline;

import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nhoxb.mysimpletwitter.R;
import com.example.nhoxb.mysimpletwitter.activity.TimelineActivity;
import com.example.nhoxb.mysimpletwitter.model.Tweet;
import com.example.nhoxb.mysimpletwitter.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by nhoxb on 10/29/2016.
 */
public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.TimelineViewHolder>
{
    List<Tweet> mTweetList;

    public TimelineAdapter() {
        mTweetList = new ArrayList<>();
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

    @Override
    public TimelineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_tweet, parent, false);

        return new TimelineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TimelineViewHolder holder, int position) {
        Tweet tweet = mTweetList.get(position);

        holder.txtBody.setText(tweet.getBody());
        holder.txtName.setText(tweet.getUser().getName());
        holder.txtScreenName.setText("@" + tweet.getUser().getScreenName());
        holder.txtTime.setText(Utils.getRelativeTimeAgo(tweet.getCreatedAt()));

        Glide.with(holder.avatar.getContext())
                .load(tweet.getUser().getAvatarUrl())
                .bitmapTransform(new RoundedCornersTransformation(holder.avatar.getContext(),4,2))
                .into(holder.avatar);
    }

    @Override
    public int getItemCount() {
        return mTweetList.size();
    }

    class TimelineViewHolder extends RecyclerView.ViewHolder
    {
        ImageView avatar;
        TextView txtName;
        TextView txtScreenName;
        TextView txtBody;
        TextView txtTime;
        public TimelineViewHolder(View itemView) {
            super(itemView);
            avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
            txtName = (TextView) itemView.findViewById(R.id.tv_username);
            txtScreenName = (TextView) itemView.findViewById(R.id.tv_user_screenname);
            txtBody = (TextView) itemView.findViewById(R.id.tv_tweet_body);
            txtTime = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }
}
