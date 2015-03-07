package com.xiaogang.ChildOne.ui;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import com.xiaogang.ChildOne.R;
import com.xiaogang.ChildOne.widget.EmailDialog;
import com.xiaogang.ChildOne.widget.MobileDialog;


/**
 * author: ${zhanghailong}
 * Date: 2014/11/12
 * Time: 22:15
 * 类的功能、说明写在此处.
 */
public class ForgetPassOneActivity extends BaseActivity implements View.OnClickListener{
    private ImageView forgetback;//返回按钮
    private Button mobilebutton;//电话按钮
    private Button emailbutton;//邮箱按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgetpassone);
        initView();
    }

    private void initView() {
        forgetback = (ImageView) this.findViewById(R.id.forgetback);
        forgetback.setOnClickListener(this);
        mobilebutton = (Button) this.findViewById(R.id.mobilebutton);
        mobilebutton.setOnClickListener(this);
        emailbutton = (Button) this.findViewById(R.id.emailbutton);
        emailbutton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.forgetback://返回按钮
                finish();
                break;
            case R.id.mobilebutton://电话按钮
                MobileDialog dialog = new MobileDialog(this, R.style.dialog1);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();
                break;
            case R.id.emailbutton://邮箱按钮
                EmailDialog emaildialog = new EmailDialog(this, R.style.dialog1);
                emaildialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                emaildialog.show();
                break;
        }
    }
}
