package com.gl.unawa;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.gl.unawa.util.CameraUtil;

import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

public class Utility {

    public static void requestPermissions(AppCompatActivity activity) {
        activity.requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, Constants.REQUEST_RECORD);
    }

    public static void animateScale(ImageButton imageButton) {
        ObjectAnimator upX = ObjectAnimator.ofFloat(imageButton, "scaleX", 1.2f);
        ObjectAnimator upY = ObjectAnimator.ofFloat(imageButton, "scaleY", 1.2f);
        ObjectAnimator normX = ObjectAnimator.ofFloat(imageButton, "scaleX", 1f);
        ObjectAnimator normY = ObjectAnimator.ofFloat(imageButton, "scaleY", 1f);

        upX.setDuration(250);
        upY.setDuration(250);
        normX.setDuration(250);
        normY.setDuration(250);

        AnimatorSet upScale = new AnimatorSet();
        upScale.play(upX).with(upY);
        AnimatorSet downScale = new AnimatorSet();
        downScale.play(normX).with(normY);
        AnimatorSet scale = new AnimatorSet();
        scale.play(upScale).before(downScale);
        scale.start();
    }

    public static void tabTransition(Activity activity, int from, int to) {
        switch (from) {
            case Constants.OCR:
                CameraUtil.destroyCamera();
                break;

            case Constants.SIGN:
                if (Constants.cameraBridgeViewBase != null) {
                    Constants.cameraBridgeViewBase.disableView();
                }
                break;
        }

        int[][] ids = {
                new int[]{R.id.capture, R.id.surface_view},
                new int[]{R.id.subtitle},
                new int[]{R.id.listen}
        };

        for (int i = 0; i < ids.length; i++) {
            int state = View.GONE;
            if (to == 1 << i) {
                state = View.VISIBLE;
            }
            for (int j : ids[i]) {
                activity.findViewById(j).setVisibility(state);
            }
        }
        if (to == Constants.SIGN) {
            if (!OpenCVLoader.initDebug()) {
                Log.d("OpenCV", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
                OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, activity, Constants.baseLoaderCallback);
            } else {
                Log.d("OpenCV", "OpenCV library found inside package. Using it!");
                Constants.baseLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
            }
        }

    }

}
