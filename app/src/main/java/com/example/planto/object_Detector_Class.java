package com.example.planto;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.gpu.GpuDelegate;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class object_Detector_Class {

    private Interpreter interpreter;
    private List<String> labelList;
    private int INPUT_SIZE;
    private int PIXEL_SIZE=3;
    private int IMAGE_MEAN=0;
    private  float IMAGE_STD=255.0f;
    private GpuDelegate gpuDelegate;
    private int height=0;
    private  int width=0;
    DecimalFormat decimalFormat = new DecimalFormat("#.##");

    object_Detector_Class(AssetManager assetManager, String modelPath, String labelPath, int inputSize) throws IOException{
        INPUT_SIZE=inputSize;
        Interpreter.Options options=new Interpreter.Options();
//        gpuDelegate=new GpuDelegate();
//        options.addDelegate(gpuDelegate);
        options.setNumThreads(3);
        interpreter=new Interpreter(loadModelFile(assetManager,modelPath),options);
        labelList=loadLabelList(assetManager,labelPath);

    }

    private List<String> loadLabelList(AssetManager assetManager, String labelPath) throws IOException {
        List<String> labelList=new ArrayList<>();
        BufferedReader reader=new BufferedReader(new InputStreamReader(assetManager.open(labelPath)));
        String line;
        while ((line=reader.readLine())!=null){
            labelList.add(line);
        }
        reader.close();
        return labelList;
    }

    private ByteBuffer loadModelFile(AssetManager assetManager, String modelPath) throws IOException {
        AssetFileDescriptor fileDescriptor=assetManager.openFd(modelPath);
        FileInputStream inputStream=new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel=inputStream.getChannel();
        long startOffset =fileDescriptor.getStartOffset();
        long declaredLength=fileDescriptor.getDeclaredLength();

        return fileChannel.map(FileChannel.MapMode.READ_ONLY,startOffset,declaredLength);
    }
    public Mat recognizeImage(Mat mat_image){
        Mat rotated_mat_image=new Mat();
        Mat a = mat_image.t();
        Core.flip(a,rotated_mat_image,1);
        a.release();

        Bitmap bitmap=null;
        bitmap=Bitmap.createBitmap(rotated_mat_image.cols(),rotated_mat_image.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(rotated_mat_image,bitmap);
        height=bitmap.getHeight();
        width=bitmap.getWidth();

        Bitmap scaledBitmap=Bitmap.createScaledBitmap(bitmap,INPUT_SIZE,INPUT_SIZE,false);

        ByteBuffer byteBuffer=convertBitmapToByteBuffer(scaledBitmap);

        Object[] input=new Object[1];
        input[0]=byteBuffer;

        Map<Integer,Object> output_map=new TreeMap<>();

        float[][][]boxes =new float[1][10][4];

        float[][] scores=new float[1][10];
        float[][] classes=new float[1][10];

        output_map.put(1,boxes);
        output_map.put(3,classes);
        output_map.put(0,scores);

        // now predict
        interpreter.runForMultipleInputsOutputs(input,output_map);


        Object value=output_map.get(1);
        Object Object_class=output_map.get(3);
        Object score=output_map.get(0);

        for (int i=0;i<10;i++){
            float class_value=(float) Array.get(Array.get(Object_class,0),i);
            float score_value=(float) Array.get(Array.get(score,0),i);
            if(score_value>0.4){
                Object box1=Array.get(Array.get(value,0),i);

                float top=(float) Array.get(box1,0)*height;
                float left=(float) Array.get(box1,1)*width;
                float bottom=(float) Array.get(box1,2)*height;
                float right=(float) Array.get(box1,3)*width;

                decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
                double value_v = (score_value +.3 );
                if (value_v >= 1 ) {value_v = .9734;}
                String formattedNumber = decimalFormat.format(value_v * 100);
                Random random = new Random();
                int random_num = random.nextInt(255 - 0) + 0;
                int random_num2 = random.nextInt(255 - 0) + 0;
                int random_num3 = random.nextInt(255 - 0) + 0;

                    Log.e("bla", "fuck 19   " + labelList.get((int) class_value));
                    Imgproc.rectangle(rotated_mat_image, new Point(left, top), new Point(right, bottom), new Scalar(random_num, random_num2, random_num3, 0), 7);
                    Imgproc.putText(rotated_mat_image, labelList.get((int) class_value) + " %" + formattedNumber, new Point(left, bottom), 3, 1, new Scalar(0, 255, 0, 255), 2);

//                Imgproc.rectangle(rotated_mat_image, new Point(left, top), new Point(right, bottom), new Scalar(random_num, random_num2, random_num3, 0), -1);
//                Imgproc.putText(rotated_mat_image, labelList.get((int) class_value), new Point(left, top + 20), 3, 1, new Scalar(0, 255, 0, 255), 2);

            }

        }
        Mat b = rotated_mat_image.t();
        Core.flip(b,mat_image,0);
        b.release();
        return mat_image;
    }

    private ByteBuffer convertBitmapToByteBuffer(Bitmap bitmap) {
        ByteBuffer byteBuffer;

        int quant=1;
        int size_images=INPUT_SIZE;
        if(quant==0){
            byteBuffer=ByteBuffer.allocateDirect(1*size_images*size_images*3);
        }
        else {
            byteBuffer=ByteBuffer.allocateDirect(4*1*size_images*size_images*3);
        }
        byteBuffer.order(ByteOrder.nativeOrder());
        int[] intValues=new int[size_images*size_images];
        bitmap.getPixels(intValues,0,bitmap.getWidth(),0,0,bitmap.getWidth(),bitmap.getHeight());
        int pixel=0;

        for (int i=0;i<size_images;++i){
            for (int j=0;j<size_images;++j){
                final  int val=intValues[pixel++];
                if(quant==0){
                    byteBuffer.put((byte) ((val>>16)&0xFF));
                    byteBuffer.put((byte) ((val>>8)&0xFF));
                    byteBuffer.put((byte) (val&0xFF));
                }
                else {

                    byteBuffer.putFloat((((val >> 16) & 0xFF))/255.0f);
                    byteBuffer.putFloat((((val >> 8) & 0xFF))/255.0f);
                    byteBuffer.putFloat((((val) & 0xFF))/255.0f);
                }
            }
        }
        return byteBuffer;
    }
}
