package com.example.planto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditPost extends AppCompatActivity {

    private EditText editPostContent;
    private Button btnSave;

    private String postID;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        editPostContent = findViewById(R.id.edit_post_content);
        btnSave = findViewById(R.id.btn_save);

        db = FirebaseFirestore.getInstance();

        postID = getIntent().getStringExtra("postID");

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePostChanges();
            }
        });

        loadPostData();
    }

    private void loadPostData() {
        // Fetch the post data from Firestore based on the postID
        db.collection("posts").document(postID).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Post post = documentSnapshot.toObject(Post.class);
                        if (post != null) {
                            editPostContent.setText(post.getContent());
                        }
                    }
                });
    }

    private void savePostChanges() {
        String newContent = editPostContent.getText().toString().trim();

        if (TextUtils.isEmpty(newContent)) {
            Toast.makeText(EditPost.this, "Please enter post content", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update the post content in Firestore
        db.collection("posts").document(postID)
                .update("content", newContent)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditPost.this, "Post updated successfully", Toast.LENGTH_SHORT).show();
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("postContent", newContent);
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditPost.this, "Failed to update post", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
