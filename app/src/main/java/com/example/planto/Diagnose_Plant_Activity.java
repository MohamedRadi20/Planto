package com.example.planto;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.planto.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Diagnose_Plant_Activity extends AppCompatActivity {
    private final int GALLERY_RQ_CODE = 1000;
    private Handler handlerAnimation = new Handler();
    private ImageView imgAnimation1;
    private ImageView imgAnimation2;
    private Button button;
    Animation animation_left , animation_right, fade_in;
    Button upload_button ;
    TextView diagnose_plant_activity_title_textView , diagnose_plant_activity_entry_textView;
    int image_size_for_the_model = 128;
    String First_Result = "", Second_Result = "" ;
    float maxConfidence = 0 , secondMaxConfidence = 0 ;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnose_plant);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        upload_button = findViewById(R.id.upload);


        if (ContextCompat.checkSelfPermission(Diagnose_Plant_Activity.this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Diagnose_Plant_Activity.this,
                    new String[]{Manifest.permission.CAMERA},
                    100);
        }

        upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iGallary = new Intent(Intent.ACTION_PICK);
                iGallary.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallary , GALLERY_RQ_CODE);
            }
        });

        diagnose_plant_activity_title_textView = findViewById(R.id.diagnose_plant_activity_title_textView);
        diagnose_plant_activity_entry_textView = findViewById(R.id.diagnose_plant_activity_entry_textView);
        upload_button = findViewById(R.id.upload);

        animation_left = AnimationUtils.loadAnimation(this,R.anim.left);
        animation_right = AnimationUtils.loadAnimation(this,R.anim.right);
        fade_in = AnimationUtils.loadAnimation(this,R.anim.fade_in);

        diagnose_plant_activity_title_textView.setAnimation(animation_left);
        diagnose_plant_activity_entry_textView.setAnimation(animation_left);
        upload_button.setAnimation(fade_in);


        imgAnimation1 = findViewById(R.id.imgAnimation1);
        imgAnimation2 = findViewById(R.id.imgAnimation2);
        button = findViewById(R.id.button);

        startPulse();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,3);
            }
        });

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

    public void classifyImage(Bitmap image){
        try {
            Model model = Model.newInstance(getApplicationContext());

            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 128, 128, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * image_size_for_the_model * image_size_for_the_model * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[image_size_for_the_model * image_size_for_the_model];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
            int pixel = 0;

            for(int i = 0; i < image_size_for_the_model; i ++){
                for(int j = 0; j < image_size_for_the_model; j++){
                    int val = intValues[pixel++]; // RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 1));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 1));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 1));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            int maxPos = 0 , secondMaxPos = 0 ;

            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }

            for (int i = 0; i < confidences.length; i++) {
                if ((confidences[i] > secondMaxConfidence)&(confidences[i] != maxConfidence)) {
                    secondMaxConfidence = confidences[i] ;
                    secondMaxPos = i;
                }
            }

            String[] classes = getResources().getStringArray(R.array.plant_disease_classes);
            First_Result = classes[maxPos];
            Second_Result = classes[secondMaxPos];

            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == 3){
                Bitmap image = (Bitmap) data.getExtras().get("data");
                int dimension = Math.min(image.getWidth(), image.getHeight());
                image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);

                image = Bitmap.createScaledBitmap(image, image_size_for_the_model, image_size_for_the_model, false);
                classifyImage(image);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                Intent intent = new Intent(getApplicationContext(), Diagnose_Plant_Result_Activity.class);
                intent.putExtra("Diagnose_First_Resut",First_Result);
                intent.putExtra("Diagnose_Second_Resut",Second_Result);
                intent.putExtra("maxConfidence",maxConfidence);
                intent.putExtra("secondMaxConfidence",secondMaxConfidence);
                intent.putExtra("image", byteArray);
                startActivity(intent);

            }else{
                Uri dat = data.getData();
                Bitmap image = null;
                try {
                    image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), dat);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                image = Bitmap.createScaledBitmap(image, image_size_for_the_model, image_size_for_the_model, false);
                classifyImage(image);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                Intent intent = new Intent(getApplicationContext(), Diagnose_Plant_Result_Activity.class);
                intent.putExtra("Diagnose_First_Resut",First_Result);
                intent.putExtra("Diagnose_Second_Resut",Second_Result);
                intent.putExtra("maxConfidence",maxConfidence);
                intent.putExtra("secondMaxConfidence",secondMaxConfidence);
                intent.putExtra("image", byteArray);
                startActivity(intent);

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
