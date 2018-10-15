package com.codepath.apps.restclienttemplate.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codepath.apps.restclienttemplate.fragments.HomeTimelineFragment;
import com.codepath.apps.restclienttemplate.fragments.MentionTimelineFragement;

public class TweetsPagerAdapter extends FragmentPagerAdapter {


    private String[] tabTitles = new String[]{"Home", "Mentions"};
    private Context context;
    public TweetsPagerAdapter(FragmentManager fm, Context context){
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount(){
        return 2;
    }

    @Override
    public Fragment getItem(int position){
        switch (position) {
            case 0:
                return new HomeTimelineFragment();
            case 1:
                return new MentionTimelineFragement();
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
