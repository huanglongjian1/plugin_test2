package com.android.plugin;

import android.app.Activity;
import android.os.Bundle;

public interface ActivityInterface {

    // 给插件传入宿主运行环境
    void insertAppContext(Activity activity);

    void onCreate(Bundle savedInstanceState);

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();
}
