package com.gl.unawa.util;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.Toast;

import com.gl.unawa.Constants;
import com.gl.unawa.EmptyTabFactory;
import com.gl.unawa.R;
import com.gl.unawa.listeners.CVListener;
import com.gl.unawa.listeners.GestureListener;
import com.gl.unawa.listeners.STTListener;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;

public class Util_Startup {


    public static void setupGUI(final Activity activity) {

        final ImageView yellow = activity.findViewById(R.id.yellow);
        final ConstraintLayout titleBg = activity.findViewById(R.id.titleBg);
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

        Constants.tabHost = activity.findViewById(R.id.tabHost);
        Constants.tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                int old = Constants.TAB;
                Log.i("MainActivity", "Tab changed!");
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
                Utility.tabTransition(activity, old, Constants.TAB);
            }
        });

    }

    public static void setupConstants(final AppCompatActivity activity) {
        Constants.listenText = activity.findViewById(R.id.sttTextPreview);
        Constants.cameraBridgeViewBase = activity.findViewById(R.id.cvCameraView);
        Constants.cameraBridgeViewBase.setCvCameraViewListener(new CVListener());
        Constants.cameraBridgeViewBase.setVisibility(View.VISIBLE);
        Constants.cameraBridgeViewBase.disableView();
        Constants.cameraView = activity.findViewById(R.id.surface_view);

        Constants.tabHost = activity.findViewById(R.id.tabHost);
        Constants.tabHost.setup();

        Constants.tabHost.addTab(Constants.tabHost.newTabSpec("OCR").setIndicator("OCR").setContent(new EmptyTabFactory(activity)));
        Constants.tabHost.addTab(Constants.tabHost.newTabSpec("Sign").setIndicator("Sign").setContent(new EmptyTabFactory(activity)));
        Constants.tabHost.addTab(Constants.tabHost.newTabSpec("Listen").setIndicator("Listen").setContent(new EmptyTabFactory(activity)));

        Constants.avv = activity.findViewById(R.id.visualizer);
        Constants.avv.receive(0);

        Constants.listener = new STTListener();
        Constants.recognizer = SpeechRecognizer.createSpeechRecognizer(activity);
        Constants.recognizer.setRecognitionListener(Constants.listener);
        Constants.recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        Constants.recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en");
        Constants.recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        Constants.recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
        Constants.recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);

        Constants.gestureDetector = new GestureDetector(activity, new GestureListener());

        final ImageButton listenButton = activity.findViewById(R.id.startListen);
        listenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.animateScale(listenButton);
                if (Constants.isListening) {
                    Toast.makeText(activity, "Still listening", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    Utility.requestPermissions(activity);
                    Constants.isListening = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Constants.baseLoaderCallback = new BaseLoaderCallback(activity) {
            @Override
            public void onManagerConnected(int status) {
                switch (status) {
                    case LoaderCallbackInterface.SUCCESS: {
                        Log.i("Unawa::UtilityStartup", "OpenCV loaded successfully");
                        // Load ndk built module, as specified in moduleName in build.gradle
                        // after opencv initialization
                        if (Constants.TAB == Constants.SIGN) {
                            System.loadLibrary("native-lib");
                            Constants.cameraBridgeViewBase.enableView();
                            Log.i("MainActivity", "enabled frame!");
                        }
                    }
                    break;
                    default: {
                        super.onManagerConnected(status);
                    }
                }
            }
        };

        final ImageButton settingsButton = activity.findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsButton.animate().rotation(180).setDuration(500).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        settingsButton.setRotation(0);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
                Constants.toggleSettings = !Constants.toggleSettings;
                activity.findViewById(R.id.subtitle).setVisibility(Constants.toggleSettings ? View.GONE : View.VISIBLE);
                activity.findViewById(R.id.sliderContainer).setVisibility(Constants.toggleSettings ? View.VISIBLE : View.GONE);
            }
        });

    }

}