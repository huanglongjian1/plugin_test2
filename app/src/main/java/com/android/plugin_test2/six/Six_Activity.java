package com.android.plugin_test2.six;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.plugin_test2.R;
import com.android.plugin_test2.util.Loge;
import com.android.plugin_test7.IBaseInterface;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class Six_Activity extends AppCompatActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        SixFile_Uitl.extractAssets(newBase, "plugin7.apk");
        loadApk();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Six_Util.mergePluginResources(getApplication(), "com.android.plugin_test7");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_main);
        try {
            Class<?> aClass = dexClassLoader.loadClass("com.android.plugin_test7.TestResource");
            Object newInstance = aClass.newInstance();
            IBaseInterface iBaseInterface = (IBaseInterface) newInstance;
            Loge.e(iBaseInterface.getStringForResId(Six_Activity.this) + " = ");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        TextView textView = findViewById(R.id.main_tv);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Loge.e("点击了");
            }
        });

    }

    private void loadApk() {
        File fileStreamPath = this.getFileStreamPath("plugin7.apk");
        String dexPath = fileStreamPath.getPath();
        File fileRelease = getDir("plugin7", 0);//0 Context.MODE_PRIVATE;
        dexClassLoader = new DexClassLoader(dexPath, fileRelease.getAbsolutePath(), null, getClassLoader());
      //  loadResource(dexPath);
    }

    DexClassLoader dexClassLoader;
    AssetManager assetManager;
    Resources resources;
    Resources.Theme theme;

    /**
     * @param dexPath
     */
    protected void loadResource(String dexPath) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, dexPath);
            this.assetManager = assetManager;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        resources = new Resources(assetManager, super.getResources().getDisplayMetrics(), super.getResources().getConfiguration());
        theme = resources.newTheme();
        theme.setTo(super.getTheme());
    }

    //设置资源
    @Override
    public AssetManager getAssets() {
        if (assetManager == null) {
            return super.getAssets();
        }
        return assetManager;
    }

    @Override
    public Resources getResources() {

        return Six_Util.m_resources;
    }

}
