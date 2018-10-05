package com.codepath.apps.restclienttemplate.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class TweetAdapter extends ListAdapter<Tweet, TweetAdapter.ViewHolder> {
    private List<Tweet> mTweets = new ArrayList<>();

    public TweetAdapter() {
        super(DIFF_CALLBACK);
    }

    public void addMoreTweets(List<Tweet> newTweets) {
        mTweets.addAll(newTweets);
        submitList(mTweets); // DiffUtil takes care of the check
    }

    public static final DiffUtil.ItemCallback<Tweet> DIFF_CALLBACK = new DiffUtil.ItemCallback<Tweet>() {
                @Override
                public boolean areItemsTheSame(Tweet oldItem, Tweet newItem) {
                    return oldItem.getPostId() == newItem.getPostId();
                }

                @Override
                public boolean areContentsTheSame(Tweet oldItem, Tweet newItem) {
                    return (oldItem.createdAt.equals(newItem.createdAt) && oldItem.body.equals(newItem.body));
                }
            };


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tweet tweet = getItem(position);
        holder.tvUsername.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);
        holder.tvTimestamp.setText(tweet.createdAt);
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .transform(new RoundedCorners(20))
                .placeholder(R.drawable.ic_person_blue_24dp)
                .error(R.drawable.ic_person_blue_24dp);
        Glide.with(holder.ivProfileImage.getContext())
                .load(tweet.user.profileImageUrl)
                .transition(withCrossFade())
                .apply(options)
                .into(holder.ivProfileImage);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvBody;
        public TextView tvTimestamp;
        public ViewHolder(View itemView){
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
        }
    }
}
