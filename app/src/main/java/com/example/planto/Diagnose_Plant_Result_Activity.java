package com.example.planto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Diagnose_Plant_Result_Activity extends AppCompatActivity {
    Button search_button;
    ImageView imageView_dummy ;
    TextView textView_result , textView_description;
    String accumulation_of_the_result = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnose_plant_result);
        search_button = findViewById(R.id.search);
        imageView_dummy = findViewById(R.id.cameraReasult);
        textView_result = findViewById(R.id.textView_result);
        textView_description = findViewById(R.id.textView_description);

        // Assuming you have a byte array extra called "image" in the Intent
        byte[] byteArray = getIntent().getByteArrayExtra("image");
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        String diagnose_first_result = getIntent().getStringExtra("Diagnose_First_Resut");
        String diagnose_second_result = getIntent().getStringExtra("Diagnose_Second_Resut");
        float maxConfidence = getIntent().getFloatExtra("maxConfidence",0);
        float secondMaxConfidence = getIntent().getFloatExtra("secondMaxConfidence",0);


        // TODO CURE FETCHING AND PROCCESSING
        String description = getString(R.string.Description_1);
        textView_description.setText(Html.fromHtml(description));


        accumulation_of_the_result += String.format("%s:  %.1f%%\n",diagnose_first_result, maxConfidence * 100);
        accumulation_of_the_result += String.format("%s:  %.1f%%\n",diagnose_second_result, secondMaxConfidence * 100);

            if(diagnose_first_result != ""){
                textView_result.setTextSize(12);
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
}