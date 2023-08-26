package com.android.plugin_test2.two;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.plugin.PluginManager;
import com.android.plugin_test2.R;
import com.android.plugin_test2.util.Loge;

import java.io.File;
import java.io.FileFilter;

public class Two_Activity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.main_tv);
        textView.setText("two_activity");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPlugin();
            }
        });
    }

    private void startPlugin() {
        File apk_file = getExternalFilesDir("apk_file");
        File[] files = apk_file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".apk") | pathname.getName().endsWith(".aar");
            }
        });
        // 待加载插件路径
        String pluginPath = files[0].getAbsolutePath();
        Loge.e(pluginPath);
        PluginManager.getInstance().setContext(this)
                .loadFromPath(pluginPath);
        // 获取插件的 PackageInfo
        PackageInfo packageInfo = PluginManager.getInstance().getPackageInfo();
        if (packageInfo != null) {
            // 需要插件入口 Activity 在 AndroidManifest 中第一个定义
            ActivityInfo activityInfo = packageInfo.activities[0];
            Intent intent = new Intent(this, ProxyActivity.class);
            intent.putExtra("className", activityInfo.name);
            startActivity(intent);
        }
    }


}
