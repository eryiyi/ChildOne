package com.xiaogang.ChildOne.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.google.gson.Gson;
import com.xiaogang.ChildOne.data.ErrorDATA;
import com.xiaogang.ChildOne.util.CommonUtil;
import com.xiaogang.ChildOne.util.InternetURL;
import com.xiaogang.ChildOne.util.upload.MultiPartStack;

import java.util.ArrayList;

/**
 * Created by liuzwei on 2014/12/12.
 */
public class MessageService extends Service {
    private static final String TAG = MessageService.class.getSimpleName();
    private static boolean isRun = true;

    private MessageBinder messageBinder = new MessageBinder();
    private String uid;
    private String friend_id;
    private String user_type;
    private Intent intent = new Intent("com.app.ebaebo.ui.RECEIVER");
    private static RequestQueue requestQueue;
    private MessageThread messageThread;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    getMsg();
                    break;
            }
        }
    } ;
    @Override
    public IBinder onBind(Intent intent) {

        Log.i(TAG, "Start bind...");
        return messageBinder;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "start onCreate~~~");
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.e(TAG, "start onStart~~~");
        super.onStart(intent, startId);
        ArrayList<String> data = intent.getStringArrayListExtra("getData");
        uid = data.get(0);
        friend_id = data.get(1);
        user_type = data.get(2);
        requestQueue = Volley.newRequestQueue(getApplicationContext(), new MultiPartStack());

        isRun = true;
        messageThread = new MessageThread();
        new Thread(messageThread).start();
    }

    @Override
    public void onDestroy() {
         Log.e(TAG, "start onDestroy~~~");
        isRun = false;
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG, "start onUnbind~~~");
        isRun = false;
        return super.onUnbind(intent);
    }

    public class MessageBinder extends Binder {
         public MessageService getService(){
             return MessageService.this;
         }
    }

    private void getMsg() {
        StringRequest request = new StringRequest(
                Request.Method.GET,
                String.format(InternetURL.MESSAGE_DETAIL_LIST + "?uid=%s&friend_id=%s&new=1&user_type=%s", uid, friend_id, user_type),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //

                        Gson gson = new Gson();
                        if (CommonUtil.isJson(s)) {

                            ErrorDATA data = gson.fromJson(s, ErrorDATA.class);
                            if (data.getCode() == -1) {
                                Log.e(TAG, data.getMsg());

                            } else {
                                intent.putExtra("messages", s);
                                sendBroadcast(intent);
                            }
                        }

                        if (messageThread != null){
                            synchronized (messageThread) {
                                messageThread.notify();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //
                        Log.e(TAG, volleyError.toString());
                    }
                }
        );
        requestQueue.add(request);

    }

    public class MessageThread implements Runnable {
        @Override
        public void run() {
            while (isRun){
                try {
                    Thread.sleep(1000);
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                    synchronized (this){
                        this.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
