package com.codepath.apps.restclienttemplate.adapter;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
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
import com.codepath.apps.restclienttemplate.activities.ProfileActivity;
import com.codepath.apps.restclienttemplate.models.Tweet;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class TweetAdapter extends PagedListAdapter<Tweet, TweetAdapter.ViewHolder> {

    public TweetAdapter() {
        super(DIFF_CALLBACK);
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

    private Context context;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Tweet tweet = getItem(position);
        if(tweet == null){
            return;
        }
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
        holder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tweet tweet = getItem(holder.getAdapterPosition());
                String sn = tweet.user.screenName;
                Intent i = new Intent(context, ProfileActivity.class);
                i.putExtra("screen_name", sn);
                i.putExtra("twitter_id", tweet.user.uid);
                context.startActivity(i);


            }
        });
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
