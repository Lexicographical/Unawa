package com.gl.unawa.listeners;

import com.gl.unawa.Constants;
import com.gl.unawa.custom.PortraitCameraBridgeViewBase;

import org.opencv.core.Mat;

public class CVListener implements PortraitCameraBridgeViewBase.CvCameraViewListener2 {
    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(PortraitCameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat mat = inputFrame.rgba();
        modifyMat(mat.getNativeObjAddr(), Constants.hsvBounds);
        return mat;
    }

    public native void modifyMat(long matAddr, int[] hsvBounds);
}
