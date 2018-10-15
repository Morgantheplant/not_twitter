package com.codepath.apps.restclienttemplate.fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TweetDataSource;
import com.codepath.apps.restclienttemplate.TweetDataSourceFactory;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.adapter.TweetAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TweetsListFragment extends Fragment implements ClientProxy {
    private TwitterClient client;
    LiveData<PagedList<Tweet>> tweets;
    TweetAdapter tweetAdapter;
    RecyclerView rvTweets;
    TweetDataSourceFactory factory;
    private static int COMPOSE_TWEET = 20;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragments_tweets_list, container, false);
        Context ctx = getContext();
        rvTweets = v.findViewById(R.id.rvTweet);
        tweetAdapter = new TweetAdapter();
        rvTweets.setLayoutManager(new LinearLayoutManager(ctx));
        rvTweets.setAdapter(tweetAdapter);
        return v;
    }

    public void initializeDataSource(ClientProxy clientProxy){
        PagedList.Config config = new PagedList.Config.Builder().setPageSize(12).build();
        TweetDataSource tds =  new TweetDataSource(clientProxy);
        factory = new TweetDataSourceFactory(tds);
        tweets = new LivePagedListBuilder(factory, config).build();

        tweets.observe(this, new Observer<PagedList<Tweet>>() {
            @Override
            public void onChanged(@javax.annotation.Nullable PagedList<Tweet> tweets) {
                tweetAdapter.submitList(tweets);
            }
        });

    }

    public void initializeProfileDataSource(ClientProxy clientProxy, long tid){
        PagedList.Config config = new PagedList.Config.Builder().setPageSize(12).build();
        TweetDataSource tds = new TweetDataSource(clientProxy, tid);
        factory = new TweetDataSourceFactory(tds);
        tweets = new LivePagedListBuilder(factory, config).build();

        tweets.observe(this, new Observer<PagedList<Tweet>>() {
            @Override
            public void onChanged(@javax.annotation.Nullable PagedList<Tweet> tweets) {
                tweetAdapter.submitList(tweets);
            }
        });
    }

    public void onRefreshList(Tweet tweet){
        Toast.makeText(getContext(), "@"+ tweet.user.screenName +":" + tweet.body, Toast.LENGTH_LONG).show();
        factory.tweetLiveData.getValue().invalidate();
    }

    public void fetchData(long max_id, int count, JsonHttpResponseHandler jsonHttpResponseHandler){
    }
    public void fetchData(long tid, long count, JsonHttpResponseHandler jsonHttpResponseHandler){

    }
}
