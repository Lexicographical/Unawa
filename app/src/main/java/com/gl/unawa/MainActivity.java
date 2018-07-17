package com.gl.unawa;

import android.animation.Animator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.Toast;

import com.gl.unawa.custom.PortraitCameraBridgeViewBase;
import com.gl.unawa.listeners.CVListener;
import com.gl.unawa.listeners.GestureListener;
import com.gl.unawa.listeners.STTListener;
import com.gl.unawa.util.CameraUtil;
import com.gl.unawa.util.OCR_TTS_Util;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int RESULT_SPEECH = 1;

    private static final String TAG = "Unawa::MainActivity";

    private BaseLoaderCallback _baseLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
                    // Load ndk built module, as specified in moduleName in build.gradle
                    // after opencv initialization
                    System.loadLibrary("native-lib");
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
            }
        }
    };

//    TODO: GestureDetector

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            setupConstants();
            setupGUI();
            OCR_TTS_Util.setup(this);
            CameraUtil.init(this);
            CameraUtil.setLayoutMode(this, Constants.OCR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostCreate(Bundle bundle) {
        super.onPostCreate(bundle);
        Constants.startup = false;
    }


    private void setupConstants() {
        Constants.listenText = findViewById(R.id.sttTextPreview);
        Constants.cameraBridgeViewBase = findViewById(R.id.cvCameraView);
        Constants.cameraBridgeViewBase.disableView();
        Constants.cameraView = findViewById(R.id.surface_view);

        Constants.tabHost = findViewById(R.id.tabHost);
        Constants.tabHost.setup();

        Constants.tabHost.addTab(Constants.tabHost.newTabSpec("OCR").setIndicator("OCR").setContent(new EmptyTabFactory()));
        Constants.tabHost.addTab(Constants.tabHost.newTabSpec("Sign").setIndicator("Sign").setContent(new EmptyTabFactory()));
        Constants.tabHost.addTab(Constants.tabHost.newTabSpec("Listen").setIndicator("Listen").setContent(new EmptyTabFactory()));

        Constants.avv = findViewById(R.id.visualizer);
        Constants.avv.receive(0);

        Constants.listener = new STTListener();
        Constants.recognizer = SpeechRecognizer.createSpeechRecognizer(MainActivity.this);
        Constants.recognizer.setRecognitionListener(Constants.listener);
        Constants.recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        Constants.recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en");
        Constants.recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        Constants.recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
        Constants.recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);

        Constants.gestureDetector = new GestureDetector(this, new GestureListener());

    }

    private void setupGUI() {

        final ImageView yellow = findViewById(R.id.yellow);
        final ConstraintLayout titleBg = findViewById(R.id.titleBg);
        if (Constants.STARTUP) {
            Constants.STARTUP = false;
            titleBg.setVisibility(View.VISIBLE);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    yellow.animate().setDuration(1000).rotation(120).translationX(150).setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            yellow.animate().setDuration(1000).rotation(240).translationY(130).setListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animator) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animator) {
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            titleBg.animate().setDuration(500).alpha(0).setListener(new Animator.AnimatorListener() {
                                                @Override
                                                public void onAnimationStart(Animator animator) {
                                                }

                                                @Override
                                                public void onAnimationEnd(Animator animator) {
                                                    titleBg.setVisibility(View.GONE);
                                                }

                                                @Override
                                                public void onAnimationCancel(Animator animator) {
                                                }

                                                @Override
                                                public void onAnimationRepeat(Animator animator) {
                                                }
                                            });
                                        }
                                    }, 100);
                                }

                                @Override
                                public void onAnimationCancel(Animator animator) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animator) {

                                }
                            });
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {
                        }
                    });
                }
            }, 1000);
        }

        TabHost tabhost = findViewById(R.id.tabHost);
        tabhost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                int old = Constants.TAB;
                switch (s) {
                    case "OCR":
                        Constants.TAB = Constants.OCR;
                        break;

                    case "Sign":
                        Constants.TAB = Constants.SIGN;
                        break;

                    case "Listen":
                        Constants.TAB = Constants.LISTEN;
                        break;

                    default:
                        Constants.TAB = Constants.OCR;
                }
                if (Constants.TAB != Constants.OCR) {
                    CameraUtil.destroyCamera();
                }
                CameraUtil.setLayoutMode(MainActivity.this, Constants.TAB);
            }
        });

        ImageButton listenButton = findViewById(R.id.startListen);
        listenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constants.isListening) {
                    Toast.makeText(MainActivity.this, "Still listening", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    Utility.requestPermissions(MainActivity.this);
                    Constants.isListening = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == Constants.REQUEST_RECORD) {
                Constants.recognizer.startListening(Constants.recognizerIntent);
            } else if (requestCode == Constants.REQUEST_CAMERA) {
                CameraUtil.setupCamera(this);
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
        if (Constants.recognizer != null) {
            Constants.recognizer.cancel();
        }
        if (Constants.TAB != Constants.LISTEN) {
            CameraUtil.destroyCamera();
            Constants.paused = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Constants.TAB != Constants.LISTEN && Constants.paused) {
            CameraUtil.setupCamera(this);
            Constants.paused = false;
        }
        if (Constants.TAB == Constants.SIGN) {
            if (!OpenCVLoader.initDebug()) {
                Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
                OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, _baseLoaderCallback);
            } else {
                Log.d(TAG, "OpenCV library found inside package. Using it!");
                _baseLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Constants.TAB != Constants.LISTEN) {
            CameraUtil.destroyCamera();
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

    private class EmptyTabFactory implements TabHost.TabContentFactory {

        @Override
        public View createTabContent(String tag) {
            return new View(MainActivity.this);
        }

    }

    public native void modifyMat(long mattAddr, int[] hsvBounds);
}
