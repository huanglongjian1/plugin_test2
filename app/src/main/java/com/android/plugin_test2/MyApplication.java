package com.android.plugin_test2;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;
import android.util.Log;

import com.android.plugin_test2.six.Six_Util;
import com.android.plugin_test2.three.Plugin_Uitl;
import com.android.plugin_test2.util.Loge;

import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class MyApplication extends Application {
    private Context sContext;

    private AssetManager assetManager;
    private Resources newResource;
    private Resources.Theme mTheme;
    private DexClassLoader dexClassLoader;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;

        try {

            //创建我们自己的Resource
            //这是插件apk的路径
            String apkPath = getFilesDir() + "/plugin7.apk";
            Loge.e(apkPath);
            dexClassLoader = new DexClassLoader(apkPath, getExternalCacheDir().getAbsolutePath(), null, getClassLoader());


            assetManager = AssetManager.class.newInstance();
            Method addAssetPathMethod = assetManager.getClass().getDeclaredMethod("addAssetPath", String.class);
            addAssetPathMethod.setAccessible(true);

            addAssetPathMethod.invoke(assetManager, apkPath);

            Method ensureStringBlocks = AssetManager.class.getDeclaredMethod("ensureStringBlocks");
            ensureStringBlocks.setAccessible(true);
            ensureStringBlocks.invoke(assetManager);

            Resources supResource = getResources();
            Log.e("Main", "supResource = " + supResource);
            newResource = new Resources(assetManager, supResource.getDisplayMetrics(), supResource.getConfiguration());
            Log.e("Main", "设置 getResource = " + getResources());

            mTheme = newResource.newTheme();
            mTheme.setTo(super.getTheme());
        } catch (Exception e) {
            Log.e("Main", "走了我的callActivityOnCreate 错了 = " + e.getMessage());
            e.printStackTrace();
        }
    }

    public DexClassLoader getDexClassLoader() {
        return dexClassLoader;
    }

    @Override
    public AssetManager getAssets() {
        return assetManager == null ? super.getAssets() : assetManager;
    }

    @Override
    public Resources getResources() {
        return newResource == null ? super.getResources() : newResource;
    }

    @Override
    public Resources.Theme getTheme() {
        return mTheme == null ? super.getTheme() : mTheme;
    }

    public Context getContext() {
        return sContext;
    }
}