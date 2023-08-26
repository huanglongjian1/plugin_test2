package com.android.plugin_test2.ten;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.plugin_test2.R;
import com.android.plugin_test2.six.Six_Util;
import com.android.plugin_test2.three.Plugin_Uitl;
import com.android.plugin_test2.util.Loge;
import com.android.plugin_test2.util.Reflector;
import com.android.plugin_test7.IBaseInterface;

import java.lang.reflect.InvocationTargetException;

import dalvik.system.PathClassLoader;

public class Ten_Activity extends AppCompatActivity {
    Resources resources;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String apk_path = getFilesDir().getAbsolutePath() + "/plugin7.apk";
        Loge.e(apk_path);
        PathClassLoader pathClassLoader = new PathClassLoader(apk_path, getClassLoader());
        try {
            createRes(apk_path);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        PackageInfo packageInfo = getPackageManager().getPackageArchiveInfo(apk_path, PackageManager.GET_ACTIVITIES);

        try {
            Six_Util.mergePluginResources(getApplication(),packageInfo.packageName);
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

        Plugin_Uitl.inject(pathClassLoader, this, apk_path);

        TextView textView = findViewById(R.id.main_tv);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    IBaseInterface iBaseInterface = (IBaseInterface) pathClassLoader.loadClass("com.android.plugin_test7.TestResource").newInstance();
                    Loge.e(iBaseInterface.toString());
                    String s = iBaseInterface.getStringForResId(Ten_Activity.this);

                    String s1 = resources.getString(resources.getIdentifier("test_string", "string", "com.android.plugin_test7"));

                    Drawable drawable = resources.getDrawable(resources.getIdentifier("ic_launcher_round", "mipmap", packageInfo.packageName));
                    ImageView imageView = findViewById(R.id.four_image);
                    imageView.setImageDrawable(drawable);
                    Loge.e(s1+s);

                    Drawable drawable1 = resources.getDrawable(Six_Util.getResId(Ten_Activity.this, apk_path, packageInfo.packageName, "ic_launcher"));
                    //   imageView.setImageDrawable(drawable1);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void createRes(String apk_path) throws IllegalAccessException, InstantiationException {
        AssetManager assetManager = AssetManager.class.newInstance();
        Reflector.on(AssetManager.class).method("addAssetPath", String.class).with(assetManager).call(apk_path);
        resources = new Resources(assetManager, super.getResources().getDisplayMetrics(), super.getResources().getConfiguration());
    }
    @Override
    public Resources getResources() {
        if (resources == null) {
            return super.getResources();

        } else {
            return resources;
        }
    }

}
