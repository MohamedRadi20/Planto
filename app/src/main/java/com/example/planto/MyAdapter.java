package com.example.planto;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private List<Post> itemList;
    FirebaseUser user;

    public MyAdapter(List<Post> itemList) {
        this.itemList = itemList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Post post = itemList.get(position);
        //holder.imageView.setImageResource(R.drawable.user);
        user= FirebaseAuth.getInstance().getCurrentUser();
        Picasso.get().load(post.getImageUrl()).into( holder.imageView);
        Picasso.get().load(user.getPhotoUrl()).into( holder.item_profile_image);
        holder.item_username.setText(user.getDisplayName());
        holder.textView.setText(post.getText());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(v.getContext(), "Clicked on " + post.getText(), Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putString("postText", post.getText());
                bundle.putString("postImage", post.getImageUrl());
                bundle.putString("postUser", post.getUser());
                bundle.putInt("postUpvotes", post.getUpvotes());
                bundle.putInt("postDownvotes", post.getDownvotes());
                bundle.putStringArrayList("postComments", post.getComments());
                Intent intent = new Intent(v.getContext(), postPage.class);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.likeCount.setText(String.valueOf(Integer.parseInt(holder.likeCount.getText().toString())+1));
            }
        });
        holder.dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.dislikeCount.setText(String.valueOf(Integer.parseInt(holder.dislikeCount.getText().toString())+1));
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}

