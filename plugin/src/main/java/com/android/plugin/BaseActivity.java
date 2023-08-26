package com.android.plugin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.view.ContextThemeWrapper;

import java.lang.reflect.Field;

public class BaseActivity extends Activity implements ActivityInterface {
    protected Context mContext;
    protected Activity hostActivity;

    @Override
    public void insertAppContext(Activity activity) {
        hostActivity = activity;
    }

    // 由于插件没有运行环境，所以像 context、super、this 这些是不能使用的，因此
    // 无法调用 super.onCreate()，只能添加 @SuppressLint("MissingSuperCall")
    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //这里context传getApplication；1.避免递归调用activity.getResources方法；2.避免内存泄漏
//        Resources resources = ResourcesUtils.getInstance().loadResources(hostActivity);
//        //这里避免使用宿主中的context对象，否则会存在资源找不到的情况，因为去找的是宿主中的资源【使用宿主context】，自己新建context，去加载自己的资源
//        mContext = new ContextThemeWrapper(getBaseContext(), 0);
//        try {
//            //反射替换自定义context的resources对象
//            Field mResourcesField = ContextThemeWrapper.class.getDeclaredField("mResources");
//            mResourcesField.setAccessible(true);
//            mResourcesField.set(mContext, resources);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onStart() {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onResume() {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onPause() {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onStop() {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onDestroy() {

    }

    @Override
    public void setContentView(int layoutResID) {
        if (hostActivity != null) {
            hostActivity.setContentView(layoutResID);
        }
    }

    @Override
    public <T extends View> T findViewById(int id) {
        if (hostActivity != null) {
            return hostActivity.findViewById(id);
        }
        // super 其实是用不了的
        return super.findViewById(id);
    }
}
