package com.gl.unawa;


import android.content.Intent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.GestureDetector;
import android.view.SurfaceView;
import android.widget.TabHost;
import android.widget.TextView;

import com.gl.unawa.custom.AudioVisualizerView;
import com.gl.unawa.custom.PortraitCameraBridgeViewBase;
import com.gl.unawa.listeners.CVListener;
import com.gl.unawa.listeners.STTListener;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiDetector;
import com.google.android.gms.vision.text.TextRecognizer;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;

/**
 * Created by User on 4/5/2018.
 */

public class Constants {

    public static boolean STARTUP = true;
    public static boolean isListening = false;
    public static final int REQUEST_RECORD = 10;
    public static final int REQUEST_CAMERA = 1;
    public static int TAB = 1;

    public static final int NULLTAB = 0;
    public static final int OCR = 1;
    public static final int SIGN = 2;
    public static final int LISTEN = 4;

    //    Generic Views
    public static TextView listenText;
    public static GestureDetector gestureDetector;
    public static TabHost tabHost;

    //    STT
    public static SpeechRecognizer recognizer;
    public static Intent recognizerIntent;
    public static STTListener listener;
    public static AudioVisualizerView avv;

    //    PTS
    public static SurfaceView cameraView;
    public static CameraSource cameraSource;
    public static TextRecognizer ocrDetector;
    public static TextToSpeech tts;
    public static String text = "No text";
    public static boolean startup = true;
    public static boolean paused = false;

//    OpenCV
    public static MultiDetector multiDetector;
    public static int[] hsvBounds = {61, 235, 102, 245, 20, 245};
    public static PortraitCameraBridgeViewBase cameraBridgeViewBase;
    public static BaseLoaderCallback baseLoaderCallback;

    public static long lastTime = 0;
    public static int ticks = 0;

}
