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

public class Diagnose_Plant_Activity extends AppCompatActivity {
    private final int GALLERY_RQ_CODE = 1000;
    ImageButton imageButton ;
    ImageView imageView;
    Button buttonUpload;
    Animation left , right ;
    TextView textView1 , textView2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnose_plant);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imageView = findViewById(R.id.cameraReasult);
        imageButton = findViewById(R.id.openCamera);
        buttonUpload = findViewById(R.id.upload);

        if (ContextCompat.checkSelfPermission(Diagnose_Plant_Activity.this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Diagnose_Plant_Activity.this,
                    new String[]{Manifest.permission.CAMERA},
                    100);
        }
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,100);

            }
        });
        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iGallary = new Intent(Intent.ACTION_PICK);
                iGallary.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallary , GALLERY_RQ_CODE);
            }
        });
        textView1 = findViewById(R.id.cool_tv1);
        textView2 = findViewById(R.id.cool_tv2);
        left = AnimationUtils.loadAnimation(this,R.anim.left);
        right = AnimationUtils.loadAnimation(this,R.anim.right);
        textView1.setAnimation(left);
        textView2.setAnimation(left);
        imageView.setAnimation(right);

    }

    protected void onActivityResult(int requestCode , int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
        Bitmap photo = (Bitmap)data.getExtras().get("data");
        imageView.setImageBitmap(photo);
        }else if ( requestCode == GALLERY_RQ_CODE ){
            imageView.setImageURI(data.getData());
        }
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
