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
import com.xiaogang.ChildOne.entity.Account;
import com.xiaogang.ChildOne.util.InternetURL;
import com.xiaogang.ChildOne.util.StringUtil;

/**
 * author: ${zhanghailong}
 * Date: 2014/11/18
 * Time: 15:58
 * 类的功能、说明写在此处.
 */
public class SettingEmailActivity extends BaseActivity implements View.OnClickListener{
    private ImageView back;
    private EditText email;
    private String emailText;
    private TextView set;
    Account account ;
    private RequestQueue mRequestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setemail);
        initView();
        account = getGson().fromJson(sp.getString(Constants.ACCOUNT_KEY, ""), Account.class);
        if(account != null){
            if(account.getEmail()!=null && !"".equals(account.getEmail()) && !" ".equals(account.getEmail()) ){
                email.setText(account.getEmail());
            }
        }
        mRequestQueue = Volley.newRequestQueue(this);
    }

    private void initView() {
        back = (ImageView) this.findViewById(R.id.back);
        back.setOnClickListener(this);
        email = (EditText) this.findViewById(R.id.email);
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
                //设置邮箱事件
                emailText = email.getText().toString();//邮箱
                if(StringUtil.isNullOrEmpty(emailText)){
                    Toast.makeText(this, "请输入邮箱！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(StringUtil.isEmail(emailText)){
                    Toast.makeText(this, "邮箱格式不正确！", Toast.LENGTH_SHORT).show();
                    return;
                }
                getYzm(emailText);
                break;
        }
    }
    private void getYzm(final String mobileNum) {
        String uri = String.format(InternetURL.GET_YZM_URL + "?mobile=%s&type=%d", mobileNum, 1);
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
                                Intent success = new Intent(SettingEmailActivity.this, SettingEmaillTwoActivity.class);
                                success.putExtra("number", emailText);
                                startActivity(success);
                            }else{
                                Toast.makeText(SettingEmailActivity.this, "获取验证码失败！", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            ErrorDATA errorDATA = gson.fromJson(s, ErrorDATA.class);
                            if (errorDATA.getMsg().equals("failed")){
                                Toast.makeText(SettingEmailActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
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
