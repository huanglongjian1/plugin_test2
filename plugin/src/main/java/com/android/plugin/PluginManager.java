package com.android.plugin;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 * @author: Q
 * @Description: 插件APK的加载类，用来加载第三方插件APK
 * @DateTime: 2023/7/8 14:42
 **/
public class PluginManager {
    //创建单例
    private static final PluginManager pluginManager = new PluginManager();

    //插件APK中的资源对象
    private Resources resources;

    //类加载器，用于加载插件APK中的类
    private DexClassLoader dexClassLoader;

    //上下文
    private Context context;

    //插件apk中的包信息管理器
    private PackageInfo packageInfo;

    //构造函数
    public PluginManager() {
    }

    //单例getter
    public static PluginManager getInstance() {
        return pluginManager;
    }

    //设置上下文
    public PluginManager setContext(Context context) {
        this.context = context;
        return this;
    }

    /**
     * @param path 插件PAK路径
     * @Author: Q
     * @Description: 从传入的路径中加载第三方插件APK
     * @DateTime: 15:30 2023/7/8
     */
    public void loadFromPath(String path) {
        //获取当前应用的私有存储路径，创建dexClassLoader时要用到
        File dexOutFile = context.getDir("dex", Context.MODE_PRIVATE);
        //初始化类加载器，参数1：dex的路径，参数2：当前应用的私有目录，基本是写死的。参数三：插件框架的路径，参数4：全局的ClassLoader，基本是写死的
        dexClassLoader = new DexClassLoader(path, dexOutFile.getAbsolutePath(), null, context.getClassLoader());
        //获取包管理器
        PackageManager packageManager = context.getPackageManager();
        //通过包管理器去获取插件APK中的包信息对象，参数1：插件APK的路径。参数2：要获取的类型flags
        packageInfo = packageManager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        //实例化Resources
        //首先要获取到AssetManager实例，因为 AssetManager是私有类，所以用到反射获取
        try {
            //通过反射拿到assetManager实例
            AssetManager assetManager = AssetManager.class.newInstance();
            //此时实例是空的，需要通过反射获取到他的方法：addAssetPath
            Method addAssetPath = AssetManager.class.getMethod("addAssetPath", String.class);
            //执行获取到的方法
            addAssetPath.invoke(assetManager, path);
            //将resources实例化，参数1：AssetManager,参数2和参数3都是计量工具，直接用上下文点就可以获取。
            resources = new Resources(assetManager, context.getResources().getDisplayMetrics(), context.getResources().getConfiguration());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Resources、DexClassLoader、PackageInfo设置get方法


    public Resources getResources() {
        return resources;
    }

    public DexClassLoader getDexClassLoader() {
        return dexClassLoader;
    }

    public PackageInfo getPackageInfo() {
        return packageInfo;
    }
}