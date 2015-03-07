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
 * Time: 22:53
 * 类的功能、说明写在此处.
 */
public class ForgetPassTwoActivity extends BaseActivity implements View.OnClickListener {
    private String number;
    private ImageView back;//返回
    private EditText yzm ;//验证码
    private TextView set;//设置按钮
    private String yzmnumber;
    private RequestQueue mRequestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findpassone);
        number = getIntent().getExtras().getString("number");
        initView();
    }

    private void initView() {
        back = (ImageView) this.findViewById(R.id.back);
        back.setOnClickListener(this);
        set = (TextView) this.findViewById( R.id.set);
        set.setOnClickListener(this);
        yzm = (EditText) this.findViewById(R.id.yzm);
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
                yzmnumber =  yzm.getText().toString();
                if(StringUtil.isNullOrEmpty(yzmnumber)){
                    Toast.makeText(this, "请输入验证码！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(yzmnumber.length() != 4){
                    Toast.makeText(this, "验证码格式不正确！", Toast.LENGTH_SHORT).show();
                    return;
                }
                getdata(number ,yzmnumber);
                break;

        }
    }

    private void getdata(final String number, String yzmnumber) {
        String uri = String.format(InternetURL.GET_YZM_ISTURE_URL + "?mobile=%s&code=%s", number, yzmnumber);
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
                                Intent success = new Intent(ForgetPassTwoActivity.this, ForgetPassThreeActivity.class);
                                success.putExtra("number", number);
                                startActivity(success);
                            }else{
                                Toast.makeText(ForgetPassTwoActivity.this, "验证码不正确！", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            ErrorDATA errorDATA = gson.fromJson(s, ErrorDATA.class);
                            if (errorDATA.getMsg().equals("failed")){
                                Toast.makeText(ForgetPassTwoActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
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
