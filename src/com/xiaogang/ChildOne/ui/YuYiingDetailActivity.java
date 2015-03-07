package com.xiaogang.ChildOne.ui;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
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
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaogang.ChildOne.HomeBabyApplication;
import com.xiaogang.ChildOne.R;
import com.xiaogang.ChildOne.adapter.AnimateFirstDisplayListener;
import com.xiaogang.ChildOne.data.ErrorDATA;
import com.xiaogang.ChildOne.data.YuyingDetailDATA;
import com.xiaogang.ChildOne.entity.Yuying;
import com.xiaogang.ChildOne.util.InternetURL;
import com.xiaogang.ChildOne.util.MxgsaTagHandler;

/**
 * author: ${zhanghailong}
 * Date: 2014/11/18
 * Time: 14:25
 * 类的功能、说明写在此处.
 */
public class YuYiingDetailActivity extends BaseActivity implements View.OnClickListener{
    private ImageView yyback;
    private TextView cont;
    private TextView title;
    String id;
    private RequestQueue mRequestQueue;
    private ImageView yypic;
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yuyingdetail);
        id =(String) getIntent().getExtras().get("yy");
        mRequestQueue = Volley.newRequestQueue(this);
        initView();
        getData();
//        title.setText(yy.getTitle());
//        cont.setText(Html.fromHtml(yy.getContent(), null, new MxgsaTagHandler(this)));
//        cont.setClickable(true);
//        cont.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void initView() {
        yyback = (ImageView) this.findViewById(R.id.yydetailback);
        yyback.setOnClickListener(this);
        cont = (TextView) this.findViewById(R.id.content);
        title = (TextView) this.findViewById(R.id.title);
        yypic = (ImageView) this.findViewById(R.id.yypic);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.yydetailback:
                finish();
                break;
        }
    }

    private void getData(){
        String uri = String.format(InternetURL.GET_YUYING_DETAIL + "?news_id=%s", id);
        StringRequest request = new StringRequest(Request.Method.GET,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Gson gson = new Gson();
                        try {
                            YuyingDetailDATA data = gson.fromJson(s, YuyingDetailDATA.class);
                            Yuying yy = data.getData();
                            title.setText(yy.getTitle());
                            if( yy.getPic() == null || yy.getPic().equals("") ){
                                yypic.setVisibility(View.GONE);
                            }else{
                                yypic.setVisibility(View.VISIBLE);
                                imageLoader.displayImage(yy.getPic(), yypic, HomeBabyApplication.txOptions, animateFirstListener);
                            }
                            cont.setText(Html.fromHtml(yy.getContent(), null, new MxgsaTagHandler(YuYiingDetailActivity.this)));
                            cont.setClickable(true);
                            cont.setMovementMethod(LinkMovementMethod.getInstance());
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
}
