package com.xiaogang.ChildOne.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.xiaogang.ChildOne.R;

/**
 * author: ${zhanghailong}
 * Date: 2015/2/20
 * Time: 19:20
 * 类的功能、说明写在此处.
 */
public class AboutActivity extends BaseActivity implements View.OnClickListener {
    private Button about_menu;//返回
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        about_menu = (Button) this.findViewById(R.id.about_menu);
        about_menu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.about_menu:
                finish();
                break;
        }
    }
}
