package com.shahin.loginsignupui;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;



public class App extends Application {

    public static final String TAG = App.class.getSimpleName();
    private static App instance;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
//        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                .setDefaultFontPath("font/Lato-Regular.ttf")
//                .setFontAttrId(R.attr.fontPath)
//                .build());
    }
    public static synchronized App getInstance() {
        return instance;
    }


}
