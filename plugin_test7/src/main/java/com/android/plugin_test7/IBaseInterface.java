package com.android.plugin_test7;

import android.content.Context;

public interface IBaseInterface {
    void setName(String name);

    String getName();

    String getStringForResId(Context context);
}
