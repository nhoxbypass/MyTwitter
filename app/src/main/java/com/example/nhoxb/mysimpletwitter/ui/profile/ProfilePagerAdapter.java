package com.example.nhoxb.mysimpletwitter.ui.profile;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by nhoxb on 11/3/2016.
 */
public class ProfilePagerAdapter extends FragmentPagerAdapter {

    private int TAB_COUNT = 3;
    private String titles[] = new String[]{"Tweet", "Phương tiện", "Lượt thích"};

    public ProfilePagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ProfileFragment.newInstance(ProfileFragment.TWEET_LIST);
            case 1:
                return ProfileFragment.newInstance(ProfileFragment.MEDIA_LIST);
            case 2:
                return ProfileFragment.newInstance(ProfileFragment.LIKE_LIST);
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }
}
