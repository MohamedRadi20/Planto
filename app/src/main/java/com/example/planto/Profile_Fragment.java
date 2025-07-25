package com.example.planto;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class Profile_Fragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    FirebaseAuth mAuth;
    User user;
    FirebaseFirestore db;
    FirebaseUser firebaseUser;
    TextView name;
    ImageView profile_image;
    LinearLayout logout_profile, resetPassword, update_profile;
    GoogleSignInAccount account;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);

    }

    public void onStart() {
        super.onStart();
        profile_image = getActivity().findViewById(R.id.profile_image);
        name = getActivity().findViewById(R.id.userName);
        account = GoogleSignIn.getLastSignedInAccount(getActivity());
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        db.collection("users").document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<com.google.firebase.firestore.DocumentSnapshot>() {
            @Override
            public void onSuccess(com.google.firebase.firestore.DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(User.class);
                if (user != null) {
                    name.setText(user.getUsername());
                    Picasso.get().load(user.getAvatar_url()).into(profile_image);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
               Toast.makeText(getActivity().getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
        logout_profile = getActivity().findViewById(R.id.logout_profile);
        logout_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity().getApplicationContext(), Login_Activity.class);
                startActivity(intent);
                getActivity().finish();

            }
        });
        update_profile = getActivity().findViewById(R.id.update_profile);
        update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), updateProfile.class);
                startActivity(intent);
            }
        });
        resetPassword = getActivity().findViewById(R.id.resetPassword);
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.sendPasswordResetEmail(user.getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getActivity().getApplicationContext(), "Reset password email sent", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void uploadPhoto() {
        Intent iGallary = new Intent(Intent.ACTION_PICK);
        iGallary.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(iGallary, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    private String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);

        if (cursor == null) {
            return uri.getPath();
        }

        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        if (cursor.moveToFirst()) {
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            return filePath;
        } else {
            cursor.close();
            return uri.getPath();
        }
    }

}