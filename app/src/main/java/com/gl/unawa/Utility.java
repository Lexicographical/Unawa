package com.gl.unawa;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.gl.unawa.util.CameraUtil;

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
                Constants.cameraBridgeViewBase.disableView();
                break;
        }

        if (to == Constants.OCR) {
            activity.findViewById(R.id.listen).setVisibility(View.GONE);
            activity.findViewById(R.id.capture).setVisibility(View.VISIBLE);
            activity.findViewById(R.id.subtitle).setVisibility(View.GONE);
            activity.findViewById(R.id.surface_view).setVisibility(View.VISIBLE);
        } else if (to == Constants.SIGN) {
            activity.findViewById(R.id.listen).setVisibility(View.GONE);
            activity.findViewById(R.id.capture).setVisibility(View.GONE);
            activity.findViewById(R.id.subtitle).setVisibility(View.VISIBLE);
            activity.findViewById(R.id.surface_view).setVisibility(View.GONE);
        } else {
            activity.findViewById(R.id.listen).setVisibility(View.VISIBLE);
            activity.findViewById(R.id.capture).setVisibility(View.GONE);
            activity.findViewById(R.id.subtitle).setVisibility(View.GONE);
            activity.findViewById(R.id.surface_view).setVisibility(View.GONE);
        }
    }

}
