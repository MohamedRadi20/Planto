package com.example.planto;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
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

import com.example.planto.notes.NoteActivity;

import pl.droidsonroids.gif.GifImageView;

public class Identify_Plants_Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Handler handlerAnimation = new Handler();
    private ImageView ar_imgAnimation1, ar_imgAnimation2;
    private Button ar_camera_button;
    Animation animation_left, animation_right, fade_in;
    TextView textView1, textView2;
    SharedPreferences pref;
    Spinner spinner;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify_plants);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        textView1 = findViewById(R.id.cool_tv1);
        textView2 = findViewById(R.id.cool_tv2);
        ar_imgAnimation1 = findViewById(R.id.ar_imgAnimation1);
        ar_imgAnimation2 = findViewById(R.id.ar_imgAnimation2);
        ar_camera_button = findViewById(R.id.ar_camera_button);
        spinner = findViewById(R.id.action_bar_spinner);


        CharSequence[] items = getResources().getTextArray(R.array.model_spinner);
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this, R.layout.custom_spinner_item, items);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        pref = getApplicationContext().getSharedPreferences("the_ssd_models", MODE_PRIVATE);

        ar_camera_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Camera_Activity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        animation_left = AnimationUtils.loadAnimation(this, R.anim.left);
        animation_right = AnimationUtils.loadAnimation(this, R.anim.right);

        textView1.setAnimation(animation_left);
        textView2.setAnimation(animation_left);

        startPulse();

    }

    private void startPulse() {
        runnable.run();
    }

    // TODO circle.xml   round_button_blue.xml   color/round_button_clicked.xml

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            ar_imgAnimation1.animate().scaleX(4f).scaleY(4f).alpha(0f).setDuration(1000)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            ar_imgAnimation1.setScaleX(1f);
                            ar_imgAnimation1.setScaleY(1f);
                            ar_imgAnimation1.setAlpha(1f);
                        }
                    });

            ar_imgAnimation2.animate().scaleX(4f).scaleY(4f).alpha(0f).setDuration(700)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            ar_imgAnimation2.setScaleX(1f);
                            ar_imgAnimation2.setScaleY(1f);
                            ar_imgAnimation2.setAlpha(1f);
                        }
                    });

            handlerAnimation.postDelayed(this, 1500);
        }
    };

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        if (text.equals("Fruits")) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("isFruits", true);
            editor.putBoolean("isOrnamentals", false);
            editor.putBoolean("isFlowers", false);
            editor.putBoolean("isPests", false);
            editor.commit();
            Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
        } else if (text.equals("Ornamentals")) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("isFruits", false);
            editor.putBoolean("isOrnamentals", true);
            editor.putBoolean("isFlowers", false);
            editor.putBoolean("isPests", false);
            editor.commit();
            Toast.makeText(parent.getContext(), text + " fuck", Toast.LENGTH_SHORT).show();
        } else if (text.equals("Flowers")) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("isFruits", false);
            editor.putBoolean("isOrnamentals", false);
            editor.putBoolean("isFlowers", true);
            editor.putBoolean("isPests", false);
            editor.commit();
            Toast.makeText(parent.getContext(), text + " fuck", Toast.LENGTH_SHORT).show();
        } else if (text.equals("Pests")) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("isFruits", false);
            editor.putBoolean("isOrnamentals", false);
            editor.putBoolean("isFlowers", false);
            editor.putBoolean("isPests", true);
            editor.commit();
            Toast.makeText(parent.getContext(), text + " fuck", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
            case R.id.note:
                intent = new Intent(getApplicationContext(), NoteActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

}
