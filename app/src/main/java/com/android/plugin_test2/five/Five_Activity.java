package com.android.plugin_test2.five;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.android.plugin_test2.R;
import com.android.plugin_test2.six.Six_Util;
import com.android.plugin_test2.three.Plugin_Uitl;
import com.android.plugin_test2.util.Assets_Util;
import com.android.plugin_test2.util.Loge;

import java.lang.reflect.InvocationTargetException;

import plugin_02.PluginApk;
import plugin_02.PluginManager;

public class Five_Activity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.main_tv);
        textView.setText("Five_Activity_tv");
        textView.setOnClickListener(this);
        ImageView imageView = findViewById(R.id.four_image);
        imageView.setImageResource(R.drawable.ic_launcher_background);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PluginApk pluginApk = PluginManager.getInstance().getPluginApk();
                PackageInfo packageInfo = pluginApk.getmPackageInfo();
                Loge.e(packageInfo.packageName);


                Plugin_Uitl.inject(pluginApk.getmClassLoader(), Five_Activity.this, sp.getString("path", ""));

                try {
                    Six_Util.mergePluginResources(getApplication(), packageInfo.packageName);
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


                int id = Six_Util.getResId(Five_Activity.this, sp.getString("path", ""), packageInfo.packageName, "qq");
                Loge.e(id + "-----------------");
                Drawable drawable=pluginApk.getmResources().getDrawable(id);
               // imageView.setBackground(drawable);
                Loge.e(drawable.toString());

                ViewGroup.LayoutParams layoutParams=imageView.getLayoutParams();
                layoutParams.width= 2000;
                layoutParams.height=3000;
                imageView.setLayoutParams(layoutParams);
                imageView.setImageResource(R.drawable.ic_launcher_background);
            }
        });

    }

    SharedPreferences sp;

    @Override
    public void onClick(View v) {
        sp = getSharedPreferences("APK", Context.MODE_PRIVATE);
        boolean load = sp.getBoolean("load", false);
        if (load) {
            String path = sp.getString("path", "");
            intentActivity(path);
        } else {
            //模拟下载过程
            new LoadApk().execute();
        }
    }

    class LoadApk extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            return Assets_Util.copyAssetFile2APPCache(Five_Activity.this, "didi.apk");
        }

        @Override
        protected void onPostExecute(String path) {
            super.onPostExecute(path);
            intentActivity(path);
        }
    }

    private void intentActivity(String path) {
        Toast.makeText(Five_Activity.this, path, Toast.LENGTH_LONG).show();
        if (TextUtils.isEmpty(path)) {
        } else {
            PluginManager.getInstance()
                    .init(Five_Activity.this)
                    .loadPluginApk(path);//如果插件APK是放在SD卡上，那这里的path就是文件路径，也就不需要上面的拷贝操作了


            Intent intent = new Intent(Five_Activity.this, ProxyActivity.class);
            intent.putExtra("classname", "com.android.plugin_test6.Plugin_Test6_Activity");
            startActivity(intent);
        }
    }
}
