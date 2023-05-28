package com.example.planto;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
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

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class Diagnose_Plant_Activity extends AppCompatActivity {
    private final int GALLERY_RQ_CODE = 1000;
    private Handler handlerAnimation = new Handler();
    private ImageView imgAnimation1;
    private ImageView imgAnimation2;
    private Button button, upload_button;
    Animation animation_left, animation_right, fade_in;
    TextView diagnose_plant_activity_title_textView, diagnose_plant_activity_entry_textView;

    AssetManager assetManager;
    TensorImage tensorImage;
    Interpreter interpreter;

    int image_size_for_the_model = 256;
    String First_Result = "", Second_Result = "", modelPath = "model.tflite";
    String[] classes;
    int maxIndex, secondMaxIndex;
    float maxConfidence = 0, secondMaxConfidence = 0;
    float[] results;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnose_plant);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        upload_button = findViewById(R.id.upload);

        classes = getResources().getStringArray(R.array.plant_disease_classes);

        // I wanna check the damn size here
        try {
            assetManager = getAssets();

            interpreter = loadModel(assetManager, modelPath);

            checkModelInputOutputShapes(interpreter);

        } catch (IOException e) {
            e.printStackTrace();
        }

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

    class Prediction {
        float confidence;
        int index;

        public Prediction(float confidence, int index) {
            this.confidence = confidence;
            this.index = index;
        }
    }

    private Pair<Prediction, Prediction> argmaxAndSecondMax(float[] array) {
        int maxIndex = 0;
        int secondMaxIndex = 0;
        float maxValue = array[0];
        float secondMaxValue = Float.MIN_VALUE;
        for (int i = 1; i < array.length; i++) {
            if (array[i] > maxValue) {
                secondMaxValue = maxValue;
                secondMaxIndex = maxIndex;
                maxValue = array[i];
                maxIndex = i;
            } else if (array[i] > secondMaxValue) {
                secondMaxValue = array[i];
                secondMaxIndex = i;
            }
        }
        Prediction maxPrediction = new Prediction(maxValue, maxIndex);
        Prediction secondMaxPrediction = new Prediction(secondMaxValue, secondMaxIndex);
        return new Pair<>(maxPrediction, secondMaxPrediction);
    }

    private Interpreter loadModel(AssetManager assetManager, String modelPath) throws IOException {
        AssetFileDescriptor fileDescriptor = assetManager.openFd(modelPath);
        FileInputStream fileInputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = fileInputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        MappedByteBuffer modelBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
        return new Interpreter(modelBuffer);
    }

    private void checkModelInputOutputShapes(Interpreter interpreter) {
        int inputTensorCount = interpreter.getInputTensorCount();
        int outputTensorCount = interpreter.getOutputTensorCount();

        for (int i = 0; i < inputTensorCount; i++) {
            int[] inputShape = interpreter.getInputTensor(i).shape();
            String inputShapeString = arrayToString(inputShape);
            System.out.println("Input Tensor " + i + " shape: " + inputShapeString);
        }

        for (int i = 0; i < outputTensorCount; i++) {
            int[] outputShape = interpreter.getOutputTensor(i).shape();
            String outputShapeString = arrayToString(outputShape);
            System.out.println("Output Tensor " + i + " shape: " + outputShapeString);
        }
    }

    private String arrayToString(int[] array) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (int i = 0; i < array.length; i++) {
            builder.append(array[i]);
            if (i < array.length - 1) {
                builder.append(", ");
            }
        }
        builder.append("]");
        return builder.toString();
    }

    private TensorImage preprocessImage(Bitmap image, int inputImageSize) {
        ImageProcessor imageProcessor = new ImageProcessor.Builder()
                .add(new ResizeWithCropOrPadOp(inputImageSize, inputImageSize))
                .build();
        TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
        tensorImage.load(image);
        tensorImage = imageProcessor.process(tensorImage);
        return tensorImage;
    }

    private float[] performInference(Interpreter interpreter, TensorImage inputImage) {
        int inputTensorIndex = 0;
        int outputTensorIndex = 0;

        int[] inputShape = interpreter.getInputTensor(inputTensorIndex).shape();
        int[] outputShape = interpreter.getOutputTensor(outputTensorIndex).shape();

        TensorBuffer inputBuffer = TensorBuffer.createFixedSize(inputShape, DataType.FLOAT32);
        TensorBuffer outputBuffer = TensorBuffer.createFixedSize(outputShape, DataType.FLOAT32);

        inputBuffer.loadBuffer(inputImage.getBuffer());

        interpreter.run(inputBuffer.getBuffer(), outputBuffer.getBuffer());

        float[] results = outputBuffer.getFloatArray();
        return results;
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
                Bitmap image = (Bitmap) data.getExtras().get("data");
                int dimension = Math.min(image.getWidth(), image.getHeight());
                image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);

                image = Bitmap.createScaledBitmap(image, image_size_for_the_model, image_size_for_the_model, false);

                try {
                    interpreter = loadModel(assetManager, modelPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                tensorImage = preprocessImage(image, image_size_for_the_model);

                results = performInference(interpreter, tensorImage);

                Pair<Prediction, Prediction> predictions = argmaxAndSecondMax(results);

                Prediction maxPrediction = predictions.first;
                Prediction secondMaxPrediction = predictions.second;

                maxConfidence = maxPrediction.confidence;
                maxIndex = maxPrediction.index;

                secondMaxConfidence = secondMaxPrediction.confidence;
                secondMaxIndex = secondMaxPrediction.index;

                First_Result = classes[maxIndex];
                Second_Result = classes[secondMaxIndex];

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                Intent intent = new Intent(getApplicationContext(), Diagnose_Plant_Result_Activity.class);
                intent.putExtra("Diagnose_First_Resut", First_Result);
                intent.putExtra("Diagnose_Second_Resut", Second_Result);
                intent.putExtra("maxConfidence", maxConfidence);
                intent.putExtra("secondMaxConfidence", secondMaxConfidence);
                intent.putExtra("image", byteArray);
                startActivity(intent);

            } else {
                Uri dat = data.getData();
                Bitmap image = null;
                try {
                    image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), dat);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                image = Bitmap.createScaledBitmap(image, image_size_for_the_model, image_size_for_the_model, false);

                try {
                    interpreter = loadModel(assetManager, modelPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                tensorImage = preprocessImage(image, image_size_for_the_model);

                results = performInference(interpreter, tensorImage);

                Pair<Prediction, Prediction> predictions = argmaxAndSecondMax(results);

                Prediction maxPrediction = predictions.first;
                Prediction secondMaxPrediction = predictions.second;

                maxConfidence = maxPrediction.confidence;
                maxIndex = maxPrediction.index;

                secondMaxConfidence = secondMaxPrediction.confidence;
                secondMaxIndex = secondMaxPrediction.index;

                First_Result = classes[maxIndex];
                Second_Result = classes[secondMaxIndex];

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                Intent intent = new Intent(getApplicationContext(), Diagnose_Plant_Result_Activity.class);
                intent.putExtra("Diagnose_First_Resut", First_Result);
                intent.putExtra("Diagnose_Second_Resut", Second_Result);
                intent.putExtra("maxConfidence", maxConfidence);
                intent.putExtra("secondMaxConfidence", secondMaxConfidence);
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
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

}
