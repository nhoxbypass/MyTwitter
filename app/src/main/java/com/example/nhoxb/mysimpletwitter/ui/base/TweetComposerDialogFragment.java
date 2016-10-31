package com.example.nhoxb.mysimpletwitter.ui.base;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.nhoxb.mysimpletwitter.R;
import com.example.nhoxb.mysimpletwitter.model.Tweet;
import com.example.nhoxb.mysimpletwitter.rest.TwitterApplication;
import com.example.nhoxb.mysimpletwitter.rest.TwitterClient;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;
import org.w3c.dom.Text;

import cz.msebera.android.httpclient.Header;

/**
 * Created by nhoxb on 10/29/2016.
 */
public class TweetComposerDialogFragment extends DialogFragment
{
    Button mCloseButton;
    Button mTweetButton;
    EditText mInputField;
    TextView mCharCountTextview;
    RelativeLayout mReplyContainer;
    TextView mReplyText;

    private TweetComposerDialogListener mListener;

    // 1. Defines the listener interface with a method passing back data result.
    public interface TweetComposerDialogListener {
        void onTweeted(String body);
    }


    public TweetComposerDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static TweetComposerDialogFragment newInstance(String name)
    {
        TweetComposerDialogFragment fragment = new TweetComposerDialogFragment();

        Bundle args = new Bundle();
        args.putString("name",name);
        fragment.setArguments(args);

        return fragment;
    }

    public static void showReplyComposer(Activity activity, String screenName, final long tweetedUid, final AsyncHttpResponseHandler handler)
    {
        TweetComposerDialogFragment fragment = TweetComposerDialogFragment.newInstance(screenName);
        fragment.setStyle(DialogFragment.STYLE_NORMAL,R.style.Dialog_FullScreen);
        fragment.show(activity.getFragmentManager(), "fragment_compose_tweet");
        fragment.setOnTweetedListener(new TweetComposerDialogFragment.TweetComposerDialogListener() {
            @Override
            public void onTweeted(String body) {
                TwitterApplication.getRestClient().updateStatus(body, String.valueOf(tweetedUid),handler);
            }
        });
    }

    public void setOnTweetedListener(TweetComposerDialogListener onTweetedListener)
    {
        mListener = onTweetedListener;
    }

    private void setReplyMode(String screenName)
    {
        mReplyText.setText("In reply to " + screenName);
        mInputField.append("@" + screenName + " ");
        mReplyContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        //request
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compose_tweet, container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String replyName = getArguments().getString("name", "");

        //get view
        mCloseButton = (Button) view.findViewById(R.id.btn_close_composer);
        mTweetButton = (Button) view.findViewById(R.id.btn_tweet);
        mCharCountTextview = (TextView) view.findViewById(R.id.tv_count_char);
        mInputField = (EditText) view.findViewById(R.id.tweet_input);
        mReplyContainer  = (RelativeLayout) view.findViewById(R.id.reply_container);
        mReplyText = (TextView) view.findViewById(R.id.tv_reply);

        setCharsCounter(mInputField);
        //
        mInputField.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        mTweetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = mInputField.getText().toString();
                mListener.onTweeted(text);
                dismiss();
            }
        });

        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        if (!(replyName == null || replyName.equals("") || replyName.isEmpty()))
        {
            setReplyMode(replyName);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }


    @Override
    public void onPause() {
        //Get existing layout params of window
        ViewGroup.LayoutParams layoutParams = getDialog().getWindow().getAttributes();

        //Assign window properties to layout params width and height
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;

        //Set the layout params back to the dialog
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams)layoutParams);

        // Call super onResume after sizing
        super.onResume();
    }


    //
    private void setCharsCounter(EditText editText)
    {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCharCountTextview.setText(String.valueOf(140 - count));
                if (count > 140)
                {
                    mCharCountTextview.setTextColor(ContextCompat.getColor(mCharCountTextview.getContext(), R.color.googleRed));
                }
                else
                {
                    mCharCountTextview.setTextColor(Color.parseColor("#9E9E9E"));
                }

                if (count > 0 && count <= 140)
                {
                    //Limit the number of char in one tweet
                    mTweetButton.setEnabled(true);
                }
                else
                {
                    mTweetButton.setEnabled(false);
                    mReplyContainer.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
