package com.android.plugin_test6;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import plugin_02.PluginActivityImpl;

public class Plugin_Test6_Activity extends AppCompatActivity {
    int index;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin_test6);
        TextView textView = (TextView) findViewById(R.id.test6_tv);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(index++ + "-");
                textView.setBackground(getDrawable(R.mipmap.qq));
            }
        });
    }
}