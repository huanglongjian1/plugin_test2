package com.android.plugin_test4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.plugin.BaseActivity;

import java.util.Random;

public class Plugin_Test4 extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin_test4);
        TextView textView=findViewById(R.id.plugin_test4_tv);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("随机数字"+new Random().nextInt(Integer.MAX_VALUE));
            }
        });
    }
}