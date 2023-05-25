package com.example.planto;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsViewHolder> {
    private List<Post> itemList;
    FirebaseUser firebaseUser;
    FirebaseFirestore db;
    User user;


    public PostsAdapter(List<Post> itemList) {
        this.itemList = itemList;
    }

    public void setPosts(List<Post> posts) {
        this.itemList = posts;
        notifyDataSetChanged();
    }

    @Override
    public PostsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_layout, parent, false);
        return new PostsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PostsViewHolder holder, int position) {
        Post post = itemList.get(position);
        //holder.imageView.setImageResource(R.drawable.firebaseUser);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        db.collection("users").document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(User.class);
            }
        });
        db.collection("users").document(post.getUserId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User postUser = documentSnapshot.toObject(User.class);
                Picasso.get().load(postUser.getAvatar_url()).into(holder.item_profile_image);
                holder.item_username.setText(postUser.getUsername());
            }
        });
        Picasso.get().load(post.getImageUrl()).into(holder.imageView);
        holder.comments_count.setText(String.valueOf(post.getComments().size()));
        holder.textView.setText(post.getContent());
        holder.likeCount.setText(String.valueOf(post.getLikes().size()));
        holder.dislikeCount.setText(String.valueOf(post.getDislikes().size()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(v.getContext(), "Clicked on " + post.getText(), Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putString("postID", post.getPostId());
                Intent intent = new Intent(v.getContext(), postPage.class);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleReaction(post.getLikes(), post.getDislikes(), "like", holder.likeCount, holder.dislikeCount, post);
            }
        });
        holder.dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleReaction(post.getLikes(), post.getDislikes(), "dislike", holder.likeCount, holder.dislikeCount, post);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    private void handleReaction(List<React> likes, List<React> dislikes, String reactionType, TextView likeView, TextView dislikeView,Post post) {
        int isLiked = checkUserLikes(likes);
        int isDisliked = checkUserDislikes(dislikes);
        if (reactionType.equals("like")) {
            if (isLiked == -1 && isDisliked == -1) {
                likes.add(new React(firebaseUser.getUid(), "like"));
                likeView.setText(String.valueOf(likes.size()));
                db.collection("posts").document(post.getPostId()).update("likes", likes);
            } else if (isLiked != -1) {
                likes.remove(isLiked);
                likeView.setText(String.valueOf(likes.size()));
                db.collection("posts").document(post.getPostId()).update("likes", likes);
            }
        } else {
            if (isLiked == -1 && isDisliked == -1) {
                dislikes.add(new React(firebaseUser.getUid(), "dislike"));
                dislikeView.setText(String.valueOf(dislikes.size()));
                db.collection("posts").document(post.getPostId()).update("dislikes", dislikes);
            } else if (isDisliked != -1) {
                dislikes.remove(isDisliked);
                dislikeView.setText(String.valueOf(dislikes.size()));
                db.collection("posts").document(post.getPostId()).update("dislikes", dislikes);
            }
        }
    }

    private int checkUserLikes(List<React> likes) {
        int index = 0;
        for (React react : likes) {
            if (react.getUserId().equals(firebaseUser.getUid())) {
                return index;
            }
            index++;
        }
        return -1;
    }

    private int checkUserDislikes(List<React> dislikes) {
        int index = 0;
        for (React react : dislikes) {
            if (react.getUserId().equals(firebaseUser.getUid())) {
                return index;
            }
            index++;
        }
        return -1;
    }
}

