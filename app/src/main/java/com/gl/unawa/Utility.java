package com.gl.unawa;

import android.Manifest;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;

public class Utility {

    public static void requestPermissions(AppCompatActivity activity) {
        activity.requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, Constants.REQUEST_RECORD);
    }
}
