package com.xiaogang.ChildOne.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xiaogang.ChildOne.R;
import com.xiaogang.ChildOne.data.ErrorDATA;
import com.xiaogang.ChildOne.entity.Account;
import com.xiaogang.ChildOne.util.CommonUtil;
import com.xiaogang.ChildOne.util.InternetURL;
import com.xiaogang.ChildOne.util.StringUtil;
import com.xiaogang.ChildOne.util.upload.MultiPartStack;


import java.util.HashMap;
import java.util.Map;

/**
 * author: ${zhanghailong}
 * Date: 2015/1/7
 * Time: 14:57
 * 类的功能、说明写在此处.
 */
public class AddJiarenActivity extends BaseActivity  implements View.OnClickListener{
    private ImageView backsetaddjz;//返回按钮

    private EditText name;
    private EditText relation;
    private EditText chenghu;
    private EditText mobile;
    private Account account;
    private TextView addset;//添加
    private static RequestQueue mSingleQueue;
    private ImageView telephone;//打电话
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setadd);
        mSingleQueue = Volley.newRequestQueue(this, new MultiPartStack());
        account = getGson().fromJson(sp.getString(Constants.ACCOUNT_KEY, ""), Account.class);
        initView();
    }

    private void initView() {
        backsetaddjz = (ImageView) this.findViewById(R.id.backsetaddjz);
        backsetaddjz.setOnClickListener(this);
        name= (EditText) this.findViewById(R.id.name);
        chenghu= (EditText) this.findViewById(R.id.chenghu);
        mobile= (EditText) this.findViewById(R.id.mobile);
        relation= (EditText) this.findViewById(R.id.relation);

        addset= (TextView) this.findViewById(R.id.addset);
        addset.setOnClickListener(this);
        telephone = (ImageView) this.findViewById(R.id.telephone);
        telephone.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.backsetaddjz://返回按钮
                finish();
                break;
            case R.id.addset:
                String namet = name.getText().toString();
                String chenghut =chenghu.getText().toString();
                String relationt = relation.getText().toString();
                String mobilet = mobile.getText().toString();
                if(StringUtil.isNullOrEmpty(relationt)){
                    Toast.makeText(mContext, "请输入名称", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(StringUtil.isNullOrEmpty(chenghut)){
                    Toast.makeText(mContext, "请输入称呼", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(StringUtil.isNullOrEmpty(mobilet)){
                    Toast.makeText(mContext, "请输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(StringUtil.isNullOrEmpty(namet)){
                    Toast.makeText(mContext, "请输入姓名", Toast.LENGTH_SHORT).show();
                    return;
                }
                setting(namet,chenghut,mobilet,relationt);
                break;
            case R.id.telephone://打电话
                String mobilet1 = mobile.getText().toString();
                if(StringUtil.isNullOrEmpty(mobilet1)){
                    Toast.makeText(mContext, "请输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobile.getText().toString()));
                AddJiarenActivity.this.startActivity(intent);
                break;
        }
    }

    private void setting(final String name, final String chenghu, final String mobile, final String relation){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.ADD_JIAZHANG,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (CommonUtil.isJson(s)) {
                            ErrorDATA errorDATA = getGson().fromJson(s, ErrorDATA.class);
                            if (errorDATA.getCode() == 200){
                                Toast.makeText(mContext, "设置成功", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(mContext, "设置失败，请稍后重试1", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(mContext, "设置失败，请稍后重试", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(mContext, "设置失败，请稍后重试", Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("uid", account.getUid());
                params.put("name", name);
                params.put("relation", relation);
                params.put("call_name",chenghu);
                params.put("mobile",mobile);
                params.put("relation_id","0");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        mSingleQueue.add(request);
    }
}
