package com.xiaogang.ChildOne.ui;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaogang.ChildOne.HomeBabyApplication;
import com.xiaogang.ChildOne.R;
import com.xiaogang.ChildOne.adapter.AnimateFirstDisplayListener;
import com.xiaogang.ChildOne.adapter.MyPagerAdapter;
import com.xiaogang.ChildOne.adapter.OnClickContentItemListener;
import com.xiaogang.ChildOne.data.ErrorDATA;
import com.xiaogang.ChildOne.data.JiazhangDATA;
import com.xiaogang.ChildOne.entity.Account;
import com.xiaogang.ChildOne.entity.Jiazhang;
import com.xiaogang.ChildOne.util.ImageService;
import com.xiaogang.ChildOne.util.InternetURL;
import com.xiaogang.ChildOne.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class CenterActivity extends BaseActivity implements
        View.OnClickListener,OnClickContentItemListener {
    //第一页
    private ImageView chengzhangjilu;//成长管理
    private ImageView hudongtiandi;//互动天地
//    private ImageView banjixiangce;//班级相册
//    private ImageView cartongzhi;//校车通知
//    private ImageView tongxunlu;//通讯录
    private ImageView yuyingxunxi;//育英讯息
    private ImageView notice;
    private ImageView book;
    private ImageView about;


    //第二页
    private ImageView menu_school;//学校公告
    private ImageView menu_class;//班级公告
    private ImageView menu_zuoye;//作业
    private ImageView menu_shipu;//食谱
    private ImageView menu_jiaqi;//假期
    private ImageView menu_guanhuai;//关怀
    //第三页
//    private ImageView menu_yue;//玉儿小区
//    private ImageView menu_video;//视频监控


    private ImageView set;//设置按钮
    private ImageView menuadd;//添加按钮

    private String uid;

    private String child_id;
    private Account account;
    private String identity;
    private ImageView message;//消息
    private ImageView tx;//头像

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

    private TextView titlename;//昵称

    //View集合
    private ArrayList<LinearLayout> viewPagerViews = new ArrayList<LinearLayout>();
    //滑动View控件
    private ViewPager viewPager;

    //2个界面
    private LinearLayout One;
    private LinearLayout Two;
//    private LinearLayout Three;
    //对应四个界面
    private final int BLACK = 0;
    private final int BLUE = 1;

    //当前停留的位置
    private int currentId = BLACK;
    //当前屏幕宽度
    private int ActivityWidth;

    private LinearLayout mWallpager;
    //家长集合
    private List<Jiazhang> list = new ArrayList<Jiazhang>();
    private RequestQueue mRequestQueue;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        mRequestQueue = Volley.newRequestQueue(this);
        String accountStr = sp.getString("account", "");
        if (!StringUtil.isNullOrEmpty(accountStr)){
            try{
                account =getGson().fromJson(accountStr, Account.class);
                if (account != null){
                    uid = account.getUid();
                }
            }catch (Exception e){
                Log.i("Account Gson Exception", "Account转换异常");
            }
        }
        identity = getGson().fromJson(sp.getString(Constants.IDENTITY, ""), String.class);
        String str = account.getM_cover();
        String nicheng = account.getM_name();
        if(identity.equals("0")){
            str = account.getF_cover();
            nicheng = account.getF_name();
        }
        if(identity.equals("2")){
            str = account.getCover();
            nicheng = account.getNick_name();
        }
//        imageLoader.displayImage(str, tx, HomeBabyApplication.txOptions, animateFirstListener);
        titlename.setText(nicheng);
        getData();
    }

    private void initView() {
        //viewpager
        viewPager = (ViewPager)this.findViewById(R.id.vPager);
        One = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.onelayout, null);
        viewPagerViews.add(One);
        Two= (LinearLayout) LayoutInflater.from(this).inflate(R.layout.twolayout, null);
        viewPagerViews.add(Two);
//        Three = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.threelayout,null);
//        viewPagerViews.add(Three);
        MyPagerAdapter adapter = new MyPagerAdapter(viewPagerViews);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(BLACK);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageSelected(int position) {
//                slideView(position);
//                currentId = position;
            }
            public void onPageScrolled(int arg0, float arg1, int arg2) {}
            public void onPageScrollStateChanged(int arg0) {}
        });

        chengzhangjilu = (ImageView) One.findViewById(R.id.chengzhangjilu);
        hudongtiandi = (ImageView) One.findViewById(R.id.hudongtiandi);
        book = (ImageView) One.findViewById(R.id.book);
        about = (ImageView) One.findViewById(R.id.about);
        notice = (ImageView) One.findViewById(R.id.notice);
        yuyingxunxi = (ImageView) One.findViewById(R.id.yuyingxunxi);
        menu_school = (ImageView) Two.findViewById(R.id.menu_school);
        menu_class = (ImageView) Two.findViewById(R.id.menu_class);
        menu_zuoye = (ImageView) Two.findViewById(R.id.menu_zuoye);
        menu_shipu = (ImageView) Two.findViewById(R.id.menu_shipu);
        menu_jiaqi = (ImageView) Two.findViewById(R.id.menu_jiaqi);
        menu_guanhuai = (ImageView) Two.findViewById(R.id.menu_guanhuai);
//        menu_yue = (ImageView) Three.findViewById(R.id.menu_yue);
//        menu_video = (ImageView) Three.findViewById(R.id.menu_video);

//        menu_video.setOnClickListener(this);
//        menu_yue.setOnClickListener(this);
        menu_guanhuai.setOnClickListener(this);
        menu_jiaqi.setOnClickListener(this);
        menu_shipu.setOnClickListener(this);
        menu_zuoye.setOnClickListener(this);
        menu_school.setOnClickListener(this);
        menu_class.setOnClickListener(this);
        chengzhangjilu.setOnClickListener(this);
        hudongtiandi.setOnClickListener(this);
        notice.setOnClickListener(this);
        about.setOnClickListener(this);
        book.setOnClickListener(this);
        yuyingxunxi.setOnClickListener(this);

        set = (ImageView) this.findViewById(R.id.setmenu);
        set.setOnClickListener(this);


        message= (ImageView) this.findViewById(R.id.message);
        message.setOnClickListener(this);

        tx = (ImageView) this.findViewById(R.id.tx);
        titlename = (TextView) this.findViewById(R.id.titlename);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setmenu://设置
                Intent setting = new Intent(CenterActivity.this, SettingActivity.class);
                startActivity(setting);
                break;

            case R.id.chengzhangjilu://成长管理
                Intent main = new Intent(CenterActivity.this, MainActivity.class);
                startActivity(main);
                break;
            case R.id.hudongtiandi://互动天地
                Intent jiaohu = new Intent(CenterActivity.this, JiaohuActivity.class);
                startActivity(jiaohu);
                break;
            case R.id.banjixiangce://班级相册
                Intent photo = new Intent(CenterActivity.this, PhotosActivity.class);
                photo.putExtra("uid", uid);
                startActivity(photo);
                break;
            case R.id.cartongzhi://校车通知
                if(account.getIs_teacher().equals("1")){//是教师登陆的话
                    Intent schoolbus = new Intent(CenterActivity.this, OpenglDemo.class);
                    startActivity(schoolbus);
                }else{
                    //家长登陆的话
                    Intent schoolbus = new Intent(CenterActivity.this, SchoolBusActivityFather.class);
                    startActivity(schoolbus);
                }
                break;
            case R.id.tongxunlu://通讯录
                Intent txl = new Intent(CenterActivity.this, TongxunluActivity.class);
                startActivity(txl);
                break;
            case R.id.yuyingxunxi://育英信息
                Intent yuying = new Intent(CenterActivity.this, YuyingMessageActivity.class);
                startActivity(yuying);
                break;
            case R.id.message://交互信息
                Intent hudong = new Intent(CenterActivity.this, JiaohuActivity.class);
                startActivity(hudong);
                break;
            case R.id.tx://设置个人信息
                Intent mumSet = new Intent(CenterActivity.this, MumSettingActivity.class);
                startActivity(mumSet);
                break;
            case R.id.menu_school://学校公告
                break;
            case R.id.menu_class://班级公告
                break;
            case R.id.menu_zuoye://作业
                break;
            case R.id.menu_shipu://每周食谱
                break;
            case R.id.menu_jiaqi://请假
                break;
            case R.id.menu_guanhuai://健康关怀
                break;
            case R.id.menu_yue://玉儿小区
                break;
            case R.id.menu_video://视频监控
                break;
            case R.id.book:
                break;
            case R.id.about:
                Intent about = new Intent(CenterActivity.this, AboutActivity.class);
                startActivity(about);
                break;
            case R.id.notice://告示
                Intent notice = new Intent(CenterActivity.this, NoticeActivity.class);
                startActivity(notice);
                break;

        }

    }

    @Override
    public void onClickContentItem(int position, int flag, Object object) {

    }

    private void getData(){
        String uri = String.format(InternetURL.GET_RELATIONS + "?uid=%s", account.getUid());
        StringRequest request = new StringRequest(Request.Method.GET,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Gson gson = new Gson();
                        try {
                            JiazhangDATA data = gson.fromJson(s, JiazhangDATA.class);
                            list.addAll(data.getData());
//                            initData();
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

//    public void initData(){
//        //初始化家长列表
//        if(list!=null && list.size() > 0){
//            if(list.size()>0){
//                try {
//                    imageLoader.displayImage(list.get(0).getCover(), tx, HomeBabyApplication.txOptions, animateFirstListener);
//                }catch (Exception e){
//                    Log.d("没有网络图片", e.getMessage());
//                }
//                titlename.setText(list.get(0).getCall_name());
//            }
//
//        }
//    }
}
