package com.xiaogang.ChildOne.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.xiaogang.ChildOne.R;

/**
 * author: ${zhanghailong}
 * Date: 2014/11/18
 * Time: 15:58
 * 类的功能、说明写在此处.
 */
public class SettingAboutActivity extends BaseActivity implements View.OnClickListener{
    private ImageView aboutback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setabout);
        aboutback = (ImageView) this.findViewById(R.id.aboutback);
        aboutback.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.aboutback:
                finish();
                break;
        }
    }
}
