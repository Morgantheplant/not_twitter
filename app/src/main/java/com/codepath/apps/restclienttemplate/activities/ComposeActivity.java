package com.codepath.apps.restclienttemplate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApplication;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity implements View.OnClickListener {
    EditText tweetComposer;
    private TwitterClient client;
    private String inResposeTo = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        tweetComposer = findViewById(R.id.tweet_composer);
        Toolbar toolbar = findViewById(R.id.compose_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_white_24dp);
        toolbar.setNavigationOnClickListener(this);
        client = TwitterApplication.getRestClient(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.compose_menu, menu);
        MenuItem composeTweet = menu.findItem(R.id.tweet);
        ActionBar ab = getSupportActionBar();
        if(ab != null){
            ab.setTitle(R.string.compose_tweet);
        }
        composeTweet.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(tweetComposer != null){
                    String tweet = tweetComposer.getText().toString();
                    composeTweet(tweet);

                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public void onClick(View v) {
        onBackPressed();
    }

    public void composeTweet(final String tweet){
        if (!"".equals(tweet)){
            client.composeTweet(tweet, inResposeTo, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                    Toast.makeText(getApplicationContext(), getResources().getText(R.string.success), Toast.LENGTH_LONG).show();
                    JSONObject result = parseResult(responseBody);
                    tweetComposer.setText("");
                    Tweet tweet = Tweet.fromJSON(result);
                    Intent data = new Intent();
                    data.putExtra("new_tweet", tweet);
                    setResult(RESULT_OK, data);
                    finish();
                }


                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(getApplicationContext(), getResources().getText(R.string.failure), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private JSONObject parseResult(byte[] response) {
        String str = null;
        try {
            str = new String(response, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONObject responseToJson = null;
        try {
            responseToJson = new JSONObject(str);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return responseToJson;
    }

}
