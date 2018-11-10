package com.gl.unawa.nn;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.SpannableStringBuilder;
import android.util.Log;

import com.gl.unawa.Constants;

import org.opencv.core.Mat;

import java.io.IOException;
import java.util.Arrays;

public class ImageClassifierASL extends ImageClassifier {

    private float[][] labelProbArray = null;
    private static final int IMAGE_MEAN = 128;
    private static final float IMAGE_STD = 128.0f;

    /**
     * Initializes an {@code ImageClassifier}.
     *
     * @param activity
     */
    public ImageClassifierASL(Activity activity) throws IOException {
        super(activity);
        labelProbArray = new float[1][getNumLabels()];
    }

    public void classifyFrame(final Mat seg, Activity activity) {
        if (Constants.classifier == null || activity == null) {
            Log.e("ImageClassifierASL", "Null classifier or activity");
        }
        if (seg == null) {
            Log.e("ImageClassifierASL", "Null mat passed to ImageClassifierASL::classifyFrame!");
        }
        final SpannableStringBuilder textToShow = new SpannableStringBuilder();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Constants.classifier.classifyFrame(seg, textToShow);
                Log.e("Classifier", textToShow.toString());
            }
        });
        thread.start();
    }

    @Override
    protected String getModelPath() {
        return "asl.tflite";
    }

    @Override
    protected String getLabelPath() {
        return "labels_asl.txt";
    }

    @Override
    protected int getImageSizeX() {
        return 50;
    }

    @Override
    protected int getImageSizeY() {
        return 50;
    }

    @Override
    protected int getNumBytesPerChannel() {
        return 4;
    }

    @Override
    protected void addPixelValue(int pixelValue) {
        imgData.putFloat((pixelValue >> 16) & 0xFF);
        imgData.putFloat((pixelValue >> 8) & 0xFF);
        imgData.putFloat(pixelValue & 0xFF);
    }

    @Override
    protected float getProbability(int labelIndex) {
        return labelProbArray[0][labelIndex];
    }

    @Override
    protected void setProbability(int labelIndex, Number value) {
        labelProbArray[0][labelIndex] = value.byteValue();
    }

    @Override
    protected float[][] getProbabilities() {
        return labelProbArray;
    }

    @Override
    protected float getNormalizedProbability(int labelIndex) {
//        Log.i("ImageClassiferASL", "labelProbArray " + Arrays.toString(labelProbArray[0]));
//        Log.i("ImageClassiferASL", "ImageClassiferASL::getNormalizedProbability " + labelProbArray[0][labelIndex]);
//        float prob = (labelProbArray[0][labelIndex] * 100.0f) / 127.0f;
        float prob = (labelProbArray[0][labelIndex] * 100.0f) / 255.0f;
        return prob; // replaced  "& 0xff" with "* 100"
    }

    @Override
    protected void runInference() {
        tflite.run(imgData, labelProbArray);
//        Log.i("ImageClassifierASL", "labelProbArray " + Arrays.toString(labelProbArray[0]));
    }
}
