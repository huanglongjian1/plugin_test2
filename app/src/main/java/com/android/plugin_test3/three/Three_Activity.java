package com.android.plugin_test3.three;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.PluginUtils;
import com.android.plugin_test2.R;
import com.android.plugin_test2.util.Loge;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Observable;

import dalvik.system.PathClassLoader;

public class Three_Activity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        download_patch("http://www.*******.com/");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.main_tv);
        textView.setText("Three_Activity");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestPatch testPatch = new TestPatch();
                testPatch.e();
            }
        });
    }

    private void download_patch(String url) {
        File dex_patch = getExternalFilesDir("dex_patch");
        if (!dex_patch.exists()) dex_patch.mkdirs();
        Loge.e("使用 okhttp下载补丁");
        dex_Patch();
    }

    private void dex_Patch() {
        File dex_patch = getExternalFilesDir("dex_patch");
        if (!dex_patch.exists()) return;
        File[] files_patch = dex_patch.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getAbsolutePath().endsWith(".dex");
            }
        });
        Arrays.sort(files_patch, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                String name1 = o1.getName().substring(o1.getName().lastIndexOf("_") + 1, o1.getName().lastIndexOf("."));
                String name2 = o2.getName().substring(o2.getName().lastIndexOf("_") + 1, o2.getName().lastIndexOf("."));
                return Integer.valueOf(name1) - Integer.valueOf(name2);
            }
        });
        for (File file : files_patch) {
            PathClassLoader pathClassLoader = new PathClassLoader(file.getAbsolutePath(), getClassLoader());
            PluginUtils.inject_patch(pathClassLoader);
        }
    }
}
