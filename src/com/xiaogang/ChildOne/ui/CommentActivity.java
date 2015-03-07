package com.xiaogang.ChildOne.ui;

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
import com.xiaogang.ChildOne.entity.Growing;
import com.xiaogang.ChildOne.util.InternetURL;
import com.xiaogang.ChildOne.util.StringUtil;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuzwei on 2014/11/19.
 */
public class CommentActivity extends BaseActivity implements View.OnClickListener {
    private Growing growing;
    private ImageView back;
    private TextView publish;
    private EditText content;

    private Account account;
    private String identity;
    private String commentContent;
    private RequestQueue mRequestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_layout);
        growing = (Growing) getIntent().getSerializableExtra(Constants.GROWING_KEY);
        account =getGson().fromJson(sp.getString(Constants.ACCOUNT_KEY, ""), Account.class) ;
        identity = getGson().fromJson(sp.getString(Constants.IDENTITY, ""), String.class);
        initView();
        mRequestQueue = Volley.newRequestQueue(this);
    }

    private void initView(){
        back = (ImageView) findViewById(R.id.comment_back);
        publish = (TextView) findViewById(R.id.comment_fabu);
        content = (EditText) findViewById(R.id.comment_content);

        back.setOnClickListener(this);
        publish.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.comment_back:
                finish();
                break;
            case R.id.comment_fabu:
                commentContent = content.getText().toString();
                if (StringUtil.isNullOrEmpty(commentContent)){
                    Toast.makeText(mContext, "评论内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                StringRequest request = new StringRequest(
                        Request.Method.POST,
                        InternetURL.GROWING_COMMENT_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                ErrorDATA data = getGson().fromJson(s, ErrorDATA.class);
                                if (data.getCode() == 200){
                                    Toast.makeText(mContext, "发布成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                }else {
                                    Toast.makeText(mContext, "发布失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {

                            }
                        }
                ){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("growing_id", growing.getId() );
                        params.put("uid", account.getUid());
                        params.put("user_type", identity);
                        params.put("content", content.getText().toString());

                        return params;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("Content-Type","application/x-www-form-urlencoded");
                        return params;
                    }
                };
                mRequestQueue.add(request);
                break;
        }

    }
}
