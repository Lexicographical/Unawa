package com.gl.unawa.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;

import com.gl.unawa.Constants;
import com.gl.unawa.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiDetector;

import java.io.IOException;

public class CameraUtil {

//    TODO: Make cameraSource stop for SignLanguage so OpenCV can lock canvas of SurfaceView

    public static void init(final Activity activity) {

        Constants.cameraView = activity.findViewById(R.id.surface_view);

        Constants.multiDetector = new MultiDetector.Builder().add(Constants.ocrDetector).build();

        Constants.cameraSource = new CameraSource.Builder(activity, Constants.multiDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1280, 1024)
                .setRequestedFps(2.0f)
                .setAutoFocusEnabled(true)
                .build();

        Constants.cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                setupCamera(activity);
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int height, int width) {
                Log.i("CameraUtil", "Surface changed!");
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                destroyCamera();
            }
        });

    }

    public static void setupCamera(Activity activity) {
        try {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, Constants.REQUEST_CAMERA);
                return;
            }
            Constants.cameraSource.start(Constants.cameraView.getHolder());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void destroyCamera() {
        if (Constants.cameraSource != null) {
            Constants.cameraSource.stop();
        }
    }

}
