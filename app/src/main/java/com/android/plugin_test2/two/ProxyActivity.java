package com.android.plugin_test2.two;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;

import com.android.plugin.ActivityInterface;
import com.android.plugin.PluginManager;

public class ProxyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String target = getIntent().getStringExtra("className");
        try {
            Class<?> targetClass = PluginManager.getInstance().getDexClassLoader().loadClass(target);


            ActivityInterface activityInterface = (ActivityInterface) targetClass.newInstance();
            // 给插件注入宿主环境
            activityInterface.insertAppContext(this);
            // 调用插件 Activity 的 onCreate()
            activityInterface.onCreate(new Bundle());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public Resources getResources() {
        return PluginManager.getInstance().getResources();
    }
}
