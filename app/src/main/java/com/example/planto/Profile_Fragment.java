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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    private Uri mImageUri;
    FirebaseDatabase database;
    DatabaseReference reference;
    SharedPreferences sharedPreferences;
    FirebaseStorage storage;
    TextView name;
    ImageView profile_image;
    LinearLayout logout_profile;
    Button update;

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
        name = getActivity().findViewById(R.id.name);
       /* database = FirebaseDatabase.getInstance();
        reference = database.getReference("users").child("Mohamed Khaled");
        sharedPreferences = getActivity().getSharedPreferences("shared", MODE_PRIVATE);
        storage = FirebaseStorage.getInstance();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method will be called once with the value from the database.
                Users user = dataSnapshot.getValue(Users.class);
                Picasso.get().load(user.getProfileImageUrl()).into(profile_image);
                name.setText(user.getName());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });*/
        logout_profile = getActivity().findViewById(R.id.logout_profile);
        logout_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), Login_Activity.class);
                startActivity(intent);
                getActivity().finish();

            }
        });
        update = getActivity().findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //uploadPhoto();
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

        /*if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            StorageReference storageRef = storage.getReference().child("profile");
            StorageReference imageRef = storageRef.child("Mohamed Khaled");
            String filePath = getRealPathFromURI(mImageUri);
            File file = new File(filePath);
            if (!file.exists()) {
                Toast.makeText(getActivity().getApplicationContext(), "file not found", Toast.LENGTH_LONG).show();
                return;
            }
            imageRef.putFile(Uri.fromFile(file)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    System.out.println("done");
                }
            });
        }*/
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