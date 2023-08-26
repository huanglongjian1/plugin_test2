package com.android.plugin_test2.night;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Night_Activity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        String text = "温馨提示:";
        ClipHelper.binder(text);
        EditText editText = new EditText(this);
        setContentView(editText);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
