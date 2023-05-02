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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
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
    FirebaseUser user;
    FirebaseStorage storage;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        imageView = findViewById(R.id.post_image_view);
        mEditText = findViewById(R.id.post_text_edit_text);
        user = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();
        Button addPostButton = findViewById(R.id.add_post_button);
        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPost();
            }
        });
        Button uploadPhoto = (Button) findViewById(R.id.upload_photo);
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
                    jsonBody.put("prompt", mEditText.getText().toString());
                    jsonBody.put("num_images", 1);
                    jsonBody.put("size", "256x256");
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
                                    Picasso.get().load(imageURL).into(imageView);
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

                // Set timeout duration
                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                        10000, // Timeout in milliseconds
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));

                // Add the request to the RequestQueue
                queue.add(jsonObjectRequest);

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
        String newPostId = db.collection("posts").document().getId();
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
                        ArrayList<String> comments = new ArrayList<>();
                        comments.add("comment1");
                        Post post = new Post(downloadUrl, mEditText.getText().toString(), user.getEmail(), 0, 0, comments);
                        db.collection("posts").document(newPostId).set(post).addOnCompleteListener(new OnCompleteListener<Void>() {
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