package com.android.plugin_test2;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.android.plugin_test2.hook.HookUtils;
import com.android.plugin_test2.hook.PluginManager;
import com.android.plugin_test2.one.LoadPluginDex;
import com.android.plugin_test2.util.Loge;

import java.io.File;
import java.io.FileFilter;

public class MyApp extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        File dex_file = getExternalFilesDir("dex_plugin");
        File[] files = dex_file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getAbsolutePath().endsWith(".apk");
            }
        });
        if (files.length > 0) {
            Log.e("files[0].getAbsolutePath()", files[0].getAbsolutePath());
            LoadPluginDex.mergeHostAndPluginDex(this, files[0].getAbsolutePath());
        }
     //  PluginManager.getInstance(this).init();
    }


}
