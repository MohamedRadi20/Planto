package com.example.planto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.planto.notes.NoteActivity;

public class Diagnose_Plant_Result_Activity extends AppCompatActivity {
    private ProgressBar progressBar;
    private TextView textViewProgress;
    Button search_button;
    ImageView imageView_dummy ;
    TextView textView_result , textView_description;
    String accumulation_of_the_result = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnose_plant_result);
        Log.e("TAG","fuck final_v1");

        progressBar = findViewById(R.id.progress_bar);
        textViewProgress = findViewById(R.id.text_view_progress);
        search_button = findViewById(R.id.search);
        imageView_dummy = findViewById(R.id.cameraReasult);
        textView_result = findViewById(R.id.textView_result);
        textView_description = findViewById(R.id.textView_description);

        byte[] byteArray = getIntent().getByteArrayExtra("image");
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        String diagnose_first_result = getIntent().getStringExtra("Diagnose_First_Resut");
        float maxConfidence = getIntent().getFloatExtra("maxConfidence",0);
        long inferenceTime = getIntent().getLongExtra("inferenceTime",0);
        Log.e("TAG","fuck final_v2");



        maxConfidence = maxConfidence * 100;

        String formattedMaxConfidence = String.format("%.2f", maxConfidence);

        progressBar.setProgress(0);

        progressBar.setMax(100);

        ObjectAnimator animator = ObjectAnimator.ofInt(progressBar, "progress", 0, (int) maxConfidence);
        animator.setDuration(2000);
        animator.start();

        double inferenceTime_seconds = (double) inferenceTime/1000.0;

        textViewProgress.setText(formattedMaxConfidence + "%");

        String resourceName = diagnose_first_result ;
        int resourceId = getResources().getIdentifier(resourceName, "string", getPackageName());
        String description = getString(resourceId);
        textView_description.setText(Html.fromHtml(description));


        accumulation_of_the_result += String.format("%s: \n",diagnose_first_result);
        accumulation_of_the_result += "inferenceTime_seconds: " + inferenceTime_seconds;

            if(diagnose_first_result != ""){
                textView_result.setText(accumulation_of_the_result);
            }

        imageView_dummy.setImageBitmap(bitmap);

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Search_Activity.class);
                startActivity(intent);
            }
        });
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