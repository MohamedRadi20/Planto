package com.example.planto;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class postPage extends AppCompatActivity {
    TextView item_text,item_username;
    ImageView item_image,item_profile_image;
    ListView comments_list;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_page);
        item_text = findViewById(R.id.item_text);
        item_image = findViewById(R.id.item_image);
        comments_list = findViewById(R.id.comments_list);
        item_username = findViewById(R.id.item_username);
        item_profile_image = findViewById(R.id.item_profile_image);
        user= FirebaseAuth.getInstance().getCurrentUser();
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            Picasso.get().load(user.getPhotoUrl()).into( item_profile_image);
            Picasso.get().load(extras.getString("postImage")).into( item_image);
            item_username.setText(user.getDisplayName());
            item_text.setText(extras.getString("postText"));
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, extras.getStringArrayList("postComments"));
            comments_list.setAdapter(adapter);
        }

    }
}