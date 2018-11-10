package com.gl.unawa.listeners;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.gl.unawa.Constants;
import com.gl.unawa.custom.PortraitCameraBridgeViewBase;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CVListener implements PortraitCameraBridgeViewBase.CvCameraViewListener2 {

    private Activity activity;

    public CVListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(PortraitCameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat original = inputFrame.rgba();
        Mat mat = original.clone();

        boolean showBinary = true;
        if (Constants.settingsMode == Constants.SETTINGS_DISABLED) {
            showBinary = false;
        }

        modifyMat(mat.getNativeObjAddr(), Constants.hsvBounds_Green, Constants.hsvBounds_Red, Constants.settingsMode, showBinary);

        int startX = 400, endX = startX + 300;
        int startY = 100, endY = startY + 300;

        if (Constants.settingsMode <= Constants.SETTINGS_OR) {
            if (Constants.classifier != null) {
                Mat classify = mat.submat(startY, endY, startX, endX);
                Imgproc.resize(classify, classify, new Size(50, 50));

                Mat preview = new Mat();
                int previewSize = 200;
                Imgproc.resize(classify, preview, new Size(previewSize, previewSize));
                int rowStart = 500;
                int colStart = 500;
                Mat submat = original.submat(rowStart, rowStart + previewSize, colStart, colStart + previewSize);
                preview.copyTo(submat);

                Imgproc.rectangle(original, new Point(rowStart, colStart), new Point(rowStart + previewSize, colStart + previewSize), new Scalar(0, 0, 255), 3);
                Constants.classifier.classifyFrame(classify, activity);
            } else {
                Log.i("CVListener", "Classifier was null");
            }
        }
        if (showBinary) {
            Imgproc.rectangle(mat, new Point(startX, startY), new Point(endX, endY), new Scalar(0, 255, 0), 3);
            return mat;
        }
        Imgproc.rectangle(original, new Point(startX, startY), new Point(endX, endY), new Scalar(0, 255, 0), 3);
        return original;
    }

    public native void modifyMat(long matAddr, int[] hsvBoundsGreen, int[] hsvBoundsRed, int colorMode, boolean displaySeg);

}
