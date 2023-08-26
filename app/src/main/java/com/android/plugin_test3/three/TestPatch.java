package com.android.plugin_test3.three;

import com.android.plugin_test2.util.Loge;

public class TestPatch {
    public void e() {
        Loge.e("e:测试一");
    }

    private void d() {
        Loge.e("d:私有");
    }
}
