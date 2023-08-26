package com.android;

import android.content.Context;
import android.content.res.AssetManager;

import com.android.plugin_test2.util.Reflector;

import java.lang.reflect.Array;

import dalvik.system.BaseDexClassLoader;

public class PluginUtils {
    public static int getDrawableId(Context context, String defType, String apkPackageName) {
        return context.getResources().getIdentifier(defType, "drawable", apkPackageName);
    }

    public static void mergePluginResources(Context context, String apkPath) {
        Reflector.on(AssetManager.class).method("addAssetPath", String.class).with(context.getAssets())
                .call(context.getPackageResourcePath());
        Reflector.on(AssetManager.class).method("addAssetPath", String.class).with(context.getAssets())
                .call(apkPath);
    }

    public static void inject(BaseDexClassLoader loader, Context context, String path) {
        Object pathList_suzhu = getPathList(Thread.currentThread().getContextClassLoader());
        Object pathList_chajian = getPathList(loader);
        Object dexElements = combineArray(getDexElements(pathList_suzhu), getDexElements(pathList_chajian));
       //插件和打补丁的区别就是dexElements插入的时候 插件和宿主 的先后。
        Reflector.on(pathList_suzhu.getClass()).field("dexElements").with(pathList_suzhu).set(dexElements);
    }

    public static void inject_patch(BaseDexClassLoader loader) {
        Object pathList_suzhu = getPathList(Thread.currentThread().getContextClassLoader());
        Object pathList_chajian = getPathList(loader);
        Object dexElements = combineArray(getDexElements(pathList_chajian), getDexElements(pathList_suzhu));
        Reflector.on(pathList_suzhu.getClass()).field("dexElements").with(pathList_suzhu).set(dexElements);
    }

    private static Object getPathList(Object classloader) {
        Object pathList_obj = Reflector.on(BaseDexClassLoader.class).field("pathList").with(classloader).get();
        return pathList_obj;
    }

    private static Object getDexElements(Object pathList) {
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
}
