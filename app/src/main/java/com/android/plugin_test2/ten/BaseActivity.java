package com.android.plugin_test2.ten;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;

import com.android.plugin_test2.util.Loge;

import java.lang.reflect.Field;

public class BaseActivity extends AppCompatActivity {
    protected Context mContext;
    protected Resources resources;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //这里context传getApplication；1.避免递归调用activity.getResources方法；2.避免内存泄漏
        resources = ResourcesUtils.getInstance().loadResources(getApplication());
        Loge.e(resources.toString());
        //这里避免使用宿主中的context对象，否则会存在资源找不到的情况，因为去找的是宿主中的资源【使用宿主context】，自己新建context，去加载自己的资源
        mContext = new ContextThemeWrapper(getBaseContext(), 0);
        try {
            //反射替换自定义context的resources对象
            Field mResourcesField = ContextThemeWrapper.class.getDeclaredField("mResources");
            mResourcesField.setAccessible(true);
            mResourcesField.set(mContext, resources);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
