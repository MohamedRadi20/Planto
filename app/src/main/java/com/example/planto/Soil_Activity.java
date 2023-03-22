package com.example.planto;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import pl.droidsonroids.gif.GifImageView;

public class Soil_Activity extends AppCompatActivity {
    ImageView  imageView_dummy , imageView_arrow;
    GifImageView imageView_button ;
    Animation animation_left , animation_right, fade_in;
    TextView textView1 , textView2;
    Button upload_button ;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soil);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imageView_dummy = findViewById(R.id.cameraReasult);
        imageView_button = findViewById(R.id.openCamera);
        imageView_arrow = findViewById(R.id.arrow);

        if (ContextCompat.checkSelfPermission(Soil_Activity.this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Soil_Activity.this,
                    new String[]{Manifest.permission.CAMERA},
                    100);
        }
        imageView_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,100);

            }
        });

        textView1 = findViewById(R.id.cool_tv1);
        textView2 = findViewById(R.id.cool_tv2);
        upload_button = findViewById(R.id.upload);

        animation_left = AnimationUtils.loadAnimation(this,R.anim.left);
        animation_right = AnimationUtils.loadAnimation(this,R.anim.right);
        fade_in = AnimationUtils.loadAnimation(this,R.anim.fade_in);

        textView1.setAnimation(animation_left);
        textView2.setAnimation(animation_left);
        imageView_dummy.setAnimation(animation_right);
        imageView_arrow.setAnimation(fade_in);
        imageView_button.setAnimation(fade_in);
        upload_button.setAnimation(fade_in);

    }

    protected void onActivityResult(int requestCode , int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            Bitmap photo = (Bitmap)data.getExtras().get("data");
            imageView_dummy.setImageBitmap(photo);}
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.feedback:
                Toast.makeText(getApplicationContext(), "Coming soon",
                        Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu,menu);
        return true;
    }
}
