package com.android.plugin_test2.four;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.plugin.ActivityInterface;
import com.android.plugin_test2.three.Plugin_Uitl;
import com.android.plugin_test2.util.Loge;

public class Proxy_Activity_04 extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String target = getIntent().getStringExtra("className");
        Loge.e(target);
        try {
            Class plugin_clazz = Class.forName(target);
            ActivityInterface activityInterface = (ActivityInterface) plugin_clazz.newInstance();
            activityInterface.insertAppContext(this);
            activityInterface.onCreate(savedInstanceState);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Resources getResources() {



        return Plugin_Uitl.getResources();
    }
}
