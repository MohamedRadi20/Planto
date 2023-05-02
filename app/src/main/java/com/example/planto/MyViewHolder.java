package com.example.planto;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageView,like,dislike,item_profile_image;
    public TextView textView,likeCount,dislikeCount,item_username;

    public MyViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.item_image);
        textView = itemView.findViewById(R.id.item_text);
        like = itemView.findViewById(R.id.like);
        dislike = itemView.findViewById(R.id.dislike);
        likeCount = itemView.findViewById(R.id.like_count);
        dislikeCount = itemView.findViewById(R.id.dislike_count);
        item_profile_image = itemView.findViewById(R.id.item_profile_image);
        item_username = itemView.findViewById(R.id.item_username);
    }
}
