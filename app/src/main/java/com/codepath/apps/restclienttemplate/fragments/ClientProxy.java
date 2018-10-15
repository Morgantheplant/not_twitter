package com.codepath.apps.restclienttemplate.fragments;

import com.loopj.android.http.JsonHttpResponseHandler;

public interface ClientProxy {
    void fetchData(long max_id, int count, JsonHttpResponseHandler jsonHttpResponseHandler);
    void fetchData(long twitter_id, long max_id, JsonHttpResponseHandler jsonHttpResponseHandler);
}
