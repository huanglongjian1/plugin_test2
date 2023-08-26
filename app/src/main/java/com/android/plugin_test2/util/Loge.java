package com.android.plugin_test2.util;

import android.util.Log;

public class Loge {
    public static void e(String s) {
        String className = Thread.currentThread().getStackTrace()[3].getClassName();
        String simpleClassName = className.substring(className.lastIndexOf(".") + 1, className.length());
        String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
        Log.e(simpleClassName + "*" + methodName + "------", s);
    }

}
