package com.example.planto;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.planto.notes.NoteActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.opencv.android.OpenCVLoader;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;

    static {
        if (OpenCVLoader.initDebug()) {
            Log.d("bla", "uhhh");

        } else {

            Log.d("bla", "fuck");
        }

    }

    public void plant_disease_service(View view) {
        Intent intent = new Intent(getApplicationContext(), Diagnose_Plant_Activity.class);
        startActivity(intent);
    }

    public void identify_plant_service(View view) {
        Intent intent = new Intent(getApplicationContext(), Identify_Plants_Activity.class);
        startActivity(intent);
    }

    public void separate_seeds_service(View view) {
        Intent intent = new Intent(getApplicationContext(), Soil_Activity.class);
        startActivity(intent);
    }

    public void ThreeD_AR(View view) {
        Intent intent = new Intent(getApplicationContext(), Activity_3D.class);
        startActivity(intent);
    }

    public void Plantune_service(View view) {
        Intent intent = new Intent(getApplicationContext(), Plantune_Activity.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@Nullable MenuItem item) {

        switch (item.getItemId()) {
            case R.id.help:

            case R.id.feedback:
                Toast.makeText(getApplicationContext(), "Coming soon",
                        Toast.LENGTH_SHORT).show();
                return true;
            case R.id.note:
                Intent intent = new Intent(getApplicationContext(), NoteActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        NavController navController = Navigation.findNavController(this, R.id.fragmentContainerView);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.firstFragment, R.id.secondFragment, R.id.thirdFragment, R.id.fourthFragment, R.id.fifthFragment).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
        }

    }

    protected void onStart() {
        super.onStart();
//        Log.e("bla",restorePrefData()+"");
//        if (restorePrefData() == false) {
//            Intent mainActivity = new Intent(getApplicationContext(),Intro_Activity.class );
//            startActivity(mainActivity);
//        }
//        FirebaseUser user = mAuth.getCurrentUser();
//        if (user == null) {
//            Intent intent = new Intent(getApplicationContext(),Login_Activity.class);
//            startActivity(intent);
//        }
    }

    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        Boolean isIntroActivityOpnendBefore = pref.getBoolean("isIntroOpnend",false);
        return  isIntroActivityOpnendBefore;
    }
}