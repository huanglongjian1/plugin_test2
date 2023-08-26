package com.android.plugin_test7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class Plugin_Test7_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin_test7);
        TextView textView = findViewById(R.id.plugin_test7_tv);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(new Random().nextInt(Integer.MAX_VALUE) + ":" + getResources().getString(R.string.test_string));
            }
        });
    }
}