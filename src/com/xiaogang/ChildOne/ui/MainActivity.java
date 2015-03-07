package com.xiaogang.ChildOne.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMusic;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.xiaogang.ChildOne.ActivityTack;
import com.xiaogang.ChildOne.R;
import com.xiaogang.ChildOne.adapter.GrowingAdapter;
import com.xiaogang.ChildOne.adapter.OnClickContentItemListener;
import com.xiaogang.ChildOne.data.BabyDATA;
import com.xiaogang.ChildOne.data.ErrorDATA;
import com.xiaogang.ChildOne.data.GrowingDATA;
import com.xiaogang.ChildOne.entity.Account;
import com.xiaogang.ChildOne.entity.Baby;
import com.xiaogang.ChildOne.entity.Favours;
import com.xiaogang.ChildOne.entity.Growing;
import com.xiaogang.ChildOne.util.*;
import com.xiaogang.ChildOne.util.face.FaceConversionUtil;
import com.xiaogang.ChildOne.widget.ContentListView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends BaseActivity implements
        View.OnClickListener,OnClickContentItemListener,ContentListView.OnRefreshListener, ContentListView.OnLoadListener {

    private ImageView leftbutton;

    private View callnameline;
    private Spinner growingManager;//成长管理下拉
    private ArrayAdapter<String> spinnerAdapter;
    private RelativeLayout footLayout;
    private RelativeLayout text;//文字
    private RelativeLayout photo;//拍照
    private RelativeLayout video;//摄像
    private RelativeLayout record;//录音
    private RelativeLayout picture;// 图库

    private ContentListView listView;
    private GrowingAdapter adapter;

    private String uid;
    private int pageIndex;
    private int pageSize;
    private String child_id;
    private Account account;
    private String identity;
    private Growing qGrowing;

    private List<Player> players = new LinkedList<Player>();
    private long waitTime = 2000;
    private long touchTime = 0;

    private List<Growing> growingList = new ArrayList<Growing>();
    private List<Baby> babies = new ArrayList<Baby>();//下拉列表宝宝
    private RequestQueue mRequestQueue;
    // 首先在您的Activity中添加如下成员变量
    final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
    String shareUrl = InternetURL.INTENT;
    String sharePic=InternetURL.INTENT+ "/Public/index/image/p_logo.png";//分享图片
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);
        initView();
        String accountStr = sp.getString("account", "");
        if (!StringUtil.isNullOrEmpty(accountStr)){
            try{
                account =getGson().fromJson(accountStr, Account.class);
                if (account != null){
                    uid = account.getUid();
                    pageIndex = 1;
                    pageSize = 20;
                }
            }catch (Exception e){
                Log.i("Account Gson Exception", "Account转换异常");
            }
        }
        identity = getGson().fromJson(sp.getString(Constants.IDENTITY, ""), String.class);

        mRequestQueue = Volley.newRequestQueue(this);
        adapter = new GrowingAdapter(growingList, this);
        adapter.setOnClickContentItemListener(this);
        listView.setAdapter(adapter);
        getData(ContentListView.REFRESH);
        getBaby();

        //初始化加载表情
        new Thread(new Runnable() {
            @Override
            public void run() {
                FaceConversionUtil.getInstace().getFileText(getApplication());
            }
        }).start();

        // 设置分享内容
        mController.setShareContent("");
        // 设置分享图片, 参数2为图片的url地址
        mController.setShareMedia(new UMImage(this, ""));


    }

    private void initView() {
        leftbutton = (ImageView) this.findViewById(R.id.chengzhang_back);
        leftbutton.setOnClickListener(this);
        listView = (ContentListView) this.findViewById(R.id.index_pull_refresh_lsv);
        text = (RelativeLayout) this.findViewById(R.id.foot_content);
        text.setOnClickListener(this);
        photo = (RelativeLayout) this.findViewById(R.id.foot_photo);
        photo.setOnClickListener(this);
        video = (RelativeLayout) this.findViewById(R.id.foot_video);
        video.setOnClickListener(this);
        record = (RelativeLayout) this.findViewById(R.id.foot_record);
        record.setOnClickListener(this);
        picture = (RelativeLayout) this.findViewById(R.id.foot_picture);
        picture.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chengzhang_back:
                finish();
                break;
            case R.id.foot_content://文字
                Intent textIntent = new Intent(MainActivity.this, PublishImageActivity.class);
                startActivity(textIntent);
                break;
            case R.id.foot_photo://拍照
                Intent takePhoto = new Intent(MainActivity.this, PublishPhotoActivity.class);
                startActivity(takePhoto);
                break;
            case R.id.foot_video://摄像
                Intent video = new Intent(MainActivity.this, PublishVideoActivity.class);
                startActivity(video);
                break;
            case R.id.foot_record://录音
                Intent record = new Intent(MainActivity.this, PublishRecordActivity.class);
                startActivity(record);
                break;
            case R.id.foot_picture://图库
                startActivity(new Intent(MainActivity.this, PublishPictureActivity.class));
                break;
        }
    }

    @Override
    public void onClickContentItem(final int position, int flag, final Object object) {
        qGrowing = growingList.get(position);

        switch (flag){
            case 1://收藏
                final Growing growing = growingList.get(position);
                String cancel;
                if ("1".equals(growing.getIs_favoured())){
                    cancel = "1";
                }else {
                    cancel = "";
                }
                String uri = String.format(InternetURL.FAVOURS_URL + "?growing_id=%s&uid=%s&user_type=%s&cancel=%s", growing.getId(), account.getUid(), identity, cancel);
                StringRequest request = new StringRequest(
                        Request.Method.GET,
                        uri,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                if (CommonUtil.isJson(s)){
                                    ErrorDATA data = getGson().fromJson(s, ErrorDATA.class);
                                    if (data.getCode() == 200){
                                        if (!"1".equals(growing.getIs_favoured())){
                                            growingList.get(position).setIs_favoured("1");
                                            addFavours(position);
                                            Toast.makeText(mContext, "已收藏", Toast.LENGTH_SHORT).show();
                                        }else {
                                            growingList.get(position).setIs_favoured("0");
                                            Toast.makeText(mContext, "已取消收藏", Toast.LENGTH_SHORT).show();
                                            removeFavours(position);
                                        }
                                        adapter.notifyDataSetChanged();
                                    }else {
                                        Toast.makeText(mContext, "收藏失败，请稍后重试", Toast.LENGTH_SHORT).show();
                                    }
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
                break;
            case 2://评论
                Intent comment = new Intent(MainActivity.this, CommentActivity.class);
                comment.putExtra(Constants.GROWING_KEY, growingList.get(position));
                startActivity(comment);
                break;
            case 3://分享
                final Growing grow = growingList.get(position);
//                private String dept;//文字描述
//                private String type;//0 文字 1 照片 2 视频
                String appID = "wxd97dd1adcce2b199";
                String appSecret = "7955104817ad2d367ff337ca56e12756";

                // 设置分享内容
                mController.setShareContent(grow.getDept());
                // 设置分享图片, 参数2为图片的url地址
                mController.setShareMedia(new UMImage(MainActivity.this, R.drawable.ic_launcher));
//                mController.setShareMedia(new UMImage(this, ""));
                 if(grow.getType().equals("0")){
                     //文字
                 }
                if(grow.getType().equals("1")){
                    //照片
                    if (!StringUtil.isNullOrEmpty(grow.getUrl())) {
                        String[] picUrls = grow.getUrl().split(",");
                        mController.setShareMedia(new UMImage(this, picUrls[0]));
                    }
                }
                if(grow.getDept().equals("2")){
                    //视频
                    UMVideo umVideo = new UMVideo(grow.getDept());
                    umVideo.setMediaUrl(grow.getUrl());
                    umVideo.setThumb(sharePic);
                    umVideo.setTitle(grow.getDept());
                    umVideo.setTargetUrl(shareUrl);
                    mController.setShareMedia(umVideo);
                }
                if(grow.getDept().equals("3")){
                    // 设置分享音乐
                    UMusic uMusic = new UMusic(grow.getUrl());
                    uMusic.setAuthor(grow.getDept());
                    uMusic.setTitle(grow.getDept());
                    //设置音乐缩略图
                    uMusic.setThumb(sharePic);
                    mController.setShareMedia(uMusic);
                }
                //1.添加QQ空间分享
                QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this, "100582055","f1a53f9becde189f56362e8b98e22f60");
                qZoneSsoHandler.setTargetUrl(shareUrl);
                qZoneSsoHandler.addToSocialSDK();
                //2.添加QQ好友分享
                UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, "100582055","f1a53f9becde189f56362e8b98e22f60");
                qqSsoHandler.setTargetUrl(shareUrl);
                qqSsoHandler.addToSocialSDK();
                // 添加微信平台
                UMWXHandler wxHandler = new UMWXHandler(this,appID);
                wxHandler.addToSocialSDK();
                //支持微信朋友圈
                UMWXHandler wxCircleHandler = new UMWXHandler(this,appID);
                wxCircleHandler.setToCircle(true);
                wxCircleHandler.addToSocialSDK();
                //单独设置微信分享
                WeiXinShareContent xinShareContent = new WeiXinShareContent();
                if(!"".equals(grow.getDept())){
                    xinShareContent.setShareContent(grow.getDept());
                    xinShareContent.setTitle(grow.getDept());
                }
                if(grow.getType().equals("1")){
                    //照片
                    if (!StringUtil.isNullOrEmpty(grow.getUrl())) {
                        String[] picUrls = grow.getUrl().split(",");
                        xinShareContent.setShareImage(new UMImage(this, picUrls[0]));
                    }
                }else{
                    xinShareContent.setShareImage(new UMImage(this, R.drawable.ic_launcher));
                }
                xinShareContent.setTargetUrl(shareUrl);
                mController.setShareMedia(xinShareContent);
                CircleShareContent circleMedia = new CircleShareContent();
                circleMedia.setShareContent(grow.getDept());
                circleMedia.setTitle(grow.getDept());
                    if(grow.getType().equals("1")){
                        //照片
                        if (!StringUtil.isNullOrEmpty(grow.getUrl())) {
                            String[] picUrls = grow.getUrl().split(",");
                            circleMedia.setShareImage(new UMImage(this, picUrls[0]));
                        }
                    }else{
                        circleMedia.setShareImage(new UMImage(this, R.drawable.ic_launcher));
                    }

                circleMedia.setTargetUrl(shareUrl);
                mController.setShareMedia(circleMedia);
                mController.openShare(this, false);
                break;
            case 4://播放音乐
                final Player player = new Player();
                qGrowing.setPlay(qGrowing.isPlay() ? false : true);
                for (Growing mGrowing : growingList){
                    if (mGrowing.isPlay() && !mGrowing.getId().equals(qGrowing.getId())){
                        if (players.size() > 0){
                            players.remove(0).stop();
                        }
                    }
                    if (!mGrowing.getId().equals(qGrowing.getId())){
                        mGrowing.setPlay(false);
                    }
                }
                if (players.size() > 0){
                    players.remove(0).stop();
                    adapter.notifyDataSetChanged();
                    return;
                }

                adapter.notifyDataSetChanged();
                players.add(player);
                getAppThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        player.playUrl((String) object);
                    }
                });
                break;
        }
    }

    /**
     * 处理添加收藏
     * @param position
     */
    private void addFavours(int position){
        Growing growing = growingList.get(position);
        growing.getFavours().setCount(growing.getFavours().getCount()+1);
        List<Favours> list = growing.getFavours().getList();
        Favours favours = new Favours();
        favours.setUid(account.getUid());

        if (identity.equals("1")) {
            favours.setName(account.getM_name());
            favours.setCover(account.getM_cover());
        }else if (identity.equals("0")){
            favours.setName(account.getF_name());
            favours.setCover(account.getF_cover());
        }else {
            favours.setName(account.getNick_name());
            favours.setCover(account.getCover());
        }
        list.add(favours);
    }

    /**
     * 处理取消收藏
     * @param position
     */
    private void removeFavours(int position){
        Growing growing = growingList.get(position);
        growing.getFavours().setCount(growing.getFavours().getCount()-1);
        List<Favours> list = growing.getFavours().getList();
        for (int i=list.size()-1; i>=0; i--){
            Favours favours = list.get(i);
            if (favours.getUid().equals(account.getUid())){
                list.remove(i);
            }
        }
    }

    private void getData(final int tag){
        if (!PhoneEnvUtil.isNetworkConnected(mContext)){
            Toast.makeText(mContext, R.string.check_network_isuse, Toast.LENGTH_SHORT).show();
            return;
        }
        String uri;
        if (StringUtil.isNullOrEmpty(child_id)) {
            uri = String.format(InternetURL.GROWING_MANAGER_API + "?uid=%s&pageIndex=%d&pageSize=%d", uid, pageIndex, pageSize);
        }else {
            uri = String.format(InternetURL.GROWING_MANAGER_API + "?uid=%s&pageIndex=%d&pageSize=%d&child_id=%s", uid, pageIndex, pageSize, child_id);
        }
        StringRequest request = new StringRequest(Request.Method.GET,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Gson gson = new Gson();
                        try {
                            GrowingDATA data = gson.fromJson(s, GrowingDATA.class);
                            if (data.getData().size() < 10){
                                listView.setResultSize(0);
                            }
                            switch (tag){
                                case ContentListView.REFRESH://刷新
                                    listView.onRefreshComplete();
                                    growingList.clear();
                                    break;
                                case ContentListView.LOAD://加载更多
                                    listView.onLoadComplete();
                                    break;
                            }
                            growingList.addAll(data.getData());
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

    private void getBaby(){
        String uid = account.getUid();
        String uri = String.format(InternetURL.GET_BABY_URL + "?uid=%s", uid);
        StringRequest request = new StringRequest(
                Request.Method.GET,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Gson gson = new Gson();
                        try{
                            BabyDATA data = gson.fromJson(s, BabyDATA.class);
                            babies.addAll(data.getData());
                            List<String> names = new ArrayList<String>();
                            for (int i=0; i<babies.size()+1; i++){
                                if (i==0){
                                    names.add("成长管理");
                                }else {
                                    names.add(babies.get(i-1).getName());
                                }
                            }
                            spinnerAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, names);
                            spinnerAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                            growingManager.setAdapter(spinnerAdapter);
                            growingManager.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if (position == 0) {
                                        child_id = "";
                                    } else{
                                        Baby baby = babies.get(position-1);
                                        child_id = baby.getId();
                                    }
                                    getData(ContentListView.REFRESH);

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        }catch (Exception e){
//                            ErrorDATA data = gson.fromJson(s, ErrorDATA.class);
//                            if (data.getCode() == 500){
//                                Log.i("ErrorData", "获取baby信息数据错误");
//                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }
        );
        mRequestQueue.add(request);
    }

    //下拉刷新
    @Override
    public void onRefresh() {
        pageIndex = 1;
        getData(ContentListView.REFRESH);
    }

    //上拉加载
    @Override
    public void onLoad() {
        pageIndex++;
        getData(ContentListView.LOAD);
    }

    /**
     * 再摁退出程序
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_BACK == keyCode){
            long currentTime = System.currentTimeMillis();
            if ((currentTime - touchTime) >= waitTime){
                Toast.makeText(mContext, "再摁退出登录", Toast.LENGTH_SHORT).show();
                touchTime = currentTime;
            }else {
                ActivityTack.getInstanse().exit(mContext);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
