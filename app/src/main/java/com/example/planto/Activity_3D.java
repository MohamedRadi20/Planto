package com.example.planto;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.planto.notes.NoteActivity;

public class Activity_3D extends AppCompatActivity {

     Button buttonAR;

    private Handler handlerAnimation = new Handler();
    TextView ThreeD_TextView;
    Animation animation_left , animation_right, fade_in;
    ImageView imgAnimation1;
    ImageView imgAnimation2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3d);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ThreeD_TextView = findViewById(R.id.ThreeD_TextView);


        imgAnimation1 = findViewById(R.id.imgAnimation1);
        imgAnimation2 = findViewById(R.id.imgAnimation2);
        buttonAR = findViewById(R.id.buttonAR);

        startPulse();
        buttonAR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = getPackageManager().getLaunchIntentForPackage("com.salma.MultiTargetPlants");
                startActivity(i);
            }
        });

        animation_left = AnimationUtils.loadAnimation(this, R.anim.left);
        animation_right = AnimationUtils.loadAnimation(this, R.anim.right);

        ThreeD_TextView.setAnimation(animation_left);
    }

    private void startPulse() {
        runnable.run();
    }

    // TODO circle.xml   round_button_blue.xml   color/round_button_clicked.xml
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            imgAnimation1.animate().scaleX(4f).scaleY(4f).alpha(0f).setDuration(1000)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            imgAnimation1.setScaleX(1f);
                            imgAnimation1.setScaleY(1f);
                            imgAnimation1.setAlpha(1f);
                        }
                    });

            imgAnimation2.animate().scaleX(4f).scaleY(4f).alpha(0f).setDuration(700)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            imgAnimation2.setScaleX(1f);
                            imgAnimation2.setScaleY(1f);
                            imgAnimation2.setAlpha(1f);
                        }
                    });

            handlerAnimation.postDelayed(this, 1500);
        }
    };



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
            case R.id.note:
                intent = new Intent(getApplicationContext(), NoteActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}