package com.android.plugin_test2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.plugin.BaseActivity;
import com.android.plugin.PluginManager;

import java.util.Random;

public class Plugin_Test2_Activity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin_test2);
        TextView textView = findViewById(R.id.plugin_test2_tv);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(new Random().nextInt(Integer.MAX_VALUE) + ":随机数字");
            }
        });
    }


}