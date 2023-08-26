package com.android.plugin;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

import java.lang.reflect.Method;

public class ResourcesUtils {
    private  String PLUGIN_APK_PATH;

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
        PLUGIN_APK_PATH="/storage/emulated/0/Android/data/com.android.plugin_test2/files/apk_file/plugin_test5-debug.apk";
        try {
            //通过反射调用addAssetPath方法进行插件apk资源加载
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPathMethod = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
            addAssetPathMethod.invoke(assetManager, PLUGIN_APK_PATH);
            return new Resources(assetManager, context.getResources().getDisplayMetrics(), context.getResources().getConfiguration());
        } catch (Exception e) {
            Log.e(ResourcesUtils.class.getSimpleName(), e.toString());
            e.printStackTrace();
        }
        return null;
    }

}
