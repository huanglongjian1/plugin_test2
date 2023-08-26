package com.android.plugin_test3;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.plugin.BaseActivity;

import java.util.Random;

public class Test3_Activity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test3_activity);
        TextView textView = findViewById(R.id.test3_tv);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(new Random().nextInt(Integer.MAX_VALUE) + ":随机数字");
            }
        });
    }
}
