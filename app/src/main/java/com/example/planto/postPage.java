package com.example.planto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class postPage extends AppCompatActivity {
    final int EDIT_POST_REQUEST_CODE = 1;
    List<Comment> comments;
    CommentsAdapter adapter;
    String postID;
    TextView item_text, item_username, comments_count, like_count, dislike_count;
    ImageView item_image, item_profile_image, add_comment_button, like, dislike, post_options;
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
        initializeFirebase();
        initializeViews();
        fetchCurrentUser();
    }

    private void initializeViews() {
        item_text = findViewById(R.id.item_text);
        item_image = findViewById(R.id.item_image);
        comments_list = findViewById(R.id.comments_list);
        item_username = findViewById(R.id.item_username);
        item_profile_image = findViewById(R.id.item_profile_image);
        add_comment_button = findViewById(R.id.add_comment_button);
        comment_text = findViewById(R.id.comment_text);
        comments_count = findViewById(R.id.comments_count);
        like_count = findViewById(R.id.like_count);
        dislike_count = findViewById(R.id.dislike_count);
        like = findViewById(R.id.like);
        dislike = findViewById(R.id.dislike);
        post_options = findViewById(R.id.post_options);
        post_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (post.getUserId().equals(firebaseUser.getUid()))
                    showPostPopupMenu(v);
            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleReaction(post.getLikes(), post.getDislikes(), "like", like_count, dislike_count);
            }
        });
        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleReaction(post.getLikes(), post.getDislikes(), "dislike", like_count, dislike_count);
            }
        });
        add_comment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addComment();
            }
        });

        comments_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (comments.get(position).getUser_id().equals(firebaseUser.getUid()))
                    showCommentPopupMenu(view, position);
                return true;
            }
        });

        if (getIntent().getExtras() != null) {
            postID = getIntent().getExtras().getString("postID");
            fetchPostData(postID);
        }
    }

    private void initializeFirebase() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
    }

    private void fetchCurrentUser() {
        db.collection("users").document(firebaseUser.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        user = documentSnapshot.toObject(User.class);
                    }
                });
    }

    private void fetchPostData(String postId) {
        db.collection("posts").document(postId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    post = task.getResult().toObject(Post.class);
                    if (post != null) {
                        displayPostData(post);
                    }
                } else {
                    Toast.makeText(postPage.this, "Error getting documents.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void displayPostData(Post post) {
        comments = post.getComments();
        comments_count.setText(String.valueOf(comments.size()));
        db.collection("users").document(post.getUserId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User postUser = documentSnapshot.toObject(User.class);
                if (postUser != null) {
                    Picasso.get().load(postUser.getAvatar_url()).into(item_profile_image);
                    item_username.setText(postUser.getUsername());
                }
            }
        });
        Picasso.get().load(post.getImageUrl()).into(item_image);
        item_text.setText(post.getContent());
        like_count.setText(String.valueOf(post.getLikes().size()));
        dislike_count.setText(String.valueOf(post.getDislikes().size()));
        adapter = new CommentsAdapter(getApplicationContext(), comments);
        comments_list.setAdapter(adapter);
    }

    private void handleReaction(List<React> likes, List<React> dislikes, String reactionType, TextView likeView, TextView dislikeView) {
        int isLiked = checkUserLikes(likes);
        int isDisliked = checkUserDislikes(dislikes);
        if (reactionType.equals("like")) {
            if (isLiked == -1 && isDisliked == -1) {
                likes.add(new React(firebaseUser.getUid(), "like"));
                likeView.setText(String.valueOf(likes.size()));
                db.collection("posts").document(postID).update("likes", likes);
            } else if (isLiked != -1) {
                likes.remove(isLiked);
                likeView.setText(String.valueOf(likes.size()));
                db.collection("posts").document(postID).update("likes", likes);
            }
        } else {
            if (isLiked == -1 && isDisliked == -1) {
                dislikes.add(new React(firebaseUser.getUid(), "dislike"));
                dislikeView.setText(String.valueOf(dislikes.size()));
                db.collection("posts").document(postID).update("dislikes", dislikes);
            } else if (isDisliked != -1) {
                dislikes.remove(isDisliked);
                dislikeView.setText(String.valueOf(dislikes.size()));
                db.collection("posts").document(postID).update("dislikes", dislikes);
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

    private void addComment() {
        if(comment_text.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter a comment", Toast.LENGTH_SHORT).show();
            return;
        }
        Comment comment = new Comment(firebaseUser.getUid(), comment_text.getText().toString());
        comments.add(comment);
        updateCommentsInDatabaseAndUI();
    }

    private void updateCommentsInDatabaseAndUI() {
        db.collection("posts").document(postID).update("comments", comments);
        adapter = new CommentsAdapter(getApplicationContext(), comments);
        comments_list.setAdapter(adapter);
        comments_count.setText(String.valueOf(comments.size()));
    }

    private void showCommentPopupMenu(View view, int position) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.comment_popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete:
                        deleteComment(position);
                        return true;
                    case R.id.edit:
                        editComment(position);
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }

    private void showPostPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.post_popup_menu, popupMenu.getMenu());

        // Only show the menu for the post's owner
        if (!post.getUserId().equals(firebaseUser.getUid())) {
            return;
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.edit_post:
                        editPost();
                        return true;
                    case R.id.delete_post:
                        deletePost();
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }


    private void deleteComment(int position) {
        comments.remove(position);
        updateCommentsInDatabaseAndUI();
    }

    private void editComment(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit comment");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                comments.get(position).setContent(input.getText().toString());
                updateCommentsInDatabaseAndUI();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void deletePost() {
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(post.getImageUrl());
        storageReference.delete();
        db.collection("posts").document(postID).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Post deleted successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    private void editPost() {
        // Create an Intent to open the edit post activity
        Intent intent = new Intent(postPage.this, EditPost.class);
        intent.putExtra("postID", postID);

        startActivityForResult(intent, EDIT_POST_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_POST_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            String updatedContent = data.getStringExtra("postContent");
            String updatedImageUrl = data.getStringExtra("postImageUrl");
            if (updatedContent != null) {
                // Update the post content in your UI
                item_text.setText(updatedContent);
                Picasso.get().load(updatedImageUrl).into(item_image);
            }
        }
    }

}
