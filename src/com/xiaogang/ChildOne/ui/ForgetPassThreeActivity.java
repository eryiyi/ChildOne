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
import com.xiaogang.ChildOne.util.StringUtil;

/**
 * author: ${zhanghailong}
 * Date: 2014/11/18
 * Time: 23:13
 * 类的功能、说明写在此处.
 */
public class ForgetPassThreeActivity extends BaseActivity implements View.OnClickListener {
    private String number;
    private ImageView back;
    private EditText password;
    private EditText surepass;
    private TextView set;

    private String passone;
    private String passtwo;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findpassthree);
        number = getIntent().getExtras().getString("number");
        initView();

    }

    private void initView() {
        back = (ImageView) this.findViewById(R.id.back);
        back.setOnClickListener(this);
        set = (TextView) this.findViewById(R.id.set);
        set.setOnClickListener(this);
        password = (EditText) this.findViewById(R.id.password);
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
                 //修改密码
                 passone = password.getText().toString();
                 passtwo = surepass.getText().toString();

                 if(StringUtil.isNullOrEmpty(passone) || StringUtil.isNullOrEmpty(passtwo)){
                     Toast.makeText(ForgetPassThreeActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
                     return;
                 }
                 if(passone.length() < 6 || passone.length() > 18){
                     Toast.makeText(ForgetPassThreeActivity.this, "请输入正确的密码，6到18位之间", Toast.LENGTH_SHORT).show();
                     return;
                 }
                 if(!passone.equals(passtwo)){
                     Toast.makeText(ForgetPassThreeActivity.this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
                     return;
                 }
                 updatepass(number ,passone);
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
                                Intent success = new Intent(ForgetPassThreeActivity.this, ForgetPassFourActivity.class);
                                startActivity(success);
                            }else{
                                Toast.makeText(ForgetPassThreeActivity.this, "修改密码失败！", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            ErrorDATA errorDATA = gson.fromJson(s, ErrorDATA.class);
                            if (errorDATA.getMsg().equals("failed")){
                                Toast.makeText(ForgetPassThreeActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
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
