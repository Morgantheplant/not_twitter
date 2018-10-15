package com.codepath.apps.restclienttemplate;

import android.arch.paging.ItemKeyedDataSource;
import android.support.annotation.NonNull;

import com.codepath.apps.restclienttemplate.fragments.ClientProxy;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TweetDataSource extends ItemKeyedDataSource<Long, Tweet> {
    private long tid;
    @NonNull
    @Override
    public Long getKey(@NonNull Tweet item) {
        return item.getPostId();
    }

    public TweetDataSource(ClientProxy clientProxy, long tid) {
        this.clientProxy = clientProxy;
        this.tid = tid;
    }
    public TweetDataSource(ClientProxy clientProxy) {
        this.clientProxy = clientProxy;
    }
    private ClientProxy clientProxy; // this may be incorrect

    public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull final LoadInitialCallback<Tweet> callback) {
        JsonHttpResponseHandler jsonHttpResponseHandler = createTweetHandler(callback, true);
        if(tid != 0){
            long zero = 0;
            clientProxy.fetchData(tid, zero, jsonHttpResponseHandler);
        } else {
            clientProxy.fetchData(0, 12, jsonHttpResponseHandler);

        }
    }

    // Called repeatedly when more data needs to be set
    @Override
    public void loadAfter(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Tweet> callback) {
        JsonHttpResponseHandler jsonHttpResponseHandler = createTweetHandler(callback, true);
        if(tid != 0){
            clientProxy.fetchData(tid, params.key - 1, jsonHttpResponseHandler);
        } else {
            clientProxy.fetchData(params.key - 1, 12, jsonHttpResponseHandler);
        }

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Tweet> callback) { }

    public JsonHttpResponseHandler createTweetHandler(final LoadCallback<Tweet> callback, boolean isAsync) {

        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                ArrayList<Tweet> tweets = Tweet.fromJSONArray(response);
                callback.onResult(tweets);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }



            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        };

        if (isAsync) {
            handler.setUseSynchronousMode(true);
            handler.setUsePoolThread(true);
        }

        return handler;
    }
}