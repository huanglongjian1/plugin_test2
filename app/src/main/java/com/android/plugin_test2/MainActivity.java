package com.android.plugin_test2;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.plugin_test2.clip.ProxyBinder;
import com.android.plugin_test2.hook.AmsInvocationHandler;
import com.android.plugin_test2.one.SubActivity;
import com.android.plugin_test2.util.Loge;
import com.android.plugin_test2.util.Reflector;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.main_tv);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        try {
            hookClick(textView);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void hookclip() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        final String CLIPBOARD_SERVICE = "clipboard";
// 下面这一段的意思实际就是: ServiceManager.getService("clipboard");
        Class<?> serviceManager = Class.forName("android.os.ServiceManager");
        Method getService = serviceManager.getDeclaredMethod("getService", String.class);
// （1）ServiceManager里面管理的原始的Clipboard Binder对象
        IBinder rawBinder = (IBinder) getService.invoke(null, CLIPBOARD_SERVICE);
//（2） Hook 掉这个Binder代理对象的 queryLocalInterface 方法
        IBinder hookedBinder = (IBinder) Proxy.newProxyInstance(serviceManager.getClassLoader(),
                new Class<?>[]{IBinder.class},
                new ProxyBinder(rawBinder));
// （3）把这个hook过的Binder代理对象放进ServiceManager的cache里面
        Field cacheField = serviceManager.getDeclaredField("sCache");
        cacheField.setAccessible(true);
        Map<String, IBinder> cache = (Map) cacheField.get(null);
        cache.put(CLIPBOARD_SERVICE, hookedBinder);

    }

    private void hookClick(View view) throws IllegalAccessException {
        Object mListenerInfo = Reflector.on(View.class).method("getListenerInfo").with(view).call();
        Log.e("--", mListenerInfo.getClass().getName());
        Reflector.on(mListenerInfo.getClass()).field("mOnClickListener").with(mListenerInfo).set(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("----", "===");
                try {
                    HookAMS();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                startActivity(new Intent(MainActivity.this, SubActivity.class));
            }
        });
    }

    private void getInstrumentation() {
        Object mInstrumentation = Reflector.on(Activity.class).field("mInstrumentation").with(this).get();
        //  Reflector.on(Activity.class).field("mInstrumentation").with(this).set(new InstrumentationProxy((Instrumentation) mInstrumentation));
    }

    private void HookAMS() throws ClassNotFoundException {
        Object iActivityManagerSingletonObject=Reflector.on(ActivityManager.class).field("IActivityManagerSingleton")
                .get();
        Object mInstanceObject=Reflector.on("android.util.Singleton").field("mInstance")
                .with(iActivityManagerSingletonObject).get();
        AmsInvocationHandler amsInvocationHandler=new AmsInvocationHandler(this,mInstanceObject);
        Object proxy=Proxy.newProxyInstance(getClassLoader(),new Class[]{Class.forName("android.app.IActivityManager")},amsInvocationHandler);
    }

}