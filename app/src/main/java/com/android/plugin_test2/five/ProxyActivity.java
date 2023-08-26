package com.android.plugin_test2.five;

import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.plugin_test2.util.Loge;

import dalvik.system.DexClassLoader;
import plugin_02.IPluginActivity;
import plugin_02.PluginApk;
import plugin_02.PluginManager;

public class ProxyActivity extends AppCompatActivity {
    private String mActivityClassName;
    private PluginApk mPluginApk;
    private IPluginActivity mIPluginActivity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityClassName = getIntent().getStringExtra("classname");
        mPluginApk = PluginManager.getInstance().getPluginApk();
        lunchActivity();
    }
    private void lunchActivity() {
        if (mPluginApk == null) {
            throw new NullPointerException("未获取到插件APK");
        }
        DexClassLoader classLoader = mPluginApk.getmClassLoader();
        try {
            Class clazz = classLoader.loadClass(mActivityClassName);
            //将Activity加载到内存中了
            Object o = clazz.newInstance();
            //判断要跳转到的插件Activity是否实现了规则接口
            if (o instanceof IPluginActivity) {
                mIPluginActivity = (IPluginActivity) o;
                //赋予插件Activity上下文信息
                mIPluginActivity.attach(this);
                Bundle bundle = new Bundle();
                //表明是由宿主Activity跳转过去的
                bundle.putInt("from",IPluginActivity.FROM_EXTERNAL);
                //回调插件Activity的onCreate方法，使其具有生命周期回调
                mIPluginActivity.onCreate(bundle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onStart() {
        //回调插件Activity的onStart方法
        mIPluginActivity.onStart();
        super.onStart();
    }
    @Override
    protected void onRestart() {
        mIPluginActivity.onRestart();
        super.onRestart();
    }
    @Override
    protected void onResume() {
        mIPluginActivity.onResume();
        super.onResume();
    }
    @Override
    protected void onPause() {
        mIPluginActivity.onPause();
        super.onPause();
    }
    @Override
    protected void onStop() {
        mIPluginActivity.onStop();
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        mIPluginActivity.onDestroy();
        super.onDestroy();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mIPluginActivity.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }
    /**
     * 下面这三个对象当前Activity是不具备的
     * 需要返回PluginApk对象的
     * @return
     */
    @Override
    public Resources getResources() {
        return mIPluginActivity == null ? super.getResources() : mPluginApk.getmResources();
    }
    @Override
    public AssetManager getAssets() {
        return mIPluginActivity == null ? super.getAssets() : mPluginApk.getmManager();
    }
    @Override
    public ClassLoader getClassLoader() {
        return mIPluginActivity == null ? super.getClassLoader() : mPluginApk.getmClassLoader();
    }
}