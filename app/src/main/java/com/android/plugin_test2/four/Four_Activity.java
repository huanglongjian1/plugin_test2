package com.android.plugin_test2.four;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.plugin_test2.R;
import com.android.plugin_test2.three.Plugin_Uitl;
import com.android.plugin_test2.util.Loge;

import java.io.File;
import java.io.FileFilter;
import java.io.OutputStream;
import java.lang.reflect.Field;

import dalvik.system.DexClassLoader;

public class Four_Activity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        loakApK();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.main_tv);
        textView.setText("Four_Activity_Tv");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Four_Activity.this, Proxy_Activity_04.class);
                intent.putExtra("className", "com.android.plugin_test2.Plugin_Test2_Activity");
                startActivity(intent);
            }

        });
        try {
            loadImage();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void loakApK() {
        File dex_file = getExternalFilesDir("apk_file");
        File[] files = dex_file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getAbsolutePath().endsWith(".apk");
            }
        });
        if (!files[0].exists()) {
            return;
        }
        DexClassLoader dexClassLoader = new DexClassLoader(files[0].getAbsolutePath(), getExternalCacheDir().getAbsolutePath(), null, getClassLoader());
        Plugin_Uitl.inject(dexClassLoader, this, files[0].getAbsolutePath());

        PackageInfo packageInfo=getPackageManager().getPackageArchiveInfo(files[0].getAbsolutePath(),PackageManager.GET_ACTIVITIES);
        Loge.e(packageInfo.packageName);

        try {
            pluginContext = createPackageContext(packageInfo.packageName, Context.CONTEXT_IGNORE_SECURITY | Context.CONTEXT_INCLUDE_CODE);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    Context pluginContext;

    private void loadImage() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        ImageView imageView = findViewById(R.id.four_image);
        Class drawable = Class.forName(getPackageName() + ".R$mipmap");
        Field field = drawable.getDeclaredField("ic_launcher");
        int resId = field.getInt(R.id.class);
        imageView.setBackgroundDrawable(Plugin_Uitl.getResources().getDrawable(resId));

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Loge.e(pluginContext.toString());
            }
        });
    }
}
