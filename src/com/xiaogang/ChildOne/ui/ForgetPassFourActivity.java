package com.xiaogang.ChildOne.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.xiaogang.ChildOne.R;

/**
 * author: ${zhanghailong}
 * Date: 2014/11/18
 * Time: 23:30
 * 类的功能、说明写在此处.
 */
public class ForgetPassFourActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back;
    private TextView set;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findpassfour);
        back = (ImageView) this.findViewById(R.id.back);
        back.setOnClickListener(this);
        set = (TextView) this.findViewById(R.id.set);
        set.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back:
                finish();
                break;
            case R.id.set:
                //点此重新登录
                Intent login = new Intent(this, LoginActivity.class);
                startActivity(login);
                break;
        }
    }
}
