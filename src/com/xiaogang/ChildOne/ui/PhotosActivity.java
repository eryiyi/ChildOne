package com.xiaogang.ChildOne.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.xiaogang.ChildOne.HomeBabyApplication;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

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
    private static boolean IS_REFRESH = true;
    private RequestQueue mRequestQueue;
    private TextView create_phono_tv;
    private Dialog dialog;
    private EditText et_phono_name;
    private Button cancle_bt,create_bt;

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
        getData();
    }

    private void initView() {
        clv = (ContentListView) this.findViewById(R.id.lstv);
        photosback = (ImageView) this.findViewById(R.id.photosback);
        create_phono_tv = (TextView) this.findViewById(R.id.create_phono_tv);
        if (HomeBabyApplication.is_student.equals("1")) {
        	create_phono_tv.setVisibility(View.GONE);
		}else if(HomeBabyApplication.is_student.equals("0")){
			create_phono_tv.setVisibility(View.VISIBLE);
		}
        photosback.setOnClickListener(this);
        create_phono_tv.setOnClickListener(this);
    }

    private void addXiangce(String name){
        String uri = String.format(InternetURL.ADD_XIANGCE + "?uid=%s&op=add&name=%s", HomeBabyApplication.uid,name);
        StringRequest request = new StringRequest(Request.Method.GET,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                    	
                        Gson gson = new Gson();
                        try {
                           //  PhotosDATA data = gson.fromJson(s, PhotosDATA.class);
                            JSONObject jsonObject = new JSONObject(s);
                        	String jsonStr = jsonObject.getString("data");
                        	Photos data = gson.fromJson(jsonStr, Photos.class);
                        	if (IS_REFRESH){
                                list.clear();
                            }
                            list.add(data);
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
    
    
    private void getData(){
        String uri = String.format(InternetURL.GET_PHOTOS_URL + "?uid=%s", HomeBabyApplication.uid);
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
            case R.id.create_phono_tv:
            	createPhonoName();
            	break;
            case R.id.cancle_bt:
            	dialog.dismiss();
            	break;
            case R.id.create_bt:
            	String nameStr = et_phono_name.getText().toString().trim();
            	try {
            		nameStr = URLEncoder.encode(nameStr, "UTF-8");
            	} catch (UnsupportedEncodingException e) {
            		// TODO Auto-generated catch block
            		e.printStackTrace();
            	}
            	if(nameStr.equals("")){
            		Toast.makeText(mContext, "请输入相册名称", Toast.LENGTH_SHORT).show();
            		return;
            	}
            	addXiangce(nameStr);
            	dialog.dismiss();
            	break;
        }
    }
    
    
    
	private void createPhonoName(){
		dialog = new Dialog(this, R.style.myDialog);
		dialog.setCancelable(true);
		View view = View.inflate(this, R.layout.create_phono_dialog,null);
		et_phono_name = (EditText) view.findViewById(R.id.et_phono_name);
		cancle_bt = (Button) view.findViewById(R.id.cancle_bt);
		create_bt = (Button) view.findViewById(R.id.create_bt);
		create_bt.setOnClickListener(this);
		cancle_bt.setOnClickListener(this);
		dialog.setContentView(view);
		dialog.show();
	}
}
