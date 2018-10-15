package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.codepath.apps.restclienttemplate.TwitterApplication;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class HomeTimelineFragment extends TweetsListFragment {
    TwitterClient client;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient(getContext());
        initializeDataSource(this);
    }
    @Override
    public void fetchData(long max_id, int count, JsonHttpResponseHandler jsonHttpResponseHandler){
        client.getHomeTimeline(max_id, count, jsonHttpResponseHandler);
    }
}
