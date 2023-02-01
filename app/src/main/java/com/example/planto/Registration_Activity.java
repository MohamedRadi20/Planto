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

import java.util.HashMap;
import java.util.Map;

public class Registration_Activity extends AppCompatActivity {

    TextInputEditText name , email , password ;
    Button submit ;
    String name_ , email_ , password_ ;
    TextView error , tv , signNow;
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
                new int[]{Color.parseColor("#FF979797"), Color.parseColor("#4CAF50")},
                new float[]{0, 1}, Shader.TileMode.CLAMP));

        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                error.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                name_ = String.valueOf(name.getText());
                email_ = String.valueOf(email.getText());
                password_ = String.valueOf(password.getText());
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url ="http://localhost/login-registration-android/register.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressBar.setVisibility(View.GONE);
                                if(response.equals("success")){
                                    Toast.makeText(getApplicationContext(), "Registered successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), Login_Activity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else {
                                    error.setText(response);
                                    error.setVisibility(View.VISIBLE);
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error_) {
                        progressBar.setVisibility(View.GONE);
                        error.setText(error_.getLocalizedMessage());
                        error.setVisibility(View.VISIBLE);
                    }
                }){
                    protected Map<String, String> getParams(){
                        Map<String, String> paramV = new HashMap<>();
                        paramV.put("name", name_);
                        paramV.put("email", email_);
                        paramV.put("password", password_);
                        return paramV;
                    }
                };
                queue.add(stringRequest);
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