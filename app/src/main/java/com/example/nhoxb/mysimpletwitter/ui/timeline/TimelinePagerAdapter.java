package com.example.nhoxb.mysimpletwitter.ui.timeline;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.nhoxb.mysimpletwitter.ui.timeline.TimelineFragment;

/**
 * Created by nhoxb on 11/2/2016.
 */
public class TimelinePagerAdapter extends FragmentPagerAdapter
{
    private int TAB_COUNT = 2;
    private String titles[] = new String[]{"Timeline", "Mention", "Message"};
    private Context mContext;

    public TimelinePagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return TimelineFragment.newInstance(TimelineFragment.HOME_TIMELINE);
            case 1:
                return TimelineFragment.newInstance(TimelineFragment.MENTIONS_TIMELINE);
        }
        return null;
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
