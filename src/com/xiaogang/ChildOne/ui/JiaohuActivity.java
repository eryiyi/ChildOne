package com.xiaogang.ChildOne.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xiaogang.ChildOne.R;
import com.xiaogang.ChildOne.adapter.JiaohuAdapter;
import com.xiaogang.ChildOne.data.AccountMessageDATA;
import com.xiaogang.ChildOne.entity.Account;
import com.xiaogang.ChildOne.entity.AccountMessage;
import com.xiaogang.ChildOne.util.CommonUtil;
import com.xiaogang.ChildOne.util.InternetURL;

import java.util.ArrayList;
import java.util.List;

/**
 * author: ${zhanghailong}
 * Date: 2014/11/14
 * Time: 8:53
 * 类的功能、说明写在此处.
 */
public class JiaohuActivity extends BaseActivity implements View.OnClickListener {
    private ImageView jiaohuback;
    private ListView listView;
    private List<AccountMessage> list = new ArrayList<AccountMessage>();
    private JiaohuAdapter adapter;
    private TextView publishAll;//群发消息
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jiaohu);
        initView();
        adapter = new JiaohuAdapter(list, mContext);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent chat = new Intent(JiaohuActivity.this, ChatActivity.class);
                chat.putExtra(Constants.ACCOUNT_MESSAGE, list.get(position));
                startActivity(chat);
            }
        });
        getData();
    }

    private void initView() {
        jiaohuback = (ImageView) this.findViewById(R.id.jiaohuback);
        jiaohuback.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.jiaohu_lstv);

        publishAll = (TextView) this.findViewById(R.id.publish_all);
        publishAll.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.jiaohuback:
                finish();
                break;
            case R.id.publish_all:
                startActivity(new Intent(JiaohuActivity.this, SendGroupMessageActivity.class));
                break;
        }
    }

    private void getData(){
        Account account = getGson().fromJson(sp.getString(Constants.ACCOUNT_KEY, ""), Account.class);
        String identity = getGson().fromJson(sp.getString(Constants.IDENTITY, ""), String.class);
        String uri = String.format(InternetURL.JIAOHU_MESSAGE_LIST + "?uid=%s&user_type=%s", account.getUid(), identity);
        StringRequest request = new StringRequest(
                Request.Method.GET,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (CommonUtil.isJson(s)){
                            AccountMessageDATA data = getGson().fromJson(s, AccountMessageDATA.class);
                            list.addAll(data.getData());
                            adapter.notifyDataSetChanged();
                        }else {
                            Toast.makeText(mContext, "数据错误，请稍后重试", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(mContext, "服务器异常，请稍后重试", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        getRequestQueue().add(request);
    }
}
