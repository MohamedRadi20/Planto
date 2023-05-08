package com.example.planto;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class postPage extends AppCompatActivity {
    ArrayList<String> comments;
    TextView item_text,item_username,comments_count;
    ImageView item_image,item_profile_image,add_comment_button;
    ListView comments_list;
    EditText comment_text;
    FirebaseUser user;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_page);
        item_text = findViewById(R.id.item_text);
        item_image = findViewById(R.id.item_image);
        comments_list = findViewById(R.id.comments_list);
        item_username = findViewById(R.id.item_username);
        item_profile_image = findViewById(R.id.item_profile_image);
        add_comment_button = findViewById(R.id.add_comment_button);
        comment_text = findViewById(R.id.comment_text);
        comments_count= findViewById(R.id.comments_count);
        add_comment_button.setOnClickListener(v -> {
            comments.add(comment_text.getText().toString());
            db.collection("posts").document(getIntent().getExtras().getString("postID")).update("comments",comments);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, comments);
            comments_list.setAdapter(adapter);
        });
        user= FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            comments = extras.getStringArrayList("postComments");
            comments_count.setText(String.valueOf(comments.size()));
            Picasso.get().load(user.getPhotoUrl()).into( item_profile_image);
            Picasso.get().load(extras.getString("postImage")).into( item_image);
            item_username.setText(user.getDisplayName());
            item_text.setText(extras.getString("postText"));
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, comments);
            comments_list.setAdapter(adapter);
        }

    }
}