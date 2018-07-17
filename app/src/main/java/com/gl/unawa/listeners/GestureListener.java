package com.gl.unawa.listeners;

import android.view.GestureDetector;
import android.view.MotionEvent;

import com.gl.unawa.Constants;

public class GestureListener implements GestureDetector.OnGestureListener {
    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2, float vx, float vy) {
        float dist = event1.getX() - event2.getX();
        if (Math.abs(dist) < 100 || Math.abs(vx) < 100) {
            return true;
        }
        int current = Constants.tabHost.getCurrentTab();
        if (vx > 1000) {
            current--;
            if (current < 0) {
                current = 2;
            }
        } else if (vx < -1000) {
            current++;
            if (current > 2) {
                current = 0;
            }
        }
        Constants.tabHost.setCurrentTab(current);
        return true;
    }
}
