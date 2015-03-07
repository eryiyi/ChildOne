package com.xiaogang.ChildOne.ui;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.xiaogang.ChildOne.ActivityTack;
import com.xiaogang.ChildOne.util.ToastUtil;
import com.xiaogang.ChildOne.util.upload.MultiPartStack;
import com.xiaogang.ChildOne.util.upload.MultiPartStringRequest;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by liuzwei on 2014/11/11.
 */
public class BaseActivity extends Activity {
    public Context mContext;
    public SharedPreferences sp;
    public LayoutInflater inflater;
    private RequestQueue mRequestQueue;
    private static RequestQueue mSingleQueue;

    private ActivityTack tack= ActivityTack.getInstanse();
    private ExecutorService appThread = Executors.newSingleThreadExecutor();

    private Gson gson = new Gson();

    ConnectivityManager connectMgr ;

    private final String mPageName = "BaseActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        sp = getSharedPreferences("ebaebo", Context.MODE_PRIVATE);
        inflater = LayoutInflater.from(mContext);
        connectMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        mRequestQueue = Volley.newRequestQueue(this);
        mSingleQueue = Volley.newRequestQueue(this, new MultiPartStack());
        tack.addActivity(this);

        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.updateOnlineConfig(this);
    }

    /**
     * 获得线程池
     * @return
     */
    public ExecutorService getAppThread() {
        return appThread;
    }

    public Gson getGson(){
        return gson;
    }

    public RequestQueue getRequestQueue(){
        return mRequestQueue;
    }

    protected Context getContext() {
        return mContext;
    }
    /**
     * 根据资源ID
     * @param resId
     */
    public void alert(int resId){
        ToastUtil.show(getApplicationContext(), resId);
    }

    //存储sharepreference
    public void save(String key, Object value){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, gson.toJson(value)).commit();
    }

    public ActivityTack getTack() {
        return tack;
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

        mSingleQueue.add(multiPartRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(mContext);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(mContext);
    }
}
