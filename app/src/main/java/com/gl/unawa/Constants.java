package com.gl.unawa;


import android.content.Intent;
import android.content.SharedPreferences;
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

    public static final int REQUEST_RECORD = 10;
    public static final int REQUEST_CAMERA = 1;
    public static int TAB = 1;

    public static final int NULLTAB = 0;
    public static final int OCR = 1;
    public static final int SIGN = 2;
    public static final int LISTEN = 4;

    public static boolean STARTUP = true;

    public static SharedPreferences pref;
    public static TextView titleBar;

    //    Generic Views
    public static TextView listenText;
    public static GestureDetector gestureDetector;
    public static TabHost tabHost;

    //    STT
    public static SpeechRecognizer recognizer;
    public static Intent recognizerIntent;
    public static STTListener listener;
    public static AudioVisualizerView avv;
    public static boolean isListening = false;

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
    public static TextView preview;
    public static MultiDetector multiDetector;
    //    gloves
//    public static int[] hsvBounds_Green = {39, 97, 76, 174, 106, 255};
//    hand
    public static int[] hsvBounds_Green = {54, 88, 144, 255, 47, 255};
    public static int[] hsvBounds_Red = {255, 255, 255, 255, 255, 255};
    public static PortraitCameraBridgeViewBase cameraBridgeViewBase;
    public static BaseLoaderCallback baseLoaderCallback;
    public static int settingsMode = 0;

    public static final int SETTINGS_DISABLED = 0;
    public static final int SETTINGS_OR = 1;
    public static final int SETTINGS_GREEN = 2;
    public static final int SETTINGS_RED = 3;

    public static final float GOOD_PROB_THRESHOLD = 0.15f;
    public static final float EMPTY_PROB_THRESHOLD = 0.03f;

    public static ImageClassifierASL classifier;
    public static String letterCache = "A";
    public static int letterFrameCount = 0;
    public static int emptyFrameCount = 0;
    public static final int frameThresh = 15;
    public static StringBuilder wordBuilder = new StringBuilder();

}
