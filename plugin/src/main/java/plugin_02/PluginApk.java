package plugin_02;

import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;

import dalvik.system.DexClassLoader;

public class PluginApk {
    private PackageInfo mPackageInfo;
    private DexClassLoader mClassLoader;
    private Resources mResources;
    private AssetManager mManager;
    public PluginApk(PackageInfo mPackageInfo, DexClassLoader mClassLoader, Resources mResources) {
        this.mPackageInfo = mPackageInfo;
        this.mClassLoader = mClassLoader;
        this.mResources = mResources;
        if (mResources != null) {
            mManager = mResources.getAssets();
        }
    }

    public PackageInfo getmPackageInfo() {
        return mPackageInfo;
    }

    public DexClassLoader getmClassLoader() {
        return mClassLoader;
    }

    public Resources getmResources() {
        return mResources;
    }

    public AssetManager getmManager() {
        return mManager;
    }
}