package com.example.planto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class updateProfile extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    FirebaseFirestore db;
    FirebaseStorage storage;
    FirebaseUser firebaseUser;
    Uri mImageUri;

    ImageView profile_image;
    Button upload_photo, update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        profile_image = findViewById(R.id.profile_image);
        upload_photo = findViewById(R.id.upload_photo);
        update = findViewById(R.id.update);
        upload_photo.setOnClickListener(v -> openFileChooser());
        update.setOnClickListener(v -> {
            Upload_photo();
            finish();
        });
    }

    void Upload_photo() {

        // Save the post to the database or send it to the server
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference().child("profile");

        // Create a reference to "image.jpg"
        StorageReference imageRef = storageRef.child(firebaseUser.getUid());

        // Get the data from an ImageView as bytes
        profile_image.setDrawingCacheEnabled(true);
        profile_image.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) profile_image.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Handle successful download URL retrieval
                        String downloadUrl = uri.toString();
                        db.collection("users").document(firebaseUser.getUid()).update("avatar_url", downloadUrl);
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful download URL retrieval
                        System.out.println(exception);
                    }
                });
            }
        });
    }

    private void openFileChooser() {
        // Create an intent to open the gallery
        Intent iGallary = new Intent(Intent.ACTION_PICK);
        // Set the data type for the intent to images
        iGallary.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the activity using this intent and return the result
        startActivityForResult(iGallary, PICK_IMAGE_REQUEST);
    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Step 1: Check if the request code and result code are matching
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Step 2: Get the image uri from the data
            mImageUri = data.getData();
            // Step 3: Display the image in the image view
            profile_image.setImageURI(mImageUri);
        }
    }

}