package com.android.plugin_test2.clip;

import android.os.IBinder;
import android.os.IInterface;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyBinder implements InvocationHandler {
    IBinder base;
    Class<?> stub;
    Class<?> iinterface;

    public ProxyBinder(IBinder base) {
        this.base = base; //（1）
        try {
            this.stub = Class.forName("android.content.IClipboard$Stub");
            this.iinterface = Class.forName("android.content.IClipboard");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("queryLocalInterface".equals(method.getName())) { //（3）
            return Proxy.newProxyInstance(proxy.getClass().getClassLoader(),//（4）
                    // asInterface 的时候会检测是否是特定类型的接口然后进行强制转换
                    // 因此这里的动态代理生成的类型信息的类型必须是正确的，即必须是以下3个接口实例
                    new Class[]{IBinder.class, IInterface.class, this.iinterface},
                    new FixBinder(base, stub));
        }
        return method.invoke(base, args);
    }
}
