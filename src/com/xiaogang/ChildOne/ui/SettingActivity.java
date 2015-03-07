package com.xiaogang.ChildOne.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.xiaogang.ChildOne.ActivityTack;
import com.xiaogang.ChildOne.R;
import com.xiaogang.ChildOne.entity.Account;

/**
 * Created by liuzwei on 2014/11/13.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {
    LinearLayout setPassword;//修改密码
    LinearLayout myPhone;//我的手机号
    LinearLayout babySet;//宝宝设置
    LinearLayout mumSet;//妈妈设置
    LinearLayout boundEmail;//绑定邮箱
//    LinearLayout settingwifiliner;//设置wifi
    LinearLayout about;//
    ImageView back;//返回
    TextView zhanghao;
    Account account ;
    TextView setting_exit;//退出当前账号
    private String identity;
    private TextView setting_question;
    private LinearLayout setting_baobao;
    private LinearLayout setting_mama;
    private LinearLayout setting_teacherliner;//老师设置

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        initView();
        account = getGson().fromJson(sp.getString(Constants.ACCOUNT_KEY, ""), Account.class);
        zhanghao.setText(account.getUser_name());
        identity = getGson().fromJson(sp.getString(Constants.IDENTITY, ""), String.class);
        if(identity.equals("0")){
            setting_question.setText("爸爸设置");
        }
        if(identity.equals("1")){
            setting_question.setText("妈妈设置");
        }
        if(account.getIs_teacher().equals("1")){
            setting_baobao.setVisibility(View.GONE);
            setting_mama.setVisibility(View.GONE);
            setting_teacherliner.setVisibility(View.VISIBLE);
        }
    }

    private void initView(){
        setPassword = (LinearLayout) findViewById(R.id.setting_password);
        myPhone = (LinearLayout) findViewById(R.id.setting_phonenumber);
        babySet = (LinearLayout) findViewById(R.id.setting_baobao);
        mumSet = (LinearLayout) findViewById(R.id.setting_mama);
        boundEmail = (LinearLayout) findViewById(R.id.setting_bound_email);
        about = (LinearLayout) findViewById(R.id.setting_about);
        back = (ImageView) findViewById(R.id.setting_back);
        zhanghao = (TextView) this.findViewById(R.id.zhanghao);
        setting_exit = (TextView) this.findViewById(R.id.setting_exit);
        setting_exit.setOnClickListener(this);
        setPassword.setOnClickListener(this);
        myPhone.setOnClickListener(this);
        babySet.setOnClickListener(this);
        mumSet.setOnClickListener(this);
        boundEmail.setOnClickListener(this);
        about.setOnClickListener(this);
        back.setOnClickListener(this);
        setting_question = (TextView) this.findViewById(R.id.setting_question);
        setting_baobao = (LinearLayout) this.findViewById(R.id.setting_baobao);
        setting_mama = (LinearLayout) this.findViewById(R.id.setting_mama);
//        settingwifiliner = (LinearLayout) this.findViewById(R.id.settingwifiliner);
//        settingwifiliner.setOnClickListener(this);
        setting_teacherliner = (LinearLayout) this.findViewById(R.id.setting_teacherliner);
        setting_teacherliner.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting_password://设置密码
                Intent pass = new Intent(this, SettingPassword.class);
                startActivity(pass);
                break;
            case R.id.setting_phonenumber://我的手机号
                Intent mobile = new Intent(this, SettingMobileActivity.class);
                startActivity(mobile);
                break;
            case R.id.setting_baobao://宝宝设置
                Intent babySet = new Intent(SettingActivity.this, BabySettingActivity.class);
                startActivity(babySet);
                break;
            case R.id.setting_mama://家长设置
                Intent mumSet = new Intent(SettingActivity.this, MumSettingActivity.class);
                startActivity(mumSet);
                break;
            case R.id.setting_bound_email://绑定邮箱
                Intent email = new Intent(this, SettingEmailActivity.class);
                startActivity(email);
                break;
            case R.id.setting_about://关于
                Intent about = new Intent(this, SettingAboutActivity.class);
                startActivity(about);
                break;
            case R.id.setting_back://返回按钮
                finish();
                break;
            case R.id.setting_exit:
//                ShellContext.clear();
                ActivityTack.getInstanse().popUntilActivity(LoginActivity.class);
                break;
//            case R.id.settingwifiliner:
//                Intent wifi = new Intent(this,SetWifiActivity.class);
//                startActivity(wifi);
//                break;
            case R.id.setting_teacherliner:
                //老师设置
                Intent teacher = new Intent(this,TeacherSettingActivity.class);
                startActivity(teacher);
                break;
        }
    }
}
