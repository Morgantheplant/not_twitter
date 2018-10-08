package com.codepath.apps.restclienttemplate;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.activities.ComposeActivity;
import com.codepath.apps.restclienttemplate.adapter.TweetAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;

import javax.annotation.Nullable;

public class TimelineActivity extends AppCompatActivity {
    private TwitterClient client;
    LiveData<PagedList<Tweet>> tweets;
    TweetAdapter tweetAdapter;
    RecyclerView rvTweets;
    TweetDataSourceFactory factory;
    private static int COMPOSE_TWEET = 20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        client = TwitterApplication.getRestClient(this);
        rvTweets = findViewById(R.id.rvTweet);
        tweetAdapter = new TweetAdapter();
        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        rvTweets.setAdapter(tweetAdapter);
        PagedList.Config config = new PagedList.Config.Builder().setPageSize(12).build();
        factory = new TweetDataSourceFactory(client);

        tweets = new LivePagedListBuilder(factory, config).build();

        tweets.observe(this, new Observer<PagedList<Tweet>>() {
            @Override
            public void onChanged(@Nullable PagedList<Tweet> tweets) {
                tweetAdapter.submitList(tweets);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem composeTweet = menu.findItem(R.id.compose);
        composeTweet.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent i = new Intent(TimelineActivity.this, ComposeActivity.class);
                startActivityForResult(i, COMPOSE_TWEET);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == COMPOSE_TWEET && data != null) {
            Tweet tweet = data.getParcelableExtra("new_tweet");
            if(tweet != null){
                Toast.makeText(this, "@"+ tweet.user.screenName +":" + tweet.body, Toast.LENGTH_LONG).show();
                factory.tweetLiveData.getValue().invalidate();

            }

        }
    }

}
