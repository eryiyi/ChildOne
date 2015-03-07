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
import com.xiaogang.ChildOne.adapter.OnClickContentItemListener;
import com.xiaogang.ChildOne.adapter.PhotoAdapter;
import com.xiaogang.ChildOne.data.ErrorDATA;
import com.xiaogang.ChildOne.data.PhotosDATA;
import com.xiaogang.ChildOne.entity.Account;
import com.xiaogang.ChildOne.entity.Photos;
import com.xiaogang.ChildOne.util.InternetURL;
import com.xiaogang.ChildOne.util.ShellContext;
import com.xiaogang.ChildOne.widget.ContentListView;

import java.util.ArrayList;
import java.util.List;

/**
 * author: ${zhanghailong}
 * Date: 2014/11/13
 * Time: 21:24
 * 类的功能、说明写在此处.
 */
public class PhotosActivity extends BaseActivity implements OnClickContentItemListener, View.OnClickListener,ContentListView.OnRefreshListener, ContentListView.OnLoadListener{
    private ContentListView clv;
    private PhotoAdapter adapter;
    private int pageIndex = 1;
    private List<Photos> list = new ArrayList<Photos>();
    Account account = (Account) ShellContext.getAttribute(ShellContext.ACCOUNT);
    private ImageView photosback;//返回按钮
    private String uid = "";
    private static boolean IS_REFRESH = true;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photos);
        initView();
        setTheme(R.style.index_theme);
        mRequestQueue = Volley.newRequestQueue(this);
        adapter = new PhotoAdapter(list, this);
        clv.setAdapter(adapter);
        adapter.setOnClickContentItemListener(this);
        clv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Photos photo = list.get(position == 0 ? 0 : position - 1);
                Intent pic = new Intent(PhotosActivity.this, PictureActivity.class);
                pic.putExtra("photo", photo);
                startActivity(pic);
            }
        });
        uid = getIntent().getExtras().getString("uid");//会员ID
        getData();
    }

    private void initView() {
        clv = (ContentListView) this.findViewById(R.id.lstv);
        photosback = (ImageView) this.findViewById(R.id.photosback);
        photosback.setOnClickListener(this);
    }

    private void getData(){
        String uri = String.format(InternetURL.GET_PHOTOS_URL + "?uid=%s", uid);
        StringRequest request = new StringRequest(Request.Method.GET,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Gson gson = new Gson();
                        try {
                            PhotosDATA data = gson.fromJson(s, PhotosDATA.class);
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
    private int pos = 0;
    private Photos photo;
    @Override
    public void onClickContentItem(int position, int flag, Object object) {
        pos = position;
        photo = list.get(position);
        switch (flag)
        {
            case 1:
                Intent pic = new Intent(this, PictureActivity.class);
                pic.putExtra("photo", photo);
                startActivity(pic);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.photosback:
                finish();
                break;
        }
    }
}
