package com.gl.unawa.listeners;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class CVListener implements CameraBridgeViewBase.CvCameraViewListener2 {

    private Mat rgba;

    @Override
    public void onCameraViewStarted(int width, int height) {
        rgba = new Mat(height, width, CvType.CV_8UC4);
    }

    @Override
    public void onCameraViewStopped() {
        rgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame frame) {
        rgba = frame.rgba();
        return rgba;
    }
}
