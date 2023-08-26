package com.android.plugin_test7;

import android.content.Context;

/**
 * author: liumengqiang
 * Date : 2019/9/13
 * Description :
 */
public class TestResource implements IBaseInterface {
    @Override
    public String getStringForResId(Context context) {
        return context.getResources().getString(R.string.test_string);
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public String getName() {
        return null;
    }
}

