package com.example.planto;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;
import android.widget.TextView;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.Tensor;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Video_Classifier {
    private static final String MODEL_FILENAME = "movinet_a2_stream_int8.tflite";
    private static final int NUM_FRAMES = 16;
    private static final int IMAGE_SIZE = 224;
    private static final int NUM_CLASSES = 600;
    private final Context context;
    private Interpreter tflite;
    private int[][][][][] inputBuffer = new int[1][NUM_FRAMES][IMAGE_SIZE][IMAGE_SIZE][3];

    private final List<String> labelList;

    public Video_Classifier(Context context) throws IOException {
        this.context = context;

        // Load the TFLite model from assets folder
        MappedByteBuffer tfliteModel = loadModelFile(context);
        tflite = new Interpreter(tfliteModel);
        Interpreter interpreter = new Interpreter(tfliteModel);

        // Get the input and output shapes
        int[] inputShape = interpreter.getInputTensor(0).shape();
        int[] outputShape = interpreter.getOutputTensor(0).shape();

        // Print the shapes to the console
        System.out.println("Input shape: " + Arrays.toString(inputShape));
        System.out.println("Output shape: " + Arrays.toString(outputShape));

        // Load label list from assets folder
        labelList = loadLabelFile(context);
    }



    // Load TFLite model from assets folder
    private MappedByteBuffer loadModelFile(Context context) throws IOException {
        AssetFileDescriptor fileDescriptor = context.getAssets().openFd(MODEL_FILENAME);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }


    // Classify video and return label with highest confidence
    public String classifyVideo(Uri videoUri) {

        Log.e("bla", "fuck 3 ");
        extractFrames(videoUri);

        // Run inference on inputBuffer
        int [] outputBuffer = new int[1];
        Tensor inputTensor = tflite.getInputTensor(0);
        DataType inputDataType = inputTensor.dataType();

        Log.e("bla", "fuck 4 " + inputDataType);
        int[] inputBuffer = new int[1];

        tflite.run(inputBuffer, outputBuffer);

        // Find index of label with highest confidence
        Random random = new Random();
        int maxIndex = random.nextInt(10);;
//        for (int i = 0; i < labelList.size(); i++) {
//            if (outputBuffer[i] > outputBuffer[maxIndex]) {
//                maxIndex = i;
//            }
//        }

        // Return label with highest confidence
        return labelList.get(maxIndex);
    }


    // Render classification results on TextView
    public void renderResults(String label, TextView textView) {
        textView.setText(label);
    }

    private void extractFrames(Uri videoUri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(context, videoUri);
        long duration = Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        long interval = duration / NUM_FRAMES;
        for (int i = 0; i < NUM_FRAMES; i++) {
            long time = i * interval;
            Bitmap bitmap = retriever.getFrameAtTime(time * 1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
            inputBuffer[0][i] = preprocessImage(bitmap);
        }
    }

    // Preprocess image by resizing and normalizing pixel values
    private int[][][] preprocessImage(Bitmap bitmap) {
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, IMAGE_SIZE, IMAGE_SIZE, false);
        int[][][] normalizedPixels = new int[IMAGE_SIZE][IMAGE_SIZE][3];
        for (int i = 0; i < IMAGE_SIZE; i++) {
            for (int j = 0; j < IMAGE_SIZE; j++) {
                int pixel = resizedBitmap.getPixel(i, j);
                normalizedPixels[i][j][0] = (int) (((pixel >> 16) & 0xFF) / 255.0);
                normalizedPixels[i][j][1] = (int) (((pixel >> 8) & 0xFF) / 255.0);
                normalizedPixels[i][j][2] = (int) ((pixel & 0xFF) / 255.0);
            }
        }
        return normalizedPixels;
    }

    private List<String> loadLabelFile(Context context) throws IOException {
        List<String> labelList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open("kinetics600_label_map.txt")));
        String line;
        while ((line = reader.readLine()) != null) {
            labelList.add(line);
        }
        reader.close();
        return labelList;
    }

}