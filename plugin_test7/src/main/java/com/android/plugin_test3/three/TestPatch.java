package com.android.plugin_test3.three;

import android.util.Log;

public class TestPatch {
    public void e() {
        Log.e("补丁:", "e:测试一,被修复了,第二次");
    }

    private void d() {
        Log.e("补丁:", "d:私有,被修复了，第二次");
    }
}
