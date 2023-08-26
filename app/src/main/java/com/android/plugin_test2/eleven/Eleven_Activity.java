package com.android.plugin_test2.eleven;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.plugin_test2.R;
import com.android.plugin_test2.six.Six_Util;
import com.android.plugin_test2.three.Plugin_Uitl;
import com.android.plugin_test2.util.Loge;
import com.android.plugin_test7.IBaseInterface;

import java.lang.reflect.InvocationTargetException;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

public class Eleven_Activity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.main_tv);
        textView.setText("Eleven_Activity");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                                   Loge.e(Six_Util.m_resources.toString());
//                    IBaseInterface iBaseInterface = (IBaseInterface) Class.forName("com.android.plugin_test7.TestResource").newInstance();
//                    String s = iBaseInterface.getStringForResId(Eleven_Activity.this);
//                    Loge.e(s);
                Loge.e(getResources().toString());
                String s = getResources().getString(getResources().getIdentifier("ic_launcher", "mipmap", packageInfo.packageName));
                Loge.e(s);
            }
        });
        try {
            loadApk();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    PathClassLoader pathClassLoader;
    PackageInfo packageInfo;

    private void loadApk() throws NoSuchFieldException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {
        String path = getFilesDir().getAbsolutePath() + "/plugin7.apk";
        packageInfo = getPackageManager().getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);

        pathClassLoader = new PathClassLoader(path, getClassLoader());
        Plugin_Uitl.inject_2(pathClassLoader, this, path);
        Six_Util.mergePluginResources(getApplication(), packageInfo.packageName);
    }

    @Override
    public Resources getResources() {
        return Six_Util.m_resources == null ? super.getResources() : Six_Util.m_resources;
    }
}
