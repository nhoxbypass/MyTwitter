package com.example.nhoxb.mysimpletwitter.ui.profile;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.nhoxb.mysimpletwitter.R;
import com.example.nhoxb.mysimpletwitter.data.remote.model.User;
import com.example.nhoxb.mysimpletwitter.ui.timeline.TimelineActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.appbar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.iv_cover)
    ImageView mCover;
    @BindView(R.id.iv_avatar)
    ImageView mAvatar;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.tablayout)
    TabLayout mTabLayout;

    private ProfilePagerAdapter mPagerAdapter;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);
        mAvatar.bringToFront();


        mUser = getIntent().getExtras().getParcelable(TimelineActivity.KEY_PROFILE);

        setSupportActionBar(mToolbar);

        mPagerAdapter = new ProfilePagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        Glide.with(this)
                .load(mUser.getCoverUrl())
                .into(mCover);
        Glide.with(this)
                .load(mUser.getAvatarUrl())
                .into(mAvatar);

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int maxScroll = appBarLayout.getTotalScrollRange(); //350 - 160 = 190
                float percentage = (float) (maxScroll + verticalOffset) / (float) (mAvatar.getHeight() * 2);
                if (percentage >= 0.25) {

                    //ViewCompat.animate(mAvatar).scaleX(percentage).setDuration(100);
                    //ViewCompat.animate(mAvatar).scaleY(percentage).setDuration(100);
                    mAvatar.setScaleX(percentage);
                    mAvatar.setScaleY(percentage);
                    //mAvatar.setTranslationY(verticalOffset);
                    //mAvatar.getLayoutParams().width += percentage*80;
                    // mAvatar.getLayoutParams().height += percentage*80;
                }

            }
        });
    }
}
