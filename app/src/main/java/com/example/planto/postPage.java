package com.example.planto;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class postPage extends AppCompatActivity {
    List<Comment> comments;
    CommentsAdapter adapter;
    String postID;
    TextView item_text,item_username,comments_count,like_count,dislike_count;
    ImageView item_image,item_profile_image,add_comment_button,like,dislike;
    ListView comments_list;
    EditText comment_text;
    FirebaseUser firebaseUser;
    User user;
    FirebaseFirestore db;
    Post post;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_page);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        db.collection("users").document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(com.google.firebase.firestore.DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(User.class);
            }
        });
        item_text = findViewById(R.id.item_text);
        item_image = findViewById(R.id.item_image);
        comments_list = findViewById(R.id.comments_list);
        item_username = findViewById(R.id.item_username);
        item_profile_image = findViewById(R.id.item_profile_image);
        add_comment_button = findViewById(R.id.add_comment_button);
        comment_text = findViewById(R.id.comment_text);
        comments_count= findViewById(R.id.comments_count);
        like_count = findViewById(R.id.like_count);
        dislike_count = findViewById(R.id.dislike_count);
        like = findViewById(R.id.like);
        dislike = findViewById(R.id.dislike);
        like.setOnClickListener(new View.OnClickListener() {
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
                    db.collection("posts").document(postID).update("likes", post.getLikes());
                    like_count.setText(String.valueOf(post.getLikes().size()));
                } else {
                    post.getLikes().add(new React(firebaseUser.getUid(), "like"));
                    db.collection("posts").document(postID).update("likes", post.getLikes());
                    like_count.setText(String.valueOf(post.getLikes().size()));
                }

            }
        });
        dislike.setOnClickListener(new View.OnClickListener() {
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
                    db.collection("posts").document(postID).update("dislikes", post.getDislikes());
                    dislike_count.setText(String.valueOf(post.getDislikes().size()));
                } else {
                    post.getDislikes().add(new React(firebaseUser.getUid(), "dislike"));
                    db.collection("posts").document(postID).update("dislikes", post.getDislikes());
                    dislike_count.setText(String.valueOf(post.getDislikes().size()));
                }
            }
        });
        add_comment_button.setOnClickListener(v -> {
            Comment comment = new Comment(firebaseUser.getUid(),comment_text.getText().toString());
            comments.add(comment);
            db.collection("posts").document(postID).update("comments",comments);
            adapter = new CommentsAdapter(getApplicationContext(), comments);
            comments_list.setAdapter(adapter);
            comments_count.setText(String.valueOf(comments.size()));
        });

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            db.collection("posts").document(extras.getString("postID")).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    post = task.getResult().toObject(Post.class);
                    postID = extras.getString("postID");
                    comments = post.getComments();
                    comments_count.setText(String.valueOf(comments.size()));
                    db.collection("users").document(post.getUserId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(com.google.firebase.firestore.DocumentSnapshot documentSnapshot) {
                            User postUser = documentSnapshot.toObject(User.class);
                            Picasso.get().load(postUser.getAvatar_url()).into( item_profile_image);
                            item_username.setText(postUser.getUsername());
                        }
                    });
                    Picasso.get().load(post.getImageUrl()).into( item_image);
                    item_text.setText(post.getContent());
                    like_count.setText(String.valueOf(post.getLikes().size()));
                    dislike_count.setText(String.valueOf(post.getDislikes().size()));
                    adapter = new CommentsAdapter(getApplicationContext(), comments);
                    comments_list.setAdapter(adapter);
                } else {
                    Toast.makeText(this, "Error getting documents.", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}