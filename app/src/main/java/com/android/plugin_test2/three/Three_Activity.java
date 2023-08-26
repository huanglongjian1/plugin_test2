package com.android.plugin_test2.three;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.plugin_test2.R;
import com.android.plugin_test2.util.Loge;
import com.android.plugin_test2.util.Reflector;

import java.io.File;
import java.io.FileFilter;

import dalvik.system.DexClassLoader;

public class Three_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //  hookMH();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.main_tv);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Three_Activity.this, Proxy_Activity_02.class);
                intent.putExtra("className", "com.android.plugin_test2.Plugin_Test2_Activity");
                startActivity(intent);
            }
        });
        loadApk();
    }

    private void loadApk() {
        File dex = getExternalFilesDir("apk_file").listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getAbsolutePath().endsWith(".apk");
            }
        })[0];
        if (dex.exists()) {
            DexClassLoader dexClassLoader = new DexClassLoader(dex.getAbsolutePath(), getExternalCacheDir().getAbsolutePath(), null, getClassLoader());
            Plugin_Uitl.inject(dexClassLoader, this, dex.getAbsolutePath());

            Plugin_Uitl.hookResources(this, dex.getAbsolutePath());
        }
    }

    private void hookMH() {
        Object currentActivityThread = Reflector.on("android.app.ActivityThread").field("sCurrentActivityThread")
                .get();
        Object mH_obj = Reflector.on(currentActivityThread.getClass()).field("mH")
                .with(currentActivityThread).get();
        Loge.e(mH_obj.toString());
        Loge.e(currentActivityThread.toString());
        Reflector.on(Handler.class).field("mCallback")
                .with(mH_obj)
                .set(new MymH((Handler) mH_obj));
    }

    private class MymH implements Handler.Callback {
        private Handler mH;

        public MymH(Handler mh) {
            mH = mh;
        }

        @Override
        public boolean handleMessage(@NonNull Message msg) {
            Loge.e("hook------mh" + msg.what);
            //  mH.handleMessage(msg);
            return false;
        }
    }


}
