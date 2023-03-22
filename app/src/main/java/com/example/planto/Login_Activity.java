package com.example.planto;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class Login_Activity extends AppCompatActivity {
    TextInputEditText email , password ;
    TextView registerNow ;
    Button submit ;
    TextView error , tv ;
    ProgressBar progressBar ;
    SharedPreferences sharedPreferences ;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = (TextInputEditText) findViewById(R.id.email);
        password = (TextInputEditText) findViewById(R.id.password);
        error = (TextView) findViewById(R.id.error);
        progressBar = (ProgressBar) findViewById(R.id.loading);
        sharedPreferences = getSharedPreferences("shared",MODE_PRIVATE);

        tv = findViewById(R.id.tv);
        Paint paint = tv.getPaint();

        Shader shader = paint.setShader(new LinearGradient(0, 0, tv.getPaint().measureText(tv.getText().toString()), tv.getTextSize(),
                new int[]{Color.parseColor("#FF979797"), Color.parseColor("#1B6354")},
                new float[]{0, 1}, Shader.TileMode.CLAMP));

        if(sharedPreferences.getString("logged","false").equals("true")){
            Toast.makeText(getApplicationContext(), "Logged in successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            finish();
            startActivity(intent);
        }
        registerNow = findViewById(R.id.registerNow);
        registerNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Registration_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        submit = (Button) findViewById(R.id.submit2);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                error.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Logged in successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();

            }});

    }}