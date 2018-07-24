package com.gl.unawa;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;

import com.gl.unawa.util.Util_Camera;
import com.gl.unawa.util.Util_OCR_TTS;
import com.gl.unawa.util.Util_Startup;
import com.gl.unawa.util.Utility;

import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int RESULT_SPEECH = 1;

    private static final String TAG = "Unawa::MainActivity";

//    TODO: Why does OCR SurfaceView freeze after switch tab?

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            Util_Startup.setupConstants(this);
            Util_Startup.setupGUI(this);
            Util_OCR_TTS.setup(this);
            Util_Camera.init(this);
            Utility.tabTransition(this, Constants.NULLTAB, Constants.OCR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostCreate(Bundle bundle) {
        super.onPostCreate(bundle);
        Constants.startup = false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == Constants.REQUEST_RECORD) {
                Constants.recognizer.startListening(Constants.recognizerIntent);
            } else if (requestCode == Constants.REQUEST_CAMERA) {
                Util_Camera.setupCamera(this, "MainActivity::onRequestPermissionsResult");
            }
        } else {
            Log.i("MainActivity", "Permission denied! Request code: " + requestCode);
        }


    }

    @Override
    protected void onActivityResult(int activityCode, int resultCode, Intent data) {
        if (activityCode == RESULT_SPEECH) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                StringBuilder sb = new StringBuilder();
                for (String s : text) {
                    sb.append(s);
                }
                Log.i("MainActivity::onResult", sb.toString());
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Constants.gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("MainActivity", "Pause called");
        if (Constants.recognizer != null) {
            Constants.recognizer.cancel();
        }
        if (Constants.TAB == Constants.OCR) {
            Util_Camera.destroyCamera();
            Constants.paused = true;
        }
        if (Constants.TAB == Constants.SIGN) {
            disableCamera();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("MainActivity", "Resume called");
        if (Constants.TAB == Constants.OCR && Constants.paused) {
            Util_Camera.setupCamera(this, "MainActivity::onResume");
            Constants.paused = false;
        }
        if (Constants.TAB == Constants.SIGN) {
            if (!OpenCVLoader.initDebug()) {
                Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
                OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, Constants.baseLoaderCallback);
            } else {
                Log.d(TAG, "OpenCV library found inside package. Using it!");
                Constants.baseLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Constants.TAB != Constants.LISTEN) {
            Util_Camera.destroyCamera();
        }
        if (Constants.tts != null) {
            Constants.tts.stop();
        }
        if (Constants.TAB == Constants.SIGN) {
            disableCamera();
        }
    }

    public void disableCamera() {
        if (Constants.cameraBridgeViewBase != null) {
            Constants.cameraBridgeViewBase.disableView();
        }
    }

}
