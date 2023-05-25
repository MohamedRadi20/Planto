package com.example.planto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class EditPost extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText editPostContent;
    private Uri mImageUri;
    private FirebaseStorage storage;
    private Button btnSave;
    private ImageView img_post;
    private String postID;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        System.out.println("EditPost.java");
        editPostContent = findViewById(R.id.edit_post_content);
        btnSave = findViewById(R.id.btn_save);
        img_post = findViewById(R.id.img_post);
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        postID = getIntent().getStringExtra("postID");

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePostChanges();
            }
        });
        img_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
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
                            if (post.getImageUrl() != null) {
                                Picasso.get().load(post.getImageUrl()).into(img_post);
                            }
                        }
                    }
                });
    }

    private void savePostChanges() {
        String newContent = editPostContent.getText().toString().trim();
        updatePostIamege(newContent);
    }

    private void openFileChooser() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            img_post.setImageURI(mImageUri);
        }
    }

    private void updatePost(String newContent, String downloadUrl) {
        db.collection("posts").document(postID)
                .update("content", newContent, "imageUrl", downloadUrl)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditPost.this, "Post updated successfully", Toast.LENGTH_SHORT).show();
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("postContent", newContent);
                        resultIntent.putExtra("postImageUrl", downloadUrl);
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

    private void updatePostIamege(String newContent) {
        StorageReference storageRef = storage.getReference().child("posts");
        StorageReference imageRef = storageRef.child(postID);
        img_post.setDrawingCacheEnabled(true);
        img_post.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) img_post.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@androidx.annotation.NonNull Exception exception) {
                Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String downloadUrl = uri.toString();
                        updatePost(newContent, downloadUrl);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@androidx.annotation.NonNull Exception exception) {
                        System.out.println(exception);
                    }
                });
            }
        });

    }
}
