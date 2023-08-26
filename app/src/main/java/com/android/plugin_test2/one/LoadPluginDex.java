package com.android.plugin_test2.one;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

import dalvik.system.DexClassLoader;

public class LoadPluginDex {
    public static PackageInfo packageInfo;


    public static void mergeHostAndPluginDex(Context context, String pluginPath) {

        if (TextUtils.isEmpty(pluginPath)) {
            throw new IllegalArgumentException("插件路径不能拿为空！");
        }

        try {

            Class<?> clazz = Class.forName("dalvik.system.BaseDexClassLoader");
            Field pathListField = clazz.getDeclaredField("pathList");
            pathListField.setAccessible(true);

            Class<?> dexPathListClass = Class.forName("dalvik.system.DexPathList");
            Field dexElements = dexPathListClass.getDeclaredField("dexElements");
            dexElements.setAccessible(true);

            // 1.获取宿主的ClassLoader中的 dexPathList 在从 dexPathList 获取 dexElements
            ClassLoader pathClassLoader = context.getClassLoader();

            Object dexPathList = pathListField.get(pathClassLoader);
            Object[] hostElements = (Object[]) dexElements.get(dexPathList);
            // 2.获取插件的 dexElements
            DexClassLoader dexClassLoader = new DexClassLoader(pluginPath,
                    context.getCacheDir().getAbsolutePath(), null, pathClassLoader);

            packageInfo = context.getPackageManager().getPackageArchiveInfo(pluginPath, PackageManager.GET_ACTIVITIES);
            Object pluginPathList = pathListField.get(dexClassLoader);
            Object[] pluginElements = (Object[]) dexElements.get(pluginPathList);

            // 3.先创建一个空的新数组
            Object[] allElements = (Object[]) Array.newInstance(hostElements.getClass().getComponentType(),
                    hostElements.length + pluginElements.length);

            //4把插件和宿主的Elements放进去
            System.arraycopy(hostElements, 0, allElements, 0, hostElements.length);
            System.arraycopy(pluginElements, 0, allElements, hostElements.length, pluginElements.length);

            // 5.把宿主的classloader 的 dexPathList 中的dexElements 换成 allElements
            dexElements.set(dexPathList, allElements);
        } catch (Exception e) {
            Log.d("zjs", "e", e);
            e.printStackTrace();
        }

    }


}
