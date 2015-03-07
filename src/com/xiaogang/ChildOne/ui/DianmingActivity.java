package com.xiaogang.ChildOne.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xiaogang.ChildOne.R;
import com.xiaogang.ChildOne.adapter.DianmingAdapter;
import com.xiaogang.ChildOne.adapter.OnClickContentItemListener;
import com.xiaogang.ChildOne.data.DianmingDATA;
import com.xiaogang.ChildOne.data.SuccessDATA;
import com.xiaogang.ChildOne.entity.Account;
import com.xiaogang.ChildOne.entity.Child;
import com.xiaogang.ChildOne.util.CommonUtil;
import com.xiaogang.ChildOne.util.InternetURL;

import java.util.ArrayList;
import java.util.List;

/**
 * author: ${zhanghailong}
 * Date: 2014/11/14
 * Time: 23:01
 * 类的功能、说明写在此处.
 */
public class DianmingActivity extends BaseActivity implements View.OnClickListener , OnClickContentItemListener {
    private ImageView dianmingback;
    private List<Child> list = new ArrayList<Child>();
    private DianmingAdapter adapter;
    private ListView listView;
    private TextView className;//班级名称
    private String classID;
    private String type = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dianming);

        type = getIntent().getStringExtra("type");

        initView();

        adapter = new DianmingAdapter(list, mContext, type);
        listView.setAdapter(adapter);
        adapter.setOnClickContentItemListener(this);
        getData();
    }

    private void initView() {
        dianmingback = (ImageView) this.findViewById(R.id.dianmingback);
        dianmingback.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.dianming_lstv);
        className = (TextView) findViewById(R.id.dianming_classname);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.dianmingback:
                finish();
                break;
        }
    }

    private void getData(){
        Account account = getGson().fromJson(sp.getString(Constants.ACCOUNT_KEY, ""), Account.class);
        if (account != null) {
            String uri = String.format(InternetURL.DIANMING_URL + "?uid=%s&class_id=%s&type=%s", account.getUid(), account.getClass_id(), type);
            StringRequest request = new StringRequest(
                    Request.Method.GET,
                    uri,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            if (CommonUtil.isJson(s)){
                                DianmingDATA data = getGson().fromJson(s, DianmingDATA.class);
                                className.setText(data.getData().getSclass().getName());
                                classID = data.getData().getSclass().getId();
                                list.addAll(data.getData().getChild());
                                adapter.notifyDataSetChanged();
                            }else {
                                Toast.makeText(mContext, "数据错误，请稍后重试", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    }
            );
            getRequestQueue().add(request);
        }
    }

    @Override
    public void onClickContentItem(int position, int flag, Object object) {
        switch (flag){
            case 1:
                //入园签到
                if ("0".equals(object)){
                    comeOrOut(list.get(position).getChild_id(), position, "0");
                }else {//离园签退
                    comeOrOut(list.get(position).getChild_id(), position, "1");
                }
                break;
        }
    }

    // 签到或是签退
    public void comeOrOut(String childId, final int position, final String comeType){
        Account account = getGson().fromJson(sp.getString(Constants.ACCOUNT_KEY, ""), Account.class);
        if (account != null) {
            String uri = String.format(InternetURL.DIANMING_ACTION + "?uid=%s&class_id=%s&type=%s&child_id=%s", account.getUid(), classID, type, childId);
            StringRequest request = new StringRequest(
                    Request.Method.GET,
                    uri,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            if (CommonUtil.isJson(s)){
                                SuccessDATA data = getGson().fromJson(s, SuccessDATA.class);
                                if (data.getCode() == 200) {
                                    if ("0".equals(comeType)) {//签到
                                        list.get(position).setIs_come(true);
                                    }else {//签退
                                        list.get(position).setIs_come(false);
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            }else {
                                Toast.makeText(mContext, "数据错误，请稍后重试", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    }
            );
            getRequestQueue().add(request);
        }
    }
}
