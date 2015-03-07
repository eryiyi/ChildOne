package com.xiaogang.ChildOne.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.xiaogang.ChildOne.R;
import com.xiaogang.ChildOne.data.ErrorDATA;
import com.xiaogang.ChildOne.data.SuccessDATA;
import com.xiaogang.ChildOne.util.InternetURL;
import com.xiaogang.ChildOne.util.ShellContext;
import com.xiaogang.ChildOne.util.StringUtil;

/**
 * author: ${zhanghailong}
 * Date: 2014/11/18
 * Time: 22:25
 * 类的功能、说明写在此处.
 */
public class SettingPassword extends BaseActivity implements View.OnClickListener {
    private ImageView back;//返回
    private EditText password;
    private EditText newpass;
    private EditText surepass;
    private TextView set;//设置

    private String pass ;
    private String newpassword;
    private String surepassword;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingpass);
        initView();
    }

    private void initView() {
        back = (ImageView) this.findViewById(R.id.back);
        back.setOnClickListener(this);
        set = (TextView) this.findViewById(R.id.set);
        set.setOnClickListener(this);
        password = (EditText) this.findViewById(R.id.password);
        newpass = (EditText) this.findViewById(R.id.newpass);
        surepass = (EditText) this.findViewById(R.id.surepass);
        mRequestQueue = Volley.newRequestQueue(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back:
                finish();
                break;
            case R.id.set:
                //设置密码
                pass = password.getText().toString();
                newpassword = newpass.getText().toString();
                surepassword = surepass.getText().toString();
                String str = getGson().fromJson(sp.getString("password", ""), String.class);
                if(StringUtil.isNullOrEmpty(pass)){
                    Toast.makeText(mContext, "请输入原始密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!str.equals(pass)){
                    Toast.makeText(mContext, "原始密码输入错误", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(StringUtil.isNullOrEmpty(newpassword)){
                    Toast.makeText(mContext, "请输入新密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(newpassword.length()<6 || newpassword.length()>18){
                    Toast.makeText(mContext, "请输入正确的密码，6到18位之间", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(StringUtil.isNullOrEmpty(surepassword)){
                    Toast.makeText(mContext, "请输入确认密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!newpassword.equals(surepassword)){
                    Toast.makeText(mContext, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
                updatepass(getGson().fromJson(sp.getString("username", ""), String.class) ,newpassword);
                break;
        }
    }

    private void updatepass(String number, String passone) {
        //更新密码
        String uri = String.format(InternetURL.UPDATE_PASSWORD_URL + "?mobile=%s&password=%s", number, passone);
        StringRequest request = new StringRequest(Request.Method.GET,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Gson gson = new Gson();
                        try {
                            SuccessDATA data = gson.fromJson(s, SuccessDATA.class);
                            if (data.getCode() == 200){
                                //成功
                                Toast.makeText(SettingPassword.this, "密码重置成功，请重新登录", Toast.LENGTH_SHORT).show();
                                ShellContext.clear();
                                Intent main = new Intent(SettingPassword.this, LoginActivity.class);
                                startActivity(main);
                            }else{
                                Toast.makeText(SettingPassword.this, "修改密码失败！", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            ErrorDATA errorDATA = gson.fromJson(s, ErrorDATA.class);
                            if (errorDATA.getMsg().equals("failed")){
                                Toast.makeText(SettingPassword.this, "网络错误", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        mRequestQueue.add(request);
    }

}
