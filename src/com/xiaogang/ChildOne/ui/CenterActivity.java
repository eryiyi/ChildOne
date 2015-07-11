package com.xiaogang.ChildOne.ui;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.xiaogang.ChildOne.HomeBabyApplication;
import com.xiaogang.ChildOne.R;
import com.xiaogang.ChildOne.adapter.AnimateFirstDisplayListener;
import com.xiaogang.ChildOne.adapter.MyPagerAdapter;
import com.xiaogang.ChildOne.adapter.OnClickContentItemListener;
import com.xiaogang.ChildOne.data.ErrorDATA;
import com.xiaogang.ChildOne.data.JiazhangDATA;
import com.xiaogang.ChildOne.data.NoticeDATA;
import com.xiaogang.ChildOne.data.PicData;
import com.xiaogang.ChildOne.entity.Account;
import com.xiaogang.ChildOne.entity.Jiazhang;
import com.xiaogang.ChildOne.entity.PIC;
import com.xiaogang.ChildOne.util.ImageService;
import com.xiaogang.ChildOne.util.InternetURL;
import com.xiaogang.ChildOne.util.StringUtil;
import com.xiaogang.ChildOne.util.face.FaceConversionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import u.aly.de;

public class CenterActivity extends BaseActivity implements
        View.OnClickListener,OnClickContentItemListener {
	protected static final String TAG = "CenterActivity";
    //第一页
    private ImageView chengzhangjilu;//成长管理
    private ImageView hudongtiandi;//互动天地
    private ImageView yuyingxunxi;//育英讯息
    private ImageView notice;
    private ImageView book;
    private ImageView about;
    private ImageView menu_video;//视频监控
    private ImageView daily_diet; //每周食谱
    private ImageView baby_works; //宝宝作品展示
    private ImageView actionxiangce;// 活动剪影
    private ImageView set;//设置按钮
    private ImageView menuadd;//添加按钮
    private TextView activity_main_biaotu_tv;
    private String child_id;
    private Account account;
    private String identity;
    private ImageView message;//消息
    private ImageView tx;//头像
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    private ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private TextView titlename;//昵称
    //View集合
    private ArrayList<LinearLayout> viewPagerViews = new ArrayList<LinearLayout>();
    //滑动View控件
    private ViewPager viewPager;
    //2个界面
    private LinearLayout One;
 //   private LinearLayout Two;
    private LinearLayout Three;
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
    private List<PIC> picList = new ArrayList<PIC>();
    private RequestQueue mRequestQueue;
    private MyPagerAdpter pagerAdapter;
    private ViewPager picPager;
    private ScheduledExecutorService scheduledExecutorService;
	// private int oldPosition = 0;//记录上一次点的位置
	 private int currentItem; //当前页面
    
    private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case 0x01:
				pagerAdapter = new MyPagerAdpter(picList);
				picPager.setAdapter(pagerAdapter);
				break;
			}
		}
    	
    };

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
                    HomeBabyApplication.uid = account.getUid();
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
        titlename.setText(nicheng);
        getData();
        getPicData();
      //初始化加载表情
        new Thread(new Runnable() {
            @Override
            public void run() {
                FaceConversionUtil.getInstace().getFileText(getApplication());
            }
        }).start();

    }

    private void initView() {
        //viewpager
        viewPager = (ViewPager)this.findViewById(R.id.vPager);
        picPager = (ViewPager) this.findViewById(R.id.viewpager);
        activity_main_biaotu_tv = (TextView) this.findViewById(R.id.activity_main_biaotu_tv);
        One = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.onelayout, null);
        viewPagerViews.add(One);
        Three = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.threelayout,null);
        viewPagerViews.add(Three);
        MyPagerAdapter adapter = new MyPagerAdapter(viewPagerViews);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(BLACK);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageSelected(int position) {
//                slideView(position);
//               currentId = position;
            	
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
        menu_video = (ImageView) Three.findViewById(R.id.menu_video);
       
        
        menu_video.setOnClickListener(this);
        chengzhangjilu.setOnClickListener(this);
        hudongtiandi.setOnClickListener(this);
        notice.setOnClickListener(this);
        about.setOnClickListener(this);
        book.setOnClickListener(this);
        yuyingxunxi.setOnClickListener(this);
        
        baby_works = (ImageView) Three.findViewById(R.id.baby_works);
        baby_works.setOnClickListener(this);
        daily_diet = (ImageView) Three.findViewById(R.id.daily_diet);
        daily_diet.setOnClickListener(this);
        
        actionxiangce = (ImageView) Three.findViewById(R.id.actionxiangce);
        actionxiangce.setOnClickListener(this);
        
        set = (ImageView) this.findViewById(R.id.setmenu);
        set.setOnClickListener(this);


        message= (ImageView) this.findViewById(R.id.message);
        message.setOnClickListener(this);

        tx = (ImageView) this.findViewById(R.id.tx);
        titlename = (TextView) this.findViewById(R.id.titlename);
    }
    
    
    private void getPicData(){
        String uri = String.format(InternetURL.LUNFAN_URL + "?school_id=%s", account.getSchool_id());
        StringRequest request = new StringRequest(Request.Method.GET,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Gson gson = new Gson();
                        try {
                            PicData data = gson.fromJson(s, PicData.class);
                            picList.addAll(data.getData());
                            handler.sendEmptyMessage(0x01);
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
            case R.id.actionxiangce://班级相册
                Intent photo = new Intent(CenterActivity.this, PhotosActivity.class);
                startActivity(photo);
                break;
           /* case R.id.cartongzhi://校车通知
                if(account.getIs_teacher().equals("1")){//是教师登陆的话
                    Intent schoolbus = new Intent(CenterActivity.this, OpenglDemo.class);
                    startActivity(schoolbus);
                }else{
                    //家长登陆的话
                    Intent schoolbus = new Intent(CenterActivity.this, SchoolBusActivityFather.class);
                    startActivity(schoolbus);
                }
                break;*/
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
            case R.id.menu_video://视频监控
            	Intent intent = new Intent(CenterActivity.this,PingTaiLoginActivity.class);
            	startActivity(intent);
                break;
            case R.id.book: //图书借阅
            	Intent bookIntent = new Intent(CenterActivity.this,BookListActivity.class);
            	startActivity(bookIntent);
                break;
            case R.id.about:
                Intent about = new Intent(CenterActivity.this, AboutActivity.class);
                startActivity(about);
                break;
            case R.id.notice://告示
                Intent notice = new Intent(CenterActivity.this, NoticeActivity.class);
                startActivity(notice);
                break;
            case R.id.baby_works://宝宝作品
            	Intent baby_workIntent = new Intent(CenterActivity.this,BabyWorkActivity.class);
            	startActivity(baby_workIntent);
            	break;
            case R.id.daily_diet://每周食谱
            	Intent shipu_Intent = new Intent(CenterActivity.this,ShiPuActivity.class);
            	startActivity(shipu_Intent);
            	break;
            default:
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
    
    
    private class MyPagerAdpter extends PagerAdapter{
		
		private List<PIC> images;
		private LayoutInflater inflater;
		public MyPagerAdpter(List<PIC> images) {
			this.images = images;
			inflater = getLayoutInflater();
		}
		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		}
		@Override
		public void finishUpdate(ViewGroup container) {
			
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			View imageLayout = inflater.inflate(R.layout.image_item, container, false);
			ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image_iv);
			imageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(CenterActivity.this,NewXunXiActivity.class);
					Log.i(TAG, position+"--");
					 intent.putExtra("url",picList.get(position).getUrl());
					 startActivity(intent);
					
				}
			});
			activity_main_biaotu_tv.setText(picList.get(position).getTitle());
			imageLoader.displayImage(images.get(position).getPic(), imageView, HomeBabyApplication.options, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingStarted(String imageUri, View view) {
				}

				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
					String message = null;
					switch (failReason.getType()) {		// 获取图片失败类型
						case IO_ERROR:				// 文件I/O错误
							message = "Input/Output error";
							break;
						case DECODING_ERROR:		// 解码错误
							message = "Image can't be decoded";
							break;
						case NETWORK_DENIED:		// 网络延迟
							message = "Downloads are denied";
							break;
						case OUT_OF_MEMORY:		    // 内存不足
							message = "Out Of Memory error";
							break;
						case UNKNOWN:				// 原因不明
							message = "Unknown error";
							break;
					}
				//	Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();

				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				}
			});

			((ViewPager) container).addView(imageLayout, 0);		// 将图片增加到ViewPager
			return imageLayout;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return images.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == (arg1);
		}
		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
			
		}
		
		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public void startUpdate(View container) {
			
		}
	}
    
    @Override
	protected void onStart() {
		super.onStart();
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		//每隔10秒钟切换一张图片
		scheduledExecutorService.scheduleWithFixedDelay(new ViewPagerTask(), 5, 5, TimeUnit.SECONDS);
	}
	
	
	 //切换图片
	 private class ViewPagerTask implements Runnable {
	
	      @Override
	    public void run() {
         // TODO Auto-generated method stub
	    	  
	           currentItem = (currentItem +1) % picList.size();
	         //更新界面
	 //       handler.sendEmptyMessage(0);
            timeHandler.sendEmptyMessageDelayed(0x01, 5000);
	      }
	}
	    
	private Handler timeHandler = new Handler(){
	    @Override
	    public void handleMessage(Message msg) {
	        // TODO Auto-generated method stub
	         //设置当前页面
	    	picPager.setCurrentItem(currentItem);
	    	activity_main_biaotu_tv.setText(picList.get(currentItem).getTitle());
	    }
	        
    };
	

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
