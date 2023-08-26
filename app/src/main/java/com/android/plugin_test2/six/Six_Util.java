package com.android.plugin_test2.six;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

import com.android.plugin_test2.R;
import com.android.plugin_test2.util.Loge;
import com.android.plugin_test2.util.Reflector;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Six_Util {
    public static Resources m_resources;

    public static void mergePluginResources_02(Context context, String apkName) {
        Reflector.on(AssetManager.class).method("addAssetPath", String.class).with(context.getAssets())
                .call(context.getPackageResourcePath());
        Reflector.on(AssetManager.class).method("addAssetPath", String.class).with(context.getAssets())
                .call(apkName);

    }

    public static void mergePluginResources(Application application, String apkName)
            throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, NoSuchFieldException {
        // 创建一个新的 AssetManager 对象
        AssetManager newAssetManagerObj = AssetManager.class.newInstance();
        Method addAssetPath = AssetManager.class.getMethod("addAssetPath", String.class);
        // 塞入原来宿主的资源
        Loge.e(application.toString());
        addAssetPath.invoke(newAssetManagerObj, application.getBaseContext().getPackageResourcePath());

        // 塞入插件的资源
        //File optDexFile = application.getBaseContext().getFileStreamPath(apkName);

        //  addAssetPath.invoke(newAssetManagerObj, optDexFile.getAbsolutePath());
        addAssetPath.invoke(newAssetManagerObj, apkName);

        // ----------------------------------------------

        // 创建一个新的 Resources 对象
        Resources newResourcesObj = new Resources(newAssetManagerObj,
                application.getBaseContext().getResources().getDisplayMetrics(),
                application.getBaseContext().getResources().getConfiguration());
        Loge.e(newAssetManagerObj.toString());
        // ----------------------------------------------
        m_resources = newResourcesObj;
        // 获取 ContextImpl 中的 Resources 类型的 mResources 变量，并替换它的值为新的 Resources 对象
        Field resourcesField = application.getBaseContext().getClass().getDeclaredField("mResources");
        resourcesField.setAccessible(true);
        resourcesField.set(application.getBaseContext(), newResourcesObj);

        // ----------------------------------------------

        // 获取 ContextImpl 中的 LoadedApk 类型的 mPackageInfo 变量
        Field packageInfoField = application.getBaseContext().getClass().getDeclaredField("mPackageInfo");
        packageInfoField.setAccessible(true);
        Object packageInfoObj = packageInfoField.get(application.getBaseContext());

        // 获取 mPackageInfo 变量对象中类的 Resources 类型的 mResources 变量，，并替换它的值为新的 Resources 对象
        // 注意：这是最主要的需要替换的，如果不需要支持插件运行时更新，只留这一个就可以了
        Field resourcesField2 = packageInfoObj.getClass().getDeclaredField("mResources");
        resourcesField2.setAccessible(true);
        resourcesField2.set(packageInfoObj, newResourcesObj);

        // ----------------------------------------------

        // 获取 ContextImpl 中的 Resources.Theme 类型的 mTheme 变量，并至空它
        // 注意：清理mTheme对象，否则通过inflate方式加载资源会报错, 如果是activity动态加载插件，则需要把activity的mTheme对象也设置为null
        Field themeField = application.getBaseContext().getClass().getDeclaredField("mTheme");
        themeField.setAccessible(true);
        themeField.set(application.getBaseContext(), null);
    }

    public static int getResId(Context context, String pluginPath, String apkPackageName, String resName) {

        try {

//在应用安装目录下创建一个名为app_dex文件夹目录,如果已经存在则不创建

            File optimizedDirectoryFile = context.getDir("dex", Context.MODE_PRIVATE);

// 构建插件的DexClassLoader类加载器，参数：

// 1、包含dex的apk文件或jar文件的路径，

// 2、apk、jar解压缩生成dex存储的目录，

// 3、本地library库目录，一般为null，

// 4、父ClassLoader

            //     DexClassLoader dexClassLoader = new DexClassLoader(pluginPath, optimizedDirectoryFile.getPath(), null, ClassLoader.getSystemClassLoader());

//通过使用apk自己的类加载器，反射出R类中相应的内部类进而获取我们需要的资源id

            Class clazz = context.getClassLoader().loadClass(apkPackageName + ".R$mipmap");

            Field field = clazz.getDeclaredField(resName);//得到名为resName的这张图片字段

            return field.getInt(R.id.class);//得到图片id

        } catch (Exception e) {

            e.printStackTrace();

        }

        return 0;

    }

}
