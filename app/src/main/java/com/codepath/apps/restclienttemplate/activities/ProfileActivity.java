package com.codepath.apps.restclienttemplate.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApplication;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.fragments.UserTimelineFragment;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    TwitterClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_white_24dp);
        toolbar.setNavigationOnClickListener(this);
        String screenName = getIntent().getStringExtra("screen_name");
        long tid = getIntent().getLongExtra("twitter_id", 1);
        UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(screenName, tid);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flContainer, userTimelineFragment);
        ft.commit();
        client = TwitterApplication.getRestClient(this);
        client.getUserInfo(tid, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    User user = User.fromJSON(response);
                    addUser(user);
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    User user = User.fromJSON(response.getJSONObject(0));
                    addUser(user);
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }

        });
    }

    public void addUser(User user){
        getSupportActionBar().setTitle("@"+user.screenName);
        populateUserHeadline(user);
    }

    public void populateUserHeadline(User user){
        TextView tvName = findViewById(R.id.tvName);
        TextView tvTagline = findViewById(R.id.tvTagline);
        TextView tvFollowers = findViewById(R.id.tvFollowers);
        TextView tvFollowing = findViewById(R.id.tvFollowing);
        ImageView ivProfileImage = findViewById(R.id.ivProfileImage);
        tvTagline.setText(user.tagLine);
        tvFollowers.setText(user.followersCount + " followers count");
        tvFollowing.setText(user.followingCount + " following count");
        tvName.setText(user.name);
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .transform(new RoundedCorners(100));
        Glide.with(this)
                .load(user.profileImageUrl)
                .apply(options)
                .into(ivProfileImage);
    }

    @Override
    public void onClick(View v) {
        onBackPressed();
    }
}
