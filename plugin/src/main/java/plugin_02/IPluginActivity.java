package plugin_02;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public interface IPluginActivity {
    //内部跳转
    public static int FROM_INTERNAL = 1;
    //外部跳转
    public static int FROM_EXTERNAL = 2;

    void attach(Activity proxyActivity);

    void onCreate(Bundle savedInstanceState);

    void onStart();

    void onRestart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();

    void onActivityResult(int requestCode, int resultCode, Intent data);
}