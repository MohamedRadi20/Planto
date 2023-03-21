package com.example.planto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddPostActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView imageView;
    private EditText mEditText;
    private Uri mImageUri;
    FirebaseStorage storage;
    FirebaseDatabase database;
    DatabaseReference reference;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE);
        imageView = findViewById(R.id.post_image_view);
        mEditText = findViewById(R.id.post_text_edit_text);
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("posts");
        Button addPostButton = findViewById(R.id.add_post_button);
        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPost();
            }
        });
        Button uploadPhoto = findViewById(R.id.upload_photo);
        uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        Button gptButton = findViewById(R.id.gpt_button);
        gptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mEditText.getText().toString().trim();
                if (text.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter post text", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Instantiate the RequestQueue
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                // Set the URL and request method
                String url = "https://api.openai.com/v1/engines/text-davinci-003/completions";
                int method = Request.Method.POST;
                // Set the request headers
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer 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");
                // Create the request body
                Map<String, Object> body = new HashMap<>();
                body.put("prompt", "create a post about " + mEditText.getText().toString() + " with maximum 300 characters");
                body.put("max_tokens", 1000);
                body.put("temperature", 0.8);
                // Convert the request body to JSON
                String requestBody = new Gson().toJson(body);
                // Create a new request and add it to the queue
                JsonObjectRequest request = new JsonObjectRequest(method, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Handle the response here
                                JSONArray choices = null;
                                try {
                                    choices = response.getJSONArray("choices");
                                    JSONObject firstChoice = choices.getJSONObject(0);
                                    String generatedText = firstChoice.getString("text");
                                    mEditText.setText(generatedText);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Handle any errors that occur
                                error.printStackTrace();
                            }
                        }) {
                    @Override
                    public byte[] getBody() {
                        // Convert the request body to a byte array
                        return requestBody.getBytes();
                    }

                    @Override
                    public Map<String, String> getHeaders() {
                        // Return the request headers
                        return headers;
                    }
                };
                queue.add(request);
            }
        });
        Button dall_button = findViewById(R.id.dall_button);
        dall_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "https://api.openai.com/v1/images/generations";

                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("prompt",mEditText.getText().toString() );
                    jsonBody.put("num_images", 1);
                    jsonBody.put("size", "512x512");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                // Handle successful response
                                JSONArray choices = null;
                                try {
                                    choices = response.getJSONArray("data");
                                    JSONObject completions = choices.getJSONObject(0);
                                    String imageURL = completions.getString("url");
                                    Picasso.get().load(imageURL).into( imageView);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Handle error response
                                error.printStackTrace();
                            }
                        }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json");
                        headers.put("Authorization", "Bearer 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");
                        return headers;
                    }
                };

                queue.add(jsonObjectRequest);
            }
        });

    }


    private void openFileChooser() {
        Intent iGallary = new Intent(Intent.ACTION_PICK);
        iGallary.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(iGallary, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            imageView.setImageURI(mImageUri);
        }
    }

    private void addPost() {
        String text = mEditText.getText().toString().trim();
        if (text.isEmpty()) {
            Toast.makeText(this, "Please enter post text", Toast.LENGTH_SHORT).show();
            return;
        }

       /* if (mImageUri == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        }*/
        String newPostId = reference.push().getKey();
        // Save the post to the database or send it to the server
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference().child("posts");

        // Create a reference to "image.jpg"
        StorageReference imageRef = storageRef.child(newPostId);

        // Get the data from an ImageView as bytes
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
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
                        List<String> comments = new ArrayList<>();
                        comments.add("comment1");
                        Post post = new Post(downloadUrl, mEditText.getText().toString(), sharedPreferences.getString("email", "anonymous"), 0, 0, comments);
                        reference.child(newPostId).setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getBaseContext(), "Post added", Toast.LENGTH_SHORT).show();
                            }
                        });
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
        finish();
    }

}