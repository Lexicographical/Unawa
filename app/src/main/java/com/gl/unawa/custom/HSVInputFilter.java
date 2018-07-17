package com.gl.unawa.custom;

import android.text.InputFilter;
import android.text.Spanned;

public class HSVInputFilter implements InputFilter {

    private static final int min = 0, max = 255;

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            String inputStr = dest.toString().substring(0, dstart) + dest.toString().substring(dend, dest.toString().length());
            inputStr = inputStr.substring(0, dstart) + source.toString() + inputStr.substring(dstart, inputStr.length());
            int input = Integer.parseInt(inputStr);
            if (min <= input && input <= max) {
                return null;
            }
        } catch (NumberFormatException ignored) {
        }
        return "";
    }
}
