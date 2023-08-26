package com.android.plugin_test2.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Assets_Util {
    public static String copyAssetFile2APPCache(Context context, String fileName){
        //获取应用内部存储中私有缓冲目录 data/data/包名/cache
        File cacheDir = context.getCacheDir();
        if (cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        try {
            File outFile = new File(cacheDir,fileName);
            if (outFile.exists()) {
                return outFile.getAbsolutePath();
            } else {
                BufferedInputStream bis = new BufferedInputStream(context.getResources().getAssets().open(fileName));
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outFile));
                int len;
                byte[] buff = new byte[1024*8];
                while ((len = bis.read(buff)) != -1) {
                    bos.write(buff,0,len);
                }
                bos.flush();
                bis.close();
                bos.close();
                SharedPreferences sp = context.getSharedPreferences("APK",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("load",true);
                editor.putString("path",outFile.getAbsolutePath());
                editor.commit();
                return outFile.getAbsolutePath();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
