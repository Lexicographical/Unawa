package com.gl.unawa;

import android.app.Activity;
import android.view.View;
import android.widget.TabHost;

public class EmptyTabFactory implements TabHost.TabContentFactory {

    private Activity activity;

    public EmptyTabFactory(Activity activity) {
        this.activity = activity;
    }

    @Override
    public View createTabContent(String tag) {
        return new View(activity);
    }

}
