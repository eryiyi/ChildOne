package com.xiaogang.ChildOne.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.xiaogang.ChildOne.R;
import com.xiaogang.ChildOne.adapter.NoticeAdapter;
import com.xiaogang.ChildOne.adapter.OnClickContentItemListener;
import com.xiaogang.ChildOne.adapter.YuyingAdapter;
import com.xiaogang.ChildOne.data.ErrorDATA;
import com.xiaogang.ChildOne.data.NoticeDATA;
import com.xiaogang.ChildOne.data.YuyingDATA;
import com.xiaogang.ChildOne.entity.Account;
import com.xiaogang.ChildOne.entity.Notice;
import com.xiaogang.ChildOne.entity.Yuying;
import com.xiaogang.ChildOne.util.InternetURL;
import com.xiaogang.ChildOne.widget.ContentListView;

import java.util.ArrayList;
import java.util.List;

/**
 * author: ${zhanghailong}
 * Date: 2015/2/20
 * Time: 18:49
 * 类的功能、说明写在此处.
 */
public class NoticeActivity extends BaseActivity implements OnClickContentItemListener, View.OnClickListener,ContentListView.OnRefreshListener, ContentListView.OnLoadListener {
    private ImageView notice_back;//返回按钮
    private NoticeAdapter adapter;
    private ContentListView clv;
    private int pageIndex = 1;
    private static boolean IS_REFRESH = true;
    private List<Notice> list = new ArrayList<Notice>();
    private RequestQueue mRequestQueue;
    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice);
        initView();
        account = getGson().fromJson(sp.getString(Constants.ACCOUNT_KEY, ""), Account.class);
        setTheme(R.style.index_theme);
        adapter = new NoticeAdapter(list, this);
        mRequestQueue = Volley.newRequestQueue(this);
        clv.setAdapter(adapter);
        getData();
        adapter.setOnClickContentItemListener(this);
        clv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //将数据放到Intent中并进行页面跳转
                Notice yy = list.get(position);
                Intent detailYY =  new Intent(NoticeActivity.this, NoticeDetailActivity.class);
                detailYY.putExtra("yy", yy.getId());
                startActivity(detailYY);
            }
        });
    }

    private void initView() {
        notice_back = (ImageView) this.findViewById(R.id.notice_back);
        notice_back.setOnClickListener(this);
        clv = (ContentListView) this.findViewById(R.id.notice_lstv);
        clv.setOnRefreshListener(this);
        clv.setOnLoadListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case  R.id.notice_back:
                finish();
                break;
        }
    }

    private void getData(){
        String uri = String.format(InternetURL.GET_NATICE + "?school_id=%s&pageIndex=%d&pageSize=%d", account.getSchool_id(), pageIndex, 20);
        StringRequest request = new StringRequest(Request.Method.GET,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Gson gson = new Gson();
                        try {
                            NoticeDATA data = gson.fromJson(s, NoticeDATA.class);
                            if (IS_REFRESH){
                                list.clear();
                            }
                            list.addAll(data.getData());
                            if (data.getData().size() < 10){
                                clv.setResultSize(0);
                            }
                            clv.onRefreshComplete();
                            clv.onLoadComplete();
                            adapter.notifyDataSetChanged();

                        }catch (Exception e){
                            ErrorDATA errorDATA = gson.fromJson(s, ErrorDATA.class);
                            if (errorDATA.getMsg().equals("failed")){
                                Toast.makeText(mContext, "网络错误", Toast.LENGTH_SHORT).show();
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

    //下拉刷新
    @Override
    public void onRefresh() {
        IS_REFRESH = true;
        pageIndex = 1;
        getData();
    }

    //上拉加载
    @Override
    public void onLoad() {
        IS_REFRESH = false;
        pageIndex++;
        getData();
    }
    Notice yy = null;
    @Override
    public void onClickContentItem(int position, int flag, Object object) {
        switch (flag)
        {
            case 1:
                yy = list.get(position);
                Intent detailYY =  new Intent(this, NoticeDetailActivity.class);
                detailYY.putExtra("yy", yy.getId());
                startActivity(detailYY);
                break;
        }
    }
}
