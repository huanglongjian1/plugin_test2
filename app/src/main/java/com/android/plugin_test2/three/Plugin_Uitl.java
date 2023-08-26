package com.android.plugin_test2.three;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

import androidx.appcompat.app.AppCompatActivity;

import com.android.plugin_test2.util.Loge;
import com.android.plugin_test2.util.Reflector;

import java.lang.reflect.Array;
import java.lang.reflect.Method;

import dalvik.system.BaseDexClassLoader;
import dalvik.system.DexClassLoader;

public class Plugin_Uitl {
    public static Object getPathList(Object classloader) {
        Object pathList_obj = Reflector.on(BaseDexClassLoader.class).field("pathList").with(classloader).get();
        return pathList_obj;
    }

    public static Object getDexElements(Object pathList) {
        return Reflector.on(pathList.getClass()).field("dexElements").with(pathList).get();
    }

    private static Object combineArray(Object suzhu, Object chajian) {
        //获取原数组类型
        Class<?> localClass = suzhu.getClass().getComponentType();
        //获取原数组长度
        int i = Array.getLength(suzhu);
        //插件数组加上原数组的长度
        int j = i + Array.getLength(chajian);
        //创建一个新的数组用来存储
        Object result = Array.newInstance(localClass, j);
        //一个个的将dex文件设置到新数组中
        for (int k = 0; k < j; ++k) {
            if (k < i) {
                Array.set(result, k, Array.get(suzhu, k));
            } else {
                Array.set(result, k, Array.get(chajian, k - i));
            }
        }
        return result;
    }

    public static void inject_2(BaseDexClassLoader loader, Context context, String path) {
        Object pathList_suzhu = getPathList(Thread.currentThread().getContextClassLoader());
        Object pathList_chajian = getPathList(loader);
        Object dexElements = combineArray(getDexElements(pathList_suzhu), getDexElements(pathList_chajian));
        Reflector.on(pathList_suzhu.getClass()).field("dexElements").with(pathList_suzhu).set(dexElements);
    }

    public static void inject(BaseDexClassLoader loader, Context context, String path) {
        Object pathList_suzhu = getPathList(Thread.currentThread().getContextClassLoader());
        Object pathList_chajian = getPathList(loader);
        Object dexElements = combineArray(getDexElements(pathList_suzhu), getDexElements(pathList_chajian));
        Reflector.on(pathList_suzhu.getClass()).field("dexElements").with(pathList_suzhu).set(dexElements);

        dexClassLoader = loader;


        AssetManager assetManager = null;
        try {
            assetManager = AssetManager.class.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        Reflector.on(AssetManager.class).method("addAssetPath", String.class).with(assetManager).call(path);
        resources = new Resources(assetManager, context.getResources().getDisplayMetrics(), context.getResources().getConfiguration());
        Loge.e(resources.toString());
    }

    public static Resources getResources() {
        return resources;
    }

    public static BaseDexClassLoader getDexClassLoader() {
        return dexClassLoader;
    }

    static BaseDexClassLoader dexClassLoader;
    private static Resources resources;

    public static void hookResources(Context context, String dex_Path) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Reflector.on(AssetManager.class).method("addAssetPath", String.class)
                    .with(assetManager).call(dex_Path);
            resources = new Resources(assetManager, context.getResources().getDisplayMetrics(), context.getResources().getConfiguration());


        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

}
