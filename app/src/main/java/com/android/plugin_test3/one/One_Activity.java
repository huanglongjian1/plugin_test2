package com.android.plugin_test3.one;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.plugin_test2.R;
import com.android.plugin_test2.six.Six_Util;
import com.android.plugin_test2.three.Plugin_Uitl;
import com.android.plugin_test2.util.Loge;
import com.android.plugin_test7.IBaseInterface;

import java.lang.reflect.InvocationTargetException;

import dalvik.system.PathClassLoader;

public class One_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.main_tv);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    IBaseInterface iBaseInterface = (IBaseInterface) Class.forName("com.android.plugin_test7.TestResource").newInstance();
                    String s = iBaseInterface.getStringForResId(One_Activity.this);
                    Loge.e(s);

                    Drawable drawable = getDrawable(getResources().getIdentifier("wx", "drawable", packageInfo.packageName));
                    // Bitmap bitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("qq", "mipmap", packageInfo.packageName));
                    ImageView imageView = findViewById(R.id.four_image);
                    ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                    layoutParams.height = 2000;
                    layoutParams.width = 2000;
                    imageView.setImageDrawable(drawable);

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }

            }
        });
        try {
            loadAPK();
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

    PackageInfo packageInfo;

    private void loadAPK() throws NoSuchFieldException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {
        String apk_path = getFilesDir() + "/plugin7.apk";
        packageInfo = getPackageManager().getPackageArchiveInfo(apk_path, PackageManager.GET_ACTIVITIES);
        PathClassLoader pathClassLoader = new PathClassLoader(apk_path, getClassLoader());
        Plugin_Uitl.inject_2(pathClassLoader, this, apk_path);
        //   Six_Util.mergePluginResources(getApplication(), apk_path);
        Six_Util.mergePluginResources_02(this, apk_path);
    }

//    @Override
//    public Resources getResources() {
//        //   return Six_Util.m_resources == null ? super.getResources() : Six_Util.m_resources;
//
//        return Six_Util.m_resources == null ? super.getResources() : getApplication().getResources();
//    }
}
