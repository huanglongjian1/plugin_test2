package com.android.plugin_test3.one;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.android.plugin_test2.five.ProxyActivity;
import com.android.plugin_test2.util.Loge;
import com.android.plugin_test2.util.Reflector;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

public class HookAMS {

    public static void hookStartActivity(final Context context) {
        try {
            // 获取到 ActivityTaskManager 的 Class 对象
            @SuppressLint("PrivateApi")
            Class<?> amClass = Class.forName("android.app.ActivityManager");
            // 获取到 IActivityTaskManagerSingleton 成员变量
            Field iActivityTaskManagerSingletonField = amClass.getDeclaredField("IActivityManagerSingleton");
            iActivityTaskManagerSingletonField.setAccessible(true);
            // 获取 IActivityTaskManagerSingleton 成员变量的值
            Object IActivityTaskManagerSingleton = iActivityTaskManagerSingletonField.get(null);

            // 获取 getService() 方法
            @SuppressLint("BlockedPrivateApi")
            Method getService = amClass.getDeclaredMethod("getService");
            getService.setAccessible(true);
            // 执行 getService() 方法
            final Object IActivityTaskManager = getService.invoke(null);


            // 获取到 IActivityTaskManager 的 Class 对象
            @SuppressLint("PrivateApi")
            Class<?> iamClass = Class.forName("android.app.IActivityManager");
            // 创建代理类 IActivityTaskManager
            Object proxyIActivityManager = Proxy.newProxyInstance(context.getClassLoader(), new Class[]{iamClass}, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                    if ("startActivity".equals(method.getName())) {
                        Loge.e("====================================================================");
                        Intent proxyIntent = new Intent(context, ProxyActivity.class);
                        // startActivity 第三个参数为 Intent
                        proxyIntent.putExtra("targetIntent", (Intent) args[2]);
                        args[2] = proxyIntent;
                    }
                    return method.invoke(IActivityTaskManager, args);
                }
            });

            // 获取到 Singleton 的 Class 对象
            @SuppressLint("PrivateApi")
            Class<?> sClass = Class.forName("android.util.Singleton");
            // 获取到 mInstance 成员变量
            Field mInstanceField = sClass.getDeclaredField("mInstance");
            mInstanceField.setAccessible(true);
            // 赋值 proxyIActivityManager 给 mInstance 成员变量
            mInstanceField.set(IActivityTaskManagerSingleton, proxyIActivityManager);
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void hookActivityThread() {
        Object sCurrentActivityThread = Reflector.on("android.app.ActivityThread").field("sCurrentActivityThread").get();
        Loge.e(sCurrentActivityThread.toString());
        Object mH = Reflector.on(sCurrentActivityThread.getClass()).field("mH").with(sCurrentActivityThread).get();
        Reflector.on(Handler.class).field("mCallback").with(mH).set(new HandlerProxy());
    }


    /**
     * 静态代理 ActivityThread 中的 final H mH = new H() 成员
     */
    public static class HandlerProxy implements Handler.Callback {

        public static final int EXECUTE_TRANSACTION = 159;

        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == EXECUTE_TRANSACTION) {


                // 反射 android.app.servertransaction.ClientTransaction 类
                // 该类中有如下成员变量
                // private List<ClientTransactionItem> mActivityCallbacks;
                // 这个集合中存放的就是 android.app.servertransaction.LaunchActivityItem 类实例
                // 不能直接获取 LaunchActivityItem 实例 , 否则会出错
                Class<?> clientTransactionClass = null;
                try {
                    clientTransactionClass =
                            Class.forName("android.app.servertransaction.ClientTransaction");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                // 验证当前的 msg.obj 是否是 ClientTransaction 类型 , 如果不是则不进行 Intent 替换
                // 通过阅读源码可知 , 在 ActivityThread 的 mH 中, 处理 EXECUTE_TRANSACTION 信号时
                // 有 final ClientTransaction transaction = (ClientTransaction) msg.obj;
                if (!clientTransactionClass.isInstance(msg.obj)) {
                    return true;
                }

                // 反射获取
                // private List<ClientTransactionItem> mActivityCallbacks; 成员字段
                Field mActivityCallbacksField = null;
                try {
                    mActivityCallbacksField =
                            clientTransactionClass.getDeclaredField("mActivityCallbacks");
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
                // 设置成员字段可见性
                mActivityCallbacksField.setAccessible(true);

                // 反射获取
                // private List<ClientTransactionItem> mActivityCallbacks; 成员字段实例
                Object mActivityCallbacksObject = null;
                try {
                    mActivityCallbacksObject = mActivityCallbacksField.get(msg.obj);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                // 将
                // private List<ClientTransactionItem> mActivityCallbacks; 成员字段实例
                // 强转为 List 类型 , 以用于遍历
                List mActivityCallbacksObjectList = (List) mActivityCallbacksObject;

                for (Object item : mActivityCallbacksObjectList) {
                    Class<?> clazz = item.getClass();

                    // 只处理 LaunchActivityItem 的情况
                    if (clazz.getName().equals("android.app.servertransaction.LaunchActivityItem")) {

                        // 获取 LaunchActivityItem 的 private Intent mIntent; 字段
                        // 该 Intent 中的 Activity 目前是占坑 Activity 即 StubActivity
                        // 需要在实例化之前 , 替换成插件包中的 Activity
                        Field mIntentField = null;
                        try {
                            mIntentField = clazz.getDeclaredField("mIntent");
                        } catch (NoSuchFieldException e) {
                            e.printStackTrace();
                        }
                        mIntentField.setAccessible(true);

                        // 获取 LaunchActivityItem 对象的 mIntent 成员 , 即可得到 Activity 跳转的 Intent
                        Intent intent = null;

                        try {
                            intent = (Intent) mIntentField.get(item);
                            Loge.e(intent.toString());
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }

                        // 获取 启动 插件包 组件的 Intent
                        Intent pluginIntent = intent.getParcelableExtra("pluginIntent");
                        if (pluginIntent != null) {
                            // 使用 包含插件包组件信息的 Intent ,
                            // 替换之前在 Ams 启动之前设置的 占坑 StubActivity 对应的 Intent
                            try {
                                mIntentField.set(item, pluginIntent);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            return false;
        }
    }


}
