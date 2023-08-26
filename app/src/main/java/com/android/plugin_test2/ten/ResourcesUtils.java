package com.android.plugin_test2.ten;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

import com.android.plugin_test2.util.Loge;

import java.io.File;
import java.lang.reflect.Method;

public class ResourcesUtils {
    private final String PLUGIN_APK_PATH = "/sdcard/plugin.apk";

    private static volatile ResourcesUtils instance;

    private ResourcesUtils() {

    }


    public static ResourcesUtils getInstance() {
        if (instance == null) {
            synchronized (ResourcesUtils.class) {
                if (instance == null) {
                    instance = new ResourcesUtils();
                }
            }
        }
        return instance;
    }

    public Resources loadResources(Context context) {
        try {
            File apk_file = new File(context.getFilesDir() + "/plugin7.apk");
            Loge.e(apk_file.getAbsolutePath());
            //通过反射调用addAssetPath方法进行插件apk资源加载
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPathMethod = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
            addAssetPathMethod.invoke(assetManager, apk_file.getAbsolutePath());
            return new Resources(assetManager, context.getResources().getDisplayMetrics(), context.getResources().getConfiguration());
        } catch (Exception e) {
            Log.e(ResourcesUtils.class.getSimpleName(), e.toString());
            e.printStackTrace();
        }
        return null;
    }

}
