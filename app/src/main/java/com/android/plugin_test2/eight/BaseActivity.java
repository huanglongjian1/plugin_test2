package com.android.plugin_test2.eight;


import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TextView;

import com.android.plugin_test2.six.SixFile_Uitl;
import com.android.plugin_test2.six.Six_Util;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class BaseActivity extends Activity {

    private AssetManager mAssetManager;
    private Resources mResources;
    private Resources.Theme mTheme;
    private String dexpath = null;    //apk文件地址
    private File fileRelease = null;  //释放目录

    protected DexClassLoader classLoader = null;

    private String pluginName = "plugin7.apk";

    TextView tv;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);

        SixFile_Uitl.extractAssets(newBase, pluginName);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //第三步：加载外部的插件，生成插件对应的ClassLoader
        File extractFile = this.getFileStreamPath(pluginName);
        dexpath = extractFile.getPath();

        fileRelease = getDir("dex", 0);

        classLoader = new DexClassLoader(dexpath, fileRelease.getAbsolutePath(), null, getClassLoader());
    }

    /**
     * 第一步：通过反射，创建AssetManager对象，调用addAssetPath方法，把插件Plugin的路径添加到这个AssetManager对象中
     **/
    protected void loadResources() {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, dexpath);
            mAssetManager = assetManager;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Resources superRes = super.getResources();
        superRes.getDisplayMetrics();
        superRes.getConfiguration();
        mResources = new Resources(mAssetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
        mTheme = mResources.newTheme();
        mTheme.setTo(super.getTheme());
    }

    /**
     * 第二步：重写Acitivity的getAsset，getResources和getTheme方法
     **/
    @Override
    public AssetManager getAssets() {
        return mAssetManager == null ? super.getAssets() : mAssetManager;
    }

    @Override
    public Resources getResources() {
        return mResources == null ? super.getResources() : mResources;
    }

    @Override
    public Resources.Theme getTheme() {
        return mTheme == null ? super.getTheme() : mTheme;
    }
}

