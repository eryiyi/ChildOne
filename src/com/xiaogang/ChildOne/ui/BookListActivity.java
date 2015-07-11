package com.xiaogang.ChildOne.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import com.xiaogang.ChildOne.adapter.BookAdapter;
import com.xiaogang.ChildOne.adapter.NoticeAdapter;
import com.xiaogang.ChildOne.adapter.OnClickContentItemListener;
import com.xiaogang.ChildOne.adapter.YuyingAdapter;
import com.xiaogang.ChildOne.data.BooksData;
import com.xiaogang.ChildOne.data.ErrorDATA;
import com.xiaogang.ChildOne.data.NoticeDATA;
import com.xiaogang.ChildOne.data.YuyingDATA;
import com.xiaogang.ChildOne.entity.Account;
import com.xiaogang.ChildOne.entity.Books;
import com.xiaogang.ChildOne.entity.Notice;
import com.xiaogang.ChildOne.entity.Yuying;
import com.xiaogang.ChildOne.util.InternetURL;
import com.xiaogang.ChildOne.widget.ContentListView;

import java.util.ArrayList;
import java.util.List;

/**
 * author: ${Yanglq}
 * Date: 2015/04/02
 * Time: 9:00
 * 类的功能、说明写在此处.
 */
public class BookListActivity extends BaseActivity implements OnClickContentItemListener, View.OnClickListener,ContentListView.OnRefreshListener, ContentListView.OnLoadListener {
    private ImageView notice_back;//返回按钮
    private BookAdapter adapter;
    private ContentListView clv;
    private int pageIndex = 1;
    private static boolean IS_REFRESH = true;
    private List<Books> list = new ArrayList<Books>();
    private RequestQueue mRequestQueue;
    private Account account;
    private TextView title;
    private ImageView book_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice);
        initView();
        account = getGson().fromJson(sp.getString(Constants.ACCOUNT_KEY, ""), Account.class);
        setTheme(R.style.index_theme);
        adapter = new BookAdapter(list, this);
        mRequestQueue = Volley.newRequestQueue(this);
        clv.setAdapter(adapter);
        getData();
        adapter.setOnClickContentItemListener(this);
        clv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //将数据放到Intent中并进行页面跳转
                Books yy = list.get(position);
                Intent detailYY =  new Intent(BookListActivity.this, BookDailActivity.class);
                detailYY.putExtra("book", yy);
                startActivity(detailYY);
            }
        });
    }

    private void initView() {
        notice_back = (ImageView) this.findViewById(R.id.notice_back);
        title = (TextView) this.findViewById(R.id.title);
        book_add = (ImageView) this.findViewById(R.id.book_add);
        book_add.setVisibility(View.VISIBLE);
        book_add.setOnClickListener(this);
        title.setText("图书借阅列表");
        notice_back.setOnClickListener(this);
        clv = (ContentListView) this.findViewById(R.id.notice_lstv);
        clv.setOnRefreshListener(this);
        clv.setOnLoadListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.notice_back:
                finish();
                break;
            case R.id.book_add:
            	Intent intent = new Intent(BookListActivity.this,BookBorrowActivity.class);
            	startActivity(intent);
            	break;
        }
    }

    private void getData(){
        String uri = String.format(InternetURL.BOOK_LIST_URL + "?uid=%s&pageIndex=%d&pageSize=%d", account.getUid(), pageIndex, 20);
        StringRequest request = new StringRequest(Request.Method.GET,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                    	
                    //	{"code":"200","msg":"sucess","data":[//{"borrow_id":"6","book_id":"1","book_pic":"","book_name":"\u94a2\u94c1\u662f\u600e\u4e48\u7ec3\u6210\u7684","book_num":"1","dateline":"1427903553","uid":"73","school_id":"1","user_name":"\u5927\u5b9d\u5b9d","borrow_time":"2015-04-01 23:52:33","receive_user_name":" ","receive_uid":" ","receive_datetime":" ","borrow_price":" ","days":" "},
                    	                                     //{"borrow_id":"8","book_id":"1","book_pic":"","book_name":"\u94a2\u94c1\u662f\u600e\u4e48\u7ec3\u6210\u7684","book_num":"1","dateline":"1427956548","uid":"73","school_id":"1","user_name":"","borrow_time":"2015-04-02 14:35:48","receive_user_name":" ","receive_uid":" ","receive_datetime":" ","borrow_price":" ","days":" "},
                    	
                    	                                     //{"borrow_id":"9","book_id":"1","book_pic":"","book_name":"\u94a2\u94c1\u662f\u600e\u4e48\u7ec3\u6210\u7684","book_num":"1","dateline":"1427956697","uid":"73","school_id":"1","user_name":"","borrow_time":"2015-04-02 14:38:17","receive_user_name":" ","receive_uid":" ","receive_datetime":" ","borrow_price":" ","days":" "}]}
                        Gson gson = new Gson();
                        try {
                            BooksData data = gson.fromJson(s, BooksData.class);
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
    Books yy = null;
    @Override
    public void onClickContentItem(int position, int flag, Object object) {
        switch (flag)
        {
            case 1:
                yy = list.get(position);
                Intent detailYY =  new Intent(BookListActivity.this, BookDailActivity.class);
                detailYY.putExtra("book", yy);
                startActivity(detailYY);
                break;
        }
    }
}
