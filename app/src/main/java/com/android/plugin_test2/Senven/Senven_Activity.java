package com.android.plugin_test2.Senven;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.plugin_test2.MyApplication;
import com.android.plugin_test2.R;
import com.android.plugin_test2.util.Loge;
import com.android.plugin_test7.IBaseInterface;

public class Senven_Activity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.main_tv);
        textView.setText("senven_activity_tv");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Loge.e("点击");
                try {
                    MyApplication myApplication= (MyApplication) getApplication();
                    Class<?> iBaseInterface_clazz =myApplication.getDexClassLoader().loadClass("com.android.plugin_test7.TestResource");
                    IBaseInterface iBaseInterface = (IBaseInterface) iBaseInterface_clazz.newInstance();
                    Loge.e(iBaseInterface.toString());
                    String s = iBaseInterface.getStringForResId(myApplication);
                    Loge.e(s);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
