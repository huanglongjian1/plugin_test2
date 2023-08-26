package com.android.plugin_test2.eight;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.plugin_test2.R;
import com.android.plugin_test2.util.Loge;
import com.android.plugin_test7.IBaseInterface;

public class Eight_Activity extends BaseActivity {
    TextView tv;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.main_tv);

        //带资源文件的调用
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Loge.e("点击了");
                Class mLoadClassDynamic = null;
                try {
                    //第四步：通过反射，获取插件中的类，构造出插件类的对象dynamicObject，然后就可以让插件中的类读取插件中的资源了
                    mLoadClassDynamic = classLoader.loadClass("com.android.plugin_test7.TestResource");
                    Object dynamicObject = mLoadClassDynamic.newInstance();

                    IBaseInterface dynamic = (IBaseInterface) dynamicObject;
                    String content = dynamic.getStringForResId(Eight_Activity.this);
                    tv.setText(content);
                    Toast.makeText(Eight_Activity.this, content + "", Toast.LENGTH_LONG).show();
                    ImageView imageView=findViewById(R.id.four_image);
                    Loge.e(imageView.toString());
                } catch (Exception e) {
                    Log.e("DEMO", "msg:" + e.getMessage());
                }
            }
        });
        loadResources();
    }
}
