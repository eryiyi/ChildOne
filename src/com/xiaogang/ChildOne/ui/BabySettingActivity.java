package com.xiaogang.ChildOne.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaogang.ChildOne.HomeBabyApplication;
import com.xiaogang.ChildOne.R;
import com.xiaogang.ChildOne.adapter.AnimateFirstDisplayListener;
import com.xiaogang.ChildOne.data.BabySetDATA;
import com.xiaogang.ChildOne.data.BabySetNCDATA;
import com.xiaogang.ChildOne.data.ErrorDATA;
import com.xiaogang.ChildOne.data.UploadDATA;
import com.xiaogang.ChildOne.entity.Account;

import com.xiaogang.ChildOne.entity.BabySet;
import com.xiaogang.ChildOne.util.CommonUtil;
import com.xiaogang.ChildOne.util.CompressPhotoUtil;
import com.xiaogang.ChildOne.util.InternetURL;
import com.xiaogang.ChildOne.util.StringUtil;
import com.xiaogang.ChildOne.util.upload.MultiPartStack;
import com.xiaogang.ChildOne.util.upload.MultiPartStringRequest;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuzwei on 2014/11/14.
 */
public class BabySettingActivity extends BaseActivity implements View.OnClickListener{
    ImageView back;//返回按钮
    private ImageView tx;//头像
    private EditText nameText;//name
    private TextView guanxiText;//关系
    private TextView set;
    private String identity;
    private Account account;
    private RequestQueue mRequestQueue;
    private String pics = "";
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private static final String TAG = MumSettingActivity.class.getSimpleName();
    private static final File PHOTO_CACHE_DIR = new File(Environment.getExternalStorageDirectory() + "/ebaebo/PhotoCache");
    private static RequestQueue mSingleQueue;
    private String nickName;
    BabySet babySet = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baobao_setting);
        account = getGson().fromJson(sp.getString(Constants.ACCOUNT_KEY, ""), Account.class);
        identity = getGson().fromJson(sp.getString(Constants.IDENTITY, ""), String.class);
        mRequestQueue = Volley.newRequestQueue(this);
        mSingleQueue = Volley.newRequestQueue(this, new MultiPartStack());
        initView();
        initData();
        getData();
    }
    private void getData(){
        String uri = String.format(InternetURL.SELECT_BABY_URL + "?uid=%s", account.getUid());
        StringRequest request = new StringRequest(Request.Method.GET,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Gson gson = new Gson();
                        try {
                            BabySetDATA data = gson.fromJson(s, BabySetDATA.class);
                            if(data.getCode() == 200){
                                babySet = data.getData();
                                nameText.setText(babySet.getName());
                                try {
                                    imageLoader.displayImage(babySet.getCover() , tx, HomeBabyApplication.options, animateFirstListener);
                                }catch (Exception e){
                                //Log.d("没有网络图片", e.getMessage());
                                }
                            }
                           else{
                                Toast.makeText(mContext, "查询宝宝失败", Toast.LENGTH_SHORT).show();
                            }

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
    private void initData() {
        if(identity.equals("1")){
            guanxiText.setText("关系：母子");
        }else{
            guanxiText.setText("关系：父子");
        }

    }

    private void initView(){
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        set = (TextView) this.findViewById(R.id.set);
        set.setOnClickListener(this);
        tx = (ImageView) this.findViewById(R.id.tx);
        tx.setOnClickListener(this);
        nameText = (EditText) this.findViewById(R.id.name);
        guanxiText = (TextView) this.findViewById(R.id.guanxi);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.set:
                //设置
                nickName = nameText.getText().toString();
                if (!StringUtil.isNullOrEmpty(pics)) {
                    Map<String, File> files = new HashMap<String, File>();
                    files.put("file", new File(pics));
                    Map<String, String> params = new HashMap<String, String>();
                    addPutUploadFileRequest(
                            InternetURL.UPLOAD_FILE,
                            files,
                            params,
                            mResonseListenerString,
                            mErrorListener,
                            null);
                }else {
                    if(babySet!=null){
                        pics = babySet.getCover().replaceAll(Constants.API_HEAD, "");;
                    }
                    if (StringUtil.isNullOrEmpty(pics)){
                        Toast.makeText(mContext, "请设置头像", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    setting(pics);
                }

                break;
            case R.id.tx:
                //头像设置
                ShowPickDialog();
                break;
        }
    }

    Response.Listener<String> mResonseListenerString = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            if (CommonUtil.isJson(response)){
                UploadDATA data = getGson().fromJson(response, UploadDATA.class);
                setting(data.getUrl());
            }
            Log.i(TAG, " on response String" + response.toString());
        }
    };

    Response.ErrorListener mErrorListener = new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {
            if (error != null) {
                if (error.networkResponse != null)
                    Log.i(TAG, " error " + new String(error.networkResponse.data));
            }
        }
    };
    /**
     * 选择提示对话框
     */
    private void ShowPickDialog() {
        new AlertDialog.Builder(this)
                .setTitle("设置头像...")
                .setNegativeButton("相册", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(Intent.ACTION_PICK, null);
                        intent.setDataAndType(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                "image/*");
                        startActivityForResult(intent, 1);

                    }
                })
                .setPositiveButton("拍照", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        Intent intent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        //下面这句指定调用相机拍照后的照片存储的路径
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                                .fromFile(new File(Environment
                                        .getExternalStorageDirectory(),
                                        "ebaeboTx.jpg")));
                        startActivityForResult(intent, 2);
                    }
                }).show()
                .setCanceledOnTouchOutside(true);//点击弹框外部，弹框消失
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // 如果是直接从相册获取
            case 1:
                if(data!=null){
                    startPhotoZoom(data.getData());
                }
                break;
            // 如果是调用相机拍照时
            case 2:
                File temp = new File(Environment.getExternalStorageDirectory()
                        + "/ebaeboTx.jpg");
                startPhotoZoom(Uri.fromFile(temp));
                break;
            // 取得裁剪后的图片
            case 3:
                if(data != null){
                    setPicToView(data);
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 裁剪图片方法实现
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    /**
     * 保存裁剪之后的图片数据
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(photo);
            if(photo!=null){
                pics = CompressPhotoUtil.saveBitmap2file(photo, System.currentTimeMillis() + ".jpg", PHOTO_CACHE_DIR);
                tx.setImageBitmap(photo);
            }
        }
    }

    public static void addPutUploadFileRequest(final String url,
                                               final Map<String, File> files, final Map<String, String> params,
                                               final Response.Listener<String> responseListener, final Response.ErrorListener errorListener,
                                               final Object tag) {
        if (null == url || null == responseListener) {
            return;
        }

        MultiPartStringRequest multiPartRequest = new MultiPartStringRequest(
                Request.Method.POST, url, responseListener, errorListener) {

            @Override
            public Map<String, File> getFileUploads() {
                return files;
            }

            @Override
            public Map<String, String> getStringUploads() {
                return params;
            }

        };

        Log.i(TAG, " volley put : uploadFile " + url);

        mSingleQueue.add(multiPartRequest);
    }

    private void setting(final String cover){
//        String uri = String.format(InternetURL.SELECT_BABY_URL+"?uid=%s&name=%s&cover=%s", account.getUid(), nickName,InternetURL.INTENT +cover);
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.SELECT_BABY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (CommonUtil.isJson(s)) {
                            BabySetNCDATA errorDATA = getGson().fromJson(s, BabySetNCDATA.class);
                            if (errorDATA.getCode() == 200){
                                Toast.makeText(mContext, "设置成功", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(mContext, "设置失败，请稍后重试1", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(mContext, "设置失败，请稍后重试", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(mContext, "设置失败，请稍后重试", Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("uid", account.getUid());
                params.put("name", nickName);
                params.put("cover",cover);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        mSingleQueue.add(request);
    }
}
