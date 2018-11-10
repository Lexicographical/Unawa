package com.gl.unawa;


import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.GestureDetector;
import android.view.SurfaceView;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TextView;

import com.gl.unawa.custom.AudioVisualizerView;
import com.gl.unawa.custom.PortraitCameraBridgeViewBase;
import com.gl.unawa.listeners.CVListener;
import com.gl.unawa.listeners.STTListener;
import com.gl.unawa.nn.ImageClassifierASL;
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

    //    OCR_TTS
    public static SurfaceView cameraView;
    public static CameraSource cameraSource;
    public static TextRecognizer ocrDetector;
    public static TextToSpeech tts;
    public static String text = "No text";
    public static boolean startup = true;
    public static boolean paused = false;

//    Sign Language
    public static TextView subtitle;
    public static MultiDetector multiDetector;
    public static int[] hsvBounds_Green = {39, 97, 76, 174, 106, 255};
    public static int[] hsvBounds_Red = {255, 255, 255, 255, 255, 255};
    public static PortraitCameraBridgeViewBase cameraBridgeViewBase;
    public static BaseLoaderCallback baseLoaderCallback;
    public static int settingsMode = 0;

    public static final int SETTINGS_DISABLED = 0;
    public static final int SETTINGS_OR = 1;
    public static final int SETTINGS_GREEN = 2;
    public static final int SETTINGS_RED = 3;

    public static ImageClassifierASL classifier;
    public static HandlerThread classifierThread;
    public static Handler classifierHandler;
    public static final Object classifierThreadLock = new Object();
    public static boolean runClassifierThread = false;
    public static final String CLASSIFIER_THREAD_NAME = "Unawa_Classifier_Thread";

}
