package com.example.davide.letssound.application;

import android.app.Application;

/**
 * Created by davide on 01/08/16.
 */
public class LetssoundApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initCalligraph();
        initMediaService();
    }

    /**
     *
     */
    private void initMediaService() {

    }

    /**
     *
     */
    private void initCalligraph() {
//        CalligraphyConfig.initDefault(new CalligraphyConfig
//                .Builder()
//                .setDefaultFontPath(FONT_PATH)
//                .setFontAttrId(R.attr.fontPath)
//                .build());
    }
}
