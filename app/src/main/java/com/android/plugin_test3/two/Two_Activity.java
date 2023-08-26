package com.android.plugin_test3.two;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.PluginUtils;
import com.android.plugin_test2.R;
import com.android.plugin_test2.util.Loge;
import com.android.plugin_test7.IBaseInterface;

import java.io.File;
import java.io.FileFilter;

import dalvik.system.PathClassLoader;

public class Two_Activity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.main_tv);
        textView.setText("Two_Activity");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    IBaseInterface iBaseInterface = (IBaseInterface) Class.forName("com.android.plugin_test7.TestResource").newInstance();
                    Loge.e(iBaseInterface.getStringForResId(Two_Activity.this));

//                    int resID_wx = PluginUtils.getDrawableId(Two_Activity.this, "wx", packageInfo.packageName);
                    ImageView imageView = findViewById(R.id.four_image);
                    imageView.setImageResource(com.android.plugin_test7.R.drawable.wx);

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        loadAPK();
    }

    private PackageInfo packageInfo;

    private void loadAPK() {
        File apk_dir = getFilesDir();
        File[] apk_files = apk_dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getAbsolutePath().endsWith(".apk");
            }
        });
        String apk_path = apk_files[0].getAbsolutePath();
        Loge.e(apk_path);
        packageInfo = getPackageManager().getPackageArchiveInfo(apk_path, PackageManager.GET_ACTIVITIES | PackageManager.GET_SERVICES);
        PathClassLoader pathClassLoader = new PathClassLoader(apk_path, getClassLoader());
        PluginUtils.inject(pathClassLoader, this, apk_path);
        PluginUtils.mergePluginResources(this, apk_path);
    }
}
