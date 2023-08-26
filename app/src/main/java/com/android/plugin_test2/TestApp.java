package com.android.plugin_test2;

import android.app.Application;

public class TestApp extends Application {
    public static Application application;
    @Override
    public void onCreate() {
        super.onCreate();
        application=this;
    }

    public static Application getApplication() {
        return application;
    }
}
