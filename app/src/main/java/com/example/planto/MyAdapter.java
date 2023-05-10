package com.example.planto;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private List<Post> itemList;
    FirebaseUser firebaseUser;
    FirebaseFirestore db;
    User user;

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
                boolean isLiked = false;
                int index1 = 0;
                for (React react : post.getLikes()) {
                    if (react.getUserId().equals(firebaseUser.getUid())) {
                        isLiked = true;
                        index1 = post.getLikes().indexOf(react);
                        break;
                    }
                }
                boolean isDisliked = false;
                int index2 = 0;
                for (React react : post.getDislikes()) {
                    if (react.getUserId().equals(firebaseUser.getUid())) {
                        isDisliked = true;
                        index2 = post.getDislikes().indexOf(react);
                        break;
                    }
                }
                if (isDisliked) return;
                if (isLiked) {
                    post.getLikes().remove(index1);
                    db.collection("posts").document(post.getPostId()).update("likes", post.getLikes());
                    holder.likeCount.setText(String.valueOf(post.getLikes().size()));
                } else {
                    post.getLikes().add(new React(firebaseUser.getUid(), "like"));
                    db.collection("posts").document(post.getPostId()).update("likes", post.getLikes());
                    holder.likeCount.setText(String.valueOf(post.getLikes().size()));
                }

            }
        });
        holder.dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isLiked = false;
                int index1 = 0;
                for (React react : post.getLikes()) {
                    if (react.getUserId().equals(firebaseUser.getUid())) {
                        isLiked = true;
                        index1 = post.getLikes().indexOf(react);
                        break;
                    }
                }
                boolean isDisliked = false;
                int index2 = 0;
                for (React react : post.getDislikes()) {
                    if (react.getUserId().equals(firebaseUser.getUid())) {
                        isDisliked = true;
                        index2 = post.getDislikes().indexOf(react);
                        break;
                    }
                }
                if (isLiked) return;
                if (isDisliked) {
                    post.getDislikes().remove(index2);
                    db.collection("posts").document(post.getPostId()).update("dislikes", post.getDislikes());
                    holder.dislikeCount.setText(String.valueOf(post.getDislikes().size()));
                } else {
                    post.getDislikes().add(new React(firebaseUser.getUid(), "dislike"));
                    db.collection("posts").document(post.getPostId()).update("dislikes", post.getDislikes());
                    holder.dislikeCount.setText(String.valueOf(post.getDislikes().size()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}

