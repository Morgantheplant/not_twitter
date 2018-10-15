package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.codepath.apps.restclienttemplate.TwitterApplication;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class UserTimelineFragment extends TweetsListFragment {
    TwitterClient client;
    String screenname;
    long tid = 1;
    public static UserTimelineFragment newInstance(String screenname,long tid) {
        UserTimelineFragment userTimelineFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putLong("tid", tid);
        args.putString("screen_name", screenname);
        userTimelineFragment.setArguments(args);
        return userTimelineFragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screenname = getArguments().getString("screen_name", "");
        tid = getArguments().getLong("tid", 1);
        client = TwitterApplication.getRestClient(getContext());
        initializeProfileDataSource(this, tid);

    }


    @Override
    public void fetchData(long twitter_id, long max_id,  JsonHttpResponseHandler jsonHttpResponseHandler){
        client.getUserTimeline(screenname, max_id, jsonHttpResponseHandler);
    }
}
