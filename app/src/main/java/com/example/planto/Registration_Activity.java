package com.example.planto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.checkerframework.common.reflection.qual.NewInstance;

import java.util.HashMap;
import java.util.Map;

public class Registration_Activity extends AppCompatActivity {

    TextInputEditText name , email , password ;
    Button submit ;
    TextView error , tv, signNow ;
    ProgressBar progressBar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        name = (TextInputEditText) findViewById(R.id.name);
        email = (TextInputEditText) findViewById(R.id.email);
        password = (TextInputEditText) findViewById(R.id.password);
        error = (TextView) findViewById(R.id.error);
        progressBar = (ProgressBar) findViewById(R.id.loading);

        tv = findViewById(R.id.tv);
        Paint paint = tv.getPaint();

        Shader shader = paint.setShader(new LinearGradient(0, 0, tv.getPaint().measureText(tv.getText().toString()), tv.getTextSize(),
                new int[]{Color.parseColor("#FF979797"), Color.parseColor("#1B6354")},
                new float[]{0, 1}, Shader.TileMode.CLAMP));

        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        signNow = findViewById(R.id.signNow);
        signNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent intent = new Intent(getApplicationContext(), Login_Activity.class);
              startActivity(intent);
              finish();
            }
        });
    }

}