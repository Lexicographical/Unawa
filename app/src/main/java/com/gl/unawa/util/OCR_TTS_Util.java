package com.gl.unawa.util;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageButton;

import com.gl.unawa.Constants;
import com.gl.unawa.R;
import com.gl.unawa.Utility;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.util.Locale;

public class OCR_TTS_Util {

    public static void setup(final Activity activity) {

        Constants.ocrDetector = new TextRecognizer.Builder(activity).build();
        if (!Constants.ocrDetector.isOperational()) {
            Log.w("MainActivity", "Recognizer not working. Dependencies not available.");
        } else {
            Log.w("MainActivity", "Recognizer loaded!");

            Constants.ocrDetector.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {

                }

                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if (items.size() != 0) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < items.size(); i++) {
                            TextBlock item = items.valueAt(i);
                            sb.append(item.getValue());
                            sb.append("\n");
                        }
                        Constants.text = sb.toString();
                    }
                }
            });
        }

        TextToSpeech.OnInitListener listener = new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS) {
                    Log.i("TTS", "Success!");
                    Constants.tts.setLanguage(Locale.US);
                } else {
                    Log.i("TTS", "Error starting.");
                }
            }
        };
        Constants.tts = new TextToSpeech(activity, listener);

        final ImageButton capture = activity.findViewById(R.id.capture);
        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.animateScale(capture);

                Constants.tts.speak(Constants.text, TextToSpeech.QUEUE_ADD, null, "DEFAULT");
            }
        });
    }

}
