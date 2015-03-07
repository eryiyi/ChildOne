package com.xiaogang.ChildOne.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.xiaogang.ChildOne.ui.ForgetPassTwoActivity;
import com.xiaogang.ChildOne.util.HttpUtils;
import com.xiaogang.ChildOne.util.InternetURL;
import com.xiaogang.ChildOne.util.StringUtil;


/**
 * author: liuzwei
 * Date: 2014/8/19
 * Time: 15:39
 * 类的功能、说明写在此处.
 */
public class MobileDialog extends Dialog implements View.OnClickListener{
    private Context context;
    private EditText mobile;
    private Button sure;
    private Button cancle;
    private String mobileNum;
    private RequestQueue mRequestQueue;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){

            }
            super.handleMessage(msg);
        }
    };

    public MobileDialog(Context context,int them) {
        super(context,them);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile_dialog);
        initView();
    }

    private void initView(){
        mRequestQueue = Volley.newRequestQueue(context);
        mobile = (EditText) findViewById(R.id.mobile);
        sure = (Button) this.findViewById(R.id.sure);
        sure.setOnClickListener(this);
        cancle = (Button) this.findViewById(R.id.cancle);
        cancle.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        boolean isMobileNet, isWifiNet;
        try {
            isMobileNet = HttpUtils.isMobileDataEnable(context);
            isWifiNet = HttpUtils.isWifiDataEnable(context);
            if (!isMobileNet && !isWifiNet){
                Toast.makeText(context, "当前网络连接不可用", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
//            Log.d("网络连接不可用", e.getMessage());
        }
        switch (v.getId()){
            case R.id.sure:
                mobileNum = mobile.getText().toString();//手机号
                if(StringUtil.isNullOrEmpty(mobileNum)){
                    Toast.makeText(context, "请输入手机号！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mobileNum.length() != 11){
                    Toast.makeText(context, "手机号格式不正确！", Toast.LENGTH_SHORT).show();
                    return;
                }
                getYzm(mobileNum);
                break;
            case R.id.cancle:
                MobileDialog.this.dismiss();
                break;
        }
    }

    private void getYzm(final String mobileNum) {
        String uri = String.format(InternetURL.GET_YZM_URL + "?mobile=%s&type=%d", mobileNum, 0);
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
                                Intent success = new Intent(context, ForgetPassTwoActivity.class);
                                success.putExtra("number", mobileNum);
                                context.startActivity(success);
                            }else{
                                Toast.makeText(context, "获取验证码失败！", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            ErrorDATA errorDATA = gson.fromJson(s, ErrorDATA.class);
                            if (errorDATA.getMsg().equals("failed")){
                                Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show();
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