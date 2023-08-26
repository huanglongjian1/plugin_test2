package com.android.plugin_test2.three;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.plugin.ActivityInterface;
import com.android.plugin.PluginManager;
import com.android.plugin_test2.util.Loge;

import java.io.File;
import java.io.FileFilter;

public class Proxy_Activity_02 extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String target = getIntent().getStringExtra("className");
        Loge.e(target);
        try {
            Class<?> targetClass = getClassLoader().loadClass(target);

            Loge.e(targetClass.getName());
            ActivityInterface activityInterface = (ActivityInterface) targetClass.newInstance();
            // 给插件注入宿主环境
            activityInterface.insertAppContext(this);
            // 调用插件 Activity 的 onCreate()
            activityInterface.onCreate(savedInstanceState);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public Resources getResources() {
//        return Plugin_Uitl.getResources();
//    }
}
