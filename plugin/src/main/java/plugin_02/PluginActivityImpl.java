package plugin_02;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class PluginActivityImpl extends AppCompatActivity implements IPluginActivity {
    private int from = FROM_INTERNAL;
    //赋予当前Activity上下文
    private Activity mProxyActivity;
    @Override
    public void attach(Activity proxyActivity) {
        mProxyActivity = proxyActivity;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            from = savedInstanceState.getInt("from");
        }
        if (from == FROM_INTERNAL) {
            super.onCreate(savedInstanceState);
            mProxyActivity = this;
        }
    }
    @Override
    public void setContentView(int layoutResID) {
        if (from == FROM_INTERNAL) {
            super.setContentView(layoutResID);
        } else {
            mProxyActivity.setContentView(layoutResID);
        }
    }
    @Override
    public View findViewById(int id) {
        if (from == FROM_INTERNAL) {
            return super.findViewById(id);
        } else {
            return mProxyActivity.findViewById(id);
        }
    }
    @Override
    public void onStart() {
        if (from == FROM_INTERNAL) {
            super.onStart();
        }
    }
    @Override
    public void onRestart() {
        if (from == FROM_INTERNAL) {
            super.onRestart();
        }
    }
    @Override
    public void onResume() {
        if (from == FROM_INTERNAL) {
            super.onResume();
        }
    }
    @Override
    public void onPause() {
        if (from == FROM_INTERNAL) {
            super.onPause();
        }
    }
    @Override
    public void onStop() {
        if (from == FROM_INTERNAL) {
            super.onStop();
        }
    }
    @Override
    public void onDestroy() {
        if (from == FROM_INTERNAL) {
            super.onDestroy();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (from == FROM_INTERNAL) {
            super.onActivityResult(requestCode,resultCode,data);
        }
    }
}