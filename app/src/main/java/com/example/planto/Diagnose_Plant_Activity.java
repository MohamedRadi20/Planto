package com.example.planto;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import com.example.planto.notes.NoteActivity;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class Diagnose_Plant_Activity extends AppCompatActivity {
    private final int GALLERY_RQ_CODE = 1000;
    private Handler handlerAnimation = new Handler();
    private ImageView imgAnimation1;
    private ImageView imgAnimation2;
    private Button button, upload_button;
    Animation animation_left, animation_right, fade_in;
    TextView diagnose_plant_activity_title_textView, diagnose_plant_activity_entry_textView;

    Interpreter interpreter;

    private static final int PICK_IMAGE_REQUEST = 1;
    private int inputShape[];
    private DataType inputDataType, outputDataType;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnose_plant);
        upload_button = findViewById(R.id.upload);


        if (ContextCompat.checkSelfPermission(Diagnose_Plant_Activity.this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Diagnose_Plant_Activity.this,
                    new String[]{Manifest.permission.CAMERA},
                    100);
        }

        upload_button.setOnClickListener(view -> {
            Intent iGallary = new Intent(Intent.ACTION_PICK);
            iGallary.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(iGallary, GALLERY_RQ_CODE);
        });

        diagnose_plant_activity_title_textView = findViewById(R.id.diagnose_plant_activity_title_textView);
        diagnose_plant_activity_entry_textView = findViewById(R.id.diagnose_plant_activity_entry_textView);
        upload_button = findViewById(R.id.upload);

        animation_left = AnimationUtils.loadAnimation(this, R.anim.left);
        animation_right = AnimationUtils.loadAnimation(this, R.anim.right);
        fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        diagnose_plant_activity_title_textView.setAnimation(animation_left);
        diagnose_plant_activity_entry_textView.setAnimation(animation_left);
        upload_button.setAnimation(fade_in);


        imgAnimation1 = findViewById(R.id.imgAnimation1);
        imgAnimation2 = findViewById(R.id.imgAnimation2);
        button = findViewById(R.id.button);

        startPulse();

        button.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 3);
        });


    }


    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor assetFileDescriptor = getAssets().openFd("model.tflite");
        FileInputStream inputStream = new FileInputStream(assetFileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = assetFileDescriptor.getStartOffset();
        long declaredLength = assetFileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private float[][][][] preprocess(Bitmap imageBitmap) {
        // Resize the image
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, inputShape[2], inputShape[1], true);

        // Convert the bitmap to a float array
        int[] intValues = new int[inputShape[1] * inputShape[2]];
        float[][][][] floatValues = new float[1][inputShape[1]][inputShape[2]][3];
        resizedBitmap.getPixels(intValues, 0, inputShape[2], 0, 0, inputShape[2], inputShape[1]);
        for (int i = 0; i < intValues.length; ++i) {
            final int val = intValues[i];
            floatValues[0][i / inputShape[2]][i % inputShape[2]][0] = ((val >> 16) & 0xFF) / 255.0f;
            floatValues[0][i / inputShape[2]][i % inputShape[2]][1] = ((val >> 8) & 0xFF) / 255.0f;
            floatValues[0][i / inputShape[2]][i % inputShape[2]][2] = (val & 0xFF) / 255.0f;
        }

        return floatValues;
    }

    private int argmax(float[] array) {
        int maxIndex = 0;
        float maxVal = -Float.MAX_VALUE;
        for (int i = 0; i < array.length; i++) {
            if (array[i] > maxVal) {
                maxVal = array[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    private void startPulse() {
        runnable.run();
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            imgAnimation1.animate().scaleX(4f).scaleY(4f).alpha(0f).setDuration(1000)
                    .withEndAction(() -> {
                        imgAnimation1.setScaleX(1f);
                        imgAnimation1.setScaleY(1f);
                        imgAnimation1.setAlpha(1f);
                    });

            imgAnimation2.animate().scaleX(4f).scaleY(4f).alpha(0f).setDuration(700)
                    .withEndAction(() -> {
                        imgAnimation2.setScaleX(1f);
                        imgAnimation2.setScaleY(1f);
                        imgAnimation2.setAlpha(1f);
                    });

            handlerAnimation.postDelayed(this, 1500);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 3) {
                long inferenceTime = SystemClock.uptimeMillis();

                Bitmap image = (Bitmap) data.getExtras().get("data");

                // Load model
                try {
                    interpreter = new Interpreter(loadModelFile(), new Interpreter.Options());
                } catch (IOException e) {
                    Log.e("TAG", "Failed to load model", e);
                }

                // Get input and output details
                inputShape = interpreter.getInputTensor(0).shape();
                inputDataType = interpreter.getInputTensor(0).dataType();
                outputDataType = interpreter.getOutputTensor(0).dataType();

                // Create a list of class names
                // Load the class names from the raw resource
                String[] classNames = null;
                try {
                    InputStream inputStream = getResources().openRawResource(R.raw.class_names);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    List<String> lines = new ArrayList<>();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        lines.add(line);
                    }
                    classNames = lines.toArray(new String[0]);
                } catch (IOException e) {
                    Log.e("TAG", "Failed to load class names", e);
                }
                // Load the image from the drawable resource

                // Preprocess the image
                float[][][][] inputArray = preprocess(image);

                // Invoke the interpreter to predict the image
                float[][] outputArray = new float[1][89];
                interpreter.run(inputArray, outputArray);

                // Get the predicted class index and probability
                int classIndex = argmax(outputArray[0]);
                String className = classNames[classIndex];
                float probability = outputArray[0][classIndex];
                Log.e("TAG", "Class name: " + className + ", Probability: " + probability);


                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                inferenceTime = SystemClock.uptimeMillis() - inferenceTime;

                Intent intent = new Intent(getApplicationContext(), Diagnose_Plant_Result_Activity.class);
                intent.putExtra("Diagnose_First_Resut", className);
                intent.putExtra("maxConfidence", probability);
                intent.putExtra("image", byteArray);
                intent.putExtra("inferenceTime", inferenceTime);
                Log.e("TAG","fuck final");
                startActivity(intent);

            } else {
                long inferenceTime = SystemClock.uptimeMillis();

                Uri dat = data.getData();
                Bitmap image = null;
                try {
                    image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), dat);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Load model
                try {
                    interpreter = new Interpreter(loadModelFile(), new Interpreter.Options());
                } catch (IOException e) {
                    Log.e("TAG", "Failed to load model", e);
                }

                // Get input and output details
                inputShape = interpreter.getInputTensor(0).shape();
                inputDataType = interpreter.getInputTensor(0).dataType();
                outputDataType = interpreter.getOutputTensor(0).dataType();

                // Create a list of class names
                // Load the class names from the raw resource
                String[] classNames = null;
                try {
                    InputStream inputStream = getResources().openRawResource(R.raw.class_names);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    List<String> lines = new ArrayList<>();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        lines.add(line);
                    }
                    classNames = lines.toArray(new String[0]);
                } catch (IOException e) {
                    Log.e("TAG", "Failed to load class names", e);
                }
                // Load the image from the drawable resource

                // Preprocess the image
                float[][][][] inputArray = preprocess(image);

                // Invoke the interpreter to predict the image
                float[][] outputArray = new float[1][89];
                interpreter.run(inputArray, outputArray);

                // Get the predicted class index and probability
                int classIndex = argmax(outputArray[0]);
                String className = classNames[classIndex];
                float probability = outputArray[0][classIndex];

                Log.e("TAG", "Class name: " + className + ", Probability: " + probability);


                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                inferenceTime = SystemClock.uptimeMillis() - inferenceTime;

                Intent intent = new Intent(getApplicationContext(), Diagnose_Plant_Result_Activity.class);
                intent.putExtra("Diagnose_First_Resut", className);
                intent.putExtra("maxConfidence", probability);
                intent.putExtra("image", byteArray);
                intent.putExtra("inferenceTime", inferenceTime);
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
