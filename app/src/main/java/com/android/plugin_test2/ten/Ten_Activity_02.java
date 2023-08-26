package com.android.plugin_test2.ten;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;

import com.android.plugin_test2.R;

public class Ten_Activity_02 extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view= LayoutInflater.from(mContext).inflate(R.layout.activity_main,null);
        setContentView(view);
    }

    @Override
    public Resources getResources() {
        return resources == null ? super.getResources() : resources;
    }
}
