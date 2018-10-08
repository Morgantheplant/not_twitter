package com.codepath.apps.restclienttemplate;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

import com.codepath.apps.restclienttemplate.models.Tweet;

public class TweetDataSourceFactory extends DataSource.Factory<Long, Tweet> {

    TwitterClient client;
    public MutableLiveData<TweetDataSource> tweetLiveData;
    public TweetDataSourceFactory(TwitterClient client) {
        this.client = client;
    }

    @Override
    public DataSource<Long, Tweet> create() {
        TweetDataSource dataSource = new TweetDataSource(this.client);
        tweetLiveData = new MutableLiveData<>();
        tweetLiveData.postValue(dataSource);

        return dataSource;
    }
}