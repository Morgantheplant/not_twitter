package com.codepath.apps.restclienttemplate;

import android.arch.paging.ItemKeyedDataSource;
import android.support.annotation.NonNull;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TweetDataSource extends ItemKeyedDataSource<Long, Tweet> {
    @NonNull
    @Override
    public Long getKey(@NonNull Tweet item) {
        return item.getPostId();
    }

    TwitterClient mClient;

    public TweetDataSource(TwitterClient client) {
        mClient = client;
    }

    public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull final LoadInitialCallback<Tweet> callback) {
        // Fetch data synchronously (second parameter is set to true)
        // load an initial data set so the paged list is not empty.
        // See https://issuetracker.google.com/u/2/issues/110843692?pli=1
        JsonHttpResponseHandler jsonHttpResponseHandler = createTweetHandler(callback, true);

        // No max_id should be passed on initial load
        mClient.getHomeTimeline(0, 12, jsonHttpResponseHandler);
    }

    // Called repeatedly when more data needs to be set
    @Override
    public void loadAfter(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Tweet> callback) {
        // This network call can be asynchronous (second parameter is set to false)
        JsonHttpResponseHandler jsonHttpResponseHandler = createTweetHandler(callback, true);

        // params.key & requestedLoadSize should be used
        // params.key will be the lowest Twitter post ID retrieved and should be used for the max_id= parameter in Twitter API.
        // max_id = params.key - 1
        mClient.getHomeTimeline(params.key - 1, 12, jsonHttpResponseHandler);
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Tweet> callback) { }

    public JsonHttpResponseHandler createTweetHandler(final LoadCallback<Tweet> callback, boolean isAsync) {

        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                ArrayList<Tweet> tweets = new ArrayList<Tweet>();
                tweets = Tweet.fromJSONArray(response);
                callback.onResult(tweets);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                ArrayList<Tweet> tweets = new ArrayList<Tweet>();
//                tweets = Tweet.fromJSON(response);
//                callback.onResult(tweets);
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