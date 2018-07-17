package com.gl.unawa.listeners;

import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.util.Log;

import com.gl.unawa.Constants;

import java.util.ArrayList;

public class STTListener implements RecognitionListener {
    @Override
    public void onReadyForSpeech(Bundle bundle) {

    }

    @Override
    public void onBeginningOfSpeech() {
        System.out.println("Started listening");
    }

    @Override
    public void onRmsChanged(float v) {
        if (Constants.avv != null) {
            Constants.avv.receive(v);
        }
    }

    @Override
    public void onBufferReceived(byte[] bytes) {

    }

    @Override
    public void onEndOfSpeech() {
        Constants.isListening = false;
        index = 0;
    }

    @Override
    public void onError(int i) {

        String message;
        switch (i) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "ERROR_AUDIO";
                break;

            case SpeechRecognizer.ERROR_CLIENT:
                message = "ERROR_CLIENT";
                break;

            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "ERROR_INSUFFICIENT_PERMISSIONS";
                break;

            case SpeechRecognizer.ERROR_NETWORK:
                message = "ERROR_NETWORK";
                break;

            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "ERROR_NETWORK_TIMEOUT";
                break;

            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "ERROR_NO_MATCH";
                break;

            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "ERROR_RECOGNIZER_BUSY";
                break;

            case SpeechRecognizer.ERROR_SERVER:
                message = "ERROR_SERVER";
                break;

            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "ERROR_SPEECH_TIMEOUT";
                break;

            default:
                message = "ERROR_UNKNOWN";
                break;
        }
        Log.e("STTListener", message);

    }

    @Override
    public void onResults(Bundle bundle) {
        String out = Constants.listenText.getText().toString();
        if (out.trim().equals("")) {
            out = "No text detected!";
        }
        Constants.listenText.setText(out);
    }

    private static int index = 0;

    @Override
    public void onPartialResults(Bundle bundle) {
        ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if (data == null) {
            return;
        }
        String text = Constants.listenText.getText().toString();
        String voice = data.get(0);
        String[] words = voice.split(" ");
        StringBuilder sb = new StringBuilder();
        if (text.length() > 200) {
            index = words.length - 1;
        }
        for (int i = index; i < words.length; i++) {
            sb.append(words[i]);
            sb.append(" ");
        }
        String out = sb.toString().trim();
        Constants.listenText.setText(out);
    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }

}
