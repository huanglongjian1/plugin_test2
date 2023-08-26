package com.android.plugin_test5;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.android.plugin.BaseActivity;

import java.util.Random;

public class Plugin_Test5_Activity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //   setContentView(R.layout.activity_plugin_test5);
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_plugin_test5, null);
        setContentView(view);
        TextView textView = view.findViewById(R.id.plugin_test5_tv);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(new Random().nextInt(Integer.MAX_VALUE)
                        + "随机数字");
            }
        });
    }
}