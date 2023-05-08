package com.example.planto;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import pl.droidsonroids.gif.GifImageView;

public class Identify_Plants_Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    ImageView imageView_dummy , imageView_arrow;
    GifImageView imageView_button;
    Animation animation_left , animation_right, fade_in;
    TextView textView1 , textView2;
    Button upload_button ;
    SharedPreferences pref;
    Spinner spinner;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify_plants);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinner = findViewById(R.id.action_bar_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.model_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        pref = getApplicationContext().getSharedPreferences("models",MODE_PRIVATE);

        imageView_dummy = findViewById(R.id.cameraReasult);
        imageView_arrow = findViewById(R.id.arrow);

        imageView_button =findViewById(R.id.ar_camera_button);
        imageView_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Camera_Activity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
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
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        if(text.equals("Fruits")){
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("isFruits",true);
            editor.commit();
            Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
        }else{
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("isFruits",false);
            editor.commit();
            Toast.makeText(parent.getContext(), text + " fuck", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
