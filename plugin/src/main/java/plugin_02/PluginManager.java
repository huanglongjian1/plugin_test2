package plugin_02;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class PluginManager {
    private static class Instance {
        static final PluginManager INSTANCE = new PluginManager();
    }

    private PluginManager() {
    }

    public static PluginManager getInstance() {
        return Instance.INSTANCE;
    }

    private Context mContext;
    private PluginApk mPluginApk;

    public PluginManager init(Context context) {
        //避免单例对象引起内存泄漏
        mContext = context.getApplicationContext();
        return this;
    }

    /**
     * 根据APK 路径实例化PluginApk对象
     *
     * @param path
     */
    public void loadPluginApk(String path) {
        PackageInfo packageInfo = mContext.getPackageManager().getPackageArchiveInfo(path,
                PackageManager.GET_ACTIVITIES | PackageManager.GET_SERVICES);
        if (packageInfo == null) {
            return;
        }
        DexClassLoader classLoader = createDexClassLoader(path);
        AssetManager assetManager;
        try {
            assetManager = createAssetManager(path);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        Resources resources = createResource(assetManager);
        mPluginApk = new PluginApk(packageInfo, classLoader, resources);
    }

    public PluginApk getPluginApk() {
        return mPluginApk;
    }

    /**
     * 创建访问插件APK dex文件的类加载器
     *
     * @param path
     * @return
     */
    private DexClassLoader createDexClassLoader(String path) {
        /**
         * 在宿主APK的内部存储中的data/data/包名 目录上创建一个文件夹，存放优化后的文件
         */
        File file = mContext.getDir("odex", Context.MODE_PRIVATE);
        return new DexClassLoader(path, file.getAbsolutePath(), null, mContext.getClassLoader());
    }

    private AssetManager createAssetManager(String path) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        /**
         * AssetManager的构造方和addAssetPath方法都是hide的，需要使用反射构造
         */
        AssetManager assetManager = AssetManager.class.newInstance();
        Method method = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
        method.invoke(assetManager, path);
        return assetManager;
    }

    /**
     * 创建访问插件APK资源的Resource
     *
     * @param assetManager
     * @return
     */
    private Resources createResource(AssetManager assetManager) {
        Resources resources = mContext.getResources();
        return new Resources(assetManager, resources.getDisplayMetrics(), resources.getConfiguration());
    }
}