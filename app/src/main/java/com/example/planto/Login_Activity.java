package com.example.planto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login_Activity extends AppCompatActivity {
    FirebaseAuth mAuth;
    EditText email, password;
    String email_, password_;
    TextView registerNow;
    Button submit, googleSignIn;
    TextView error, tv;
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        error = (TextView) findViewById(R.id.error);
        progressBar = (ProgressBar) findViewById(R.id.loading);
        sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);
        tv = findViewById(R.id.tv);
        Paint paint = tv.getPaint();

        Shader shader = paint.setShader(new LinearGradient(0, 0, tv.getPaint().measureText(tv.getText().toString()), tv.getTextSize(),
                new int[]{Color.parseColor("#FF979797"), Color.parseColor("#4CAF50")},
                new float[]{0, 1}, Shader.TileMode.CLAMP));

        if (sharedPreferences.getString("logged", "false").equals("true")) {
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
                email_ = email.getText().toString();
                password_ = password.getText().toString();
                // login
                if (!email_.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email_).matches()) {
                    if (!password_.isEmpty()) {
                        loginUser();
                    } else {
                        password.setError("Password is empty");
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                } else if (email_.isEmpty()) {
                    email.setError("Email is empty");
                    progressBar.setVisibility(View.INVISIBLE);
                } else {
                    email.setError("Enter correct email");
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
        googleSignIn = findViewById(R.id.googleSignIn);
        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    void signIn() {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                navigateToSecondActivity();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "something wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void navigateToSecondActivity() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", account.getEmail());
        editor.apply();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void loginUser() {
        mAuth.signInWithEmailAndPassword(email_, password_)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(), "Logged in successfully", Toast.LENGTH_SHORT).show();
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("email", email_);
                            editor.apply();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }
}