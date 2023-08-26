package com.android.plugin_test2.night;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Binder;
import android.os.IBinder;

import com.android.plugin_test2.TestApp;
import com.android.plugin_test2.util.Loge;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * hook系统剪切板服务
 * Created by 刘镓旗 on 2017/1/22.
 */
public class MyClip implements InvocationHandler {
    private Object mBase;
    String text;

    public MyClip(IBinder base, Class stub, String text) {
        this.text = text;
        //拿到asInteface方法，因为源码中执行了这一句，我们也要执行这一句
        try {
            Method asInterface = stub.getDeclaredMethod("asInterface", IBinder.class);
            mBase = asInterface.invoke(null, base);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //这里我们拦截粘贴的方法，
        if ("getPrimaryClip".equals(method.getName())) {
            Loge.e(args.toString());
            return ClipData.newPlainText(null, text+"我是刘镓旗，我改了系统源码，哈哈哈");
        }
        //再拦截是否有复制的方法，放系统认为一直都有
        if ("hasPrimaryClip".equals(method.getName())) {
            Loge.e(args.toString() + "=");
            return true;
        }
        //其他启动还是返回原有的
        return method.invoke(mBase, args);
    }
}
