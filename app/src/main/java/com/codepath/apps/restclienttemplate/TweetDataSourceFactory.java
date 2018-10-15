package com.codepath.apps.restclienttemplate;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

import com.codepath.apps.restclienttemplate.models.Tweet;

public class TweetDataSourceFactory extends DataSource.Factory<Long, Tweet> {

    private TweetDataSource dataSource;
    public MutableLiveData<TweetDataSource> tweetLiveData;
    public TweetDataSourceFactory(TweetDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public DataSource<Long, Tweet> create() {
        tweetLiveData = new MutableLiveData<>();
        tweetLiveData.postValue(dataSource);
        return dataSource;
    }
}