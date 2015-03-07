package com.xiaogang.ChildOne.ui;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.google.gson.Gson;
import com.xiaogang.ChildOne.R;
import com.xiaogang.ChildOne.data.BabyDATA;
import com.xiaogang.ChildOne.data.ErrorDATA;
import com.xiaogang.ChildOne.data.UploadDATA;
import com.xiaogang.ChildOne.demo.MP3Recorder;
import com.xiaogang.ChildOne.entity.Account;
import com.xiaogang.ChildOne.entity.Baby;
import com.xiaogang.ChildOne.util.CommonUtil;
import com.xiaogang.ChildOne.util.InternetURL;
import com.xiaogang.ChildOne.util.StringUtil;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuzwei on 2014/11/24.
 */
public class PublishRecordActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back;
    private TextView publish;
//    private ImageView play;
//    private ImageView cancel;
    private Button voice;
    private EditText content;

    private Spinner spinner;
    private GridView gridView;
    private ArrayAdapter<String> spinnerAdapter;
    private String babyId;//要发布的宝宝ID
    private ProgressDialog progressDialog;
    private Account account;
    private List<Baby> babies = new ArrayList<Baby>();//下拉列表宝宝

    /** log标记 */
    private static final String LOG_TAG = PublishRecordActivity.class.getSimpleName();
    /** 用于语音播放 */
    private MediaPlayer mPlayer = new MediaPlayer();
    /** 录音存储路径 */
    private static final String PATH = "/Recorder.mp3";
    private MP3Recorder recorder = new MP3Recorder(8000);

    private RelativeLayout recordRelativeLayout;
    private ImageView mRecordLight_1;
    private ImageView mRecordLight_2;
    private ImageView mRecordLight_3;

    private Animation mRecordLight_1_Animation;
    private Animation mRecordLight_2_Animation;
    private Animation mRecordLight_3_Animation;

    private static final int RECORD_NO = 0; // 不在录音
    private static final int RECORD_ING = 1; // 正在录音
    private static final int RECORD_ED = 2; // 完成录音
    private int mRecord_State = 0; // 录音的状态
    private boolean mPlayState; // 播放状态
    private int mPlayCurrentPosition;// 当前播放的时间


    private TextView deleteVoice;
    private LinearLayout voicePlayLayout;
    private ImageView voicePlay;
    private ProgressBar voiceProgressbar;
    private TextView voiceTime;

    private CheckBox isShare;

    /**
     * 用来控制动画效果
     */
    Handler mRecordLightHandler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (mRecord_State == RECORD_ING) {
                        mRecordLight_1.setVisibility(View.VISIBLE);
                        mRecordLight_1_Animation = AnimationUtils.loadAnimation(
                                PublishRecordActivity.this, R.anim.voice_anim);
                        mRecordLight_1.setAnimation(mRecordLight_1_Animation);
                        mRecordLight_1_Animation.startNow();
                    }
                    break;

                case 1:
                    if (mRecord_State == RECORD_ING) {
                        mRecordLight_2.setVisibility(View.VISIBLE);
                        mRecordLight_2_Animation = AnimationUtils.loadAnimation(
                                PublishRecordActivity.this, R.anim.voice_anim);
                        mRecordLight_2.setAnimation(mRecordLight_2_Animation);
                        mRecordLight_2_Animation.startNow();
                    }
                    break;
                case 2:
                    if (mRecord_State == RECORD_ING) {
                        mRecordLight_3.setVisibility(View.VISIBLE);
                        mRecordLight_3_Animation = AnimationUtils.loadAnimation(
                                PublishRecordActivity.this, R.anim.voice_anim);
                        mRecordLight_3.setAnimation(mRecordLight_3_Animation);
                        mRecordLight_3_Animation.startNow();
                    }
                    break;
                case 3:
                    if (mRecordLight_1_Animation != null) {
                        mRecordLight_1.clearAnimation();
                        mRecordLight_1_Animation.cancel();
                        mRecordLight_1.setVisibility(View.GONE);

                    }
                    if (mRecordLight_2_Animation != null) {
                        mRecordLight_2.clearAnimation();
                        mRecordLight_2_Animation.cancel();
                        mRecordLight_2.setVisibility(View.GONE);
                    }
                    if (mRecordLight_3_Animation != null) {
                        mRecordLight_3.clearAnimation();
                        mRecordLight_3_Animation.cancel();
                        mRecordLight_3.setVisibility(View.GONE);
                    }

                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_activity);
        account = getGson().fromJson(sp.getString(Constants.ACCOUNT_KEY, ""), Account.class);
        getBabyList();
        initView();
    }

    private void initView(){
        recordRelativeLayout = (RelativeLayout) this.findViewById(R.id.record_relative_layout);
        back = (ImageView) findViewById(R.id.publish_record_back);
        deleteVoice = (TextView) this.findViewById(R.id.record_activity_delete);
        deleteVoice.setOnClickListener(this);
        publish = (TextView) findViewById(R.id.publish_record_run);
//        play = (ImageView) findViewById(R.id.publish_record_play);
        voice = (Button) findViewById(R.id.voice_record_btn);
        spinner = (Spinner) findViewById(R.id.publish_record_spinner);
        content = (EditText) findViewById(R.id.publish_record_content);
//        cancel = (ImageView) findViewById(R.id.publish_record_cancel);
        isShare = (CheckBox) findViewById(R.id.publish_record_cb);

        mRecordLight_1 = (ImageView) findViewById(R.id.voice_recordinglight_1);
        mRecordLight_2 = (ImageView) findViewById(R.id.voice_recordinglight_2);
        mRecordLight_3 = (ImageView) findViewById(R.id.voice_recordinglight_3);

        voicePlayLayout = (LinearLayout) this.findViewById(R.id.voice_display_voice_layout);
        voicePlay = (ImageView) this.findViewById(R.id.voice_display_voice_play);
        voiceProgressbar = (ProgressBar) this.findViewById(R.id.voice_display_voice_progressbar);
        voiceTime = (TextView) this.findViewById(R.id.voice_display_voice_time);
         voicePlay.setOnClickListener(this);

//        play.setVisibility(View.GONE);
//        cancel.setVisibility(View.GONE);
//        cancel.setOnClickListener(this);
        back.setOnClickListener(this);
        publish.setOnClickListener(this);
//        play.setOnClickListener(this);
        voice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        // 开始动画效果
                        startRecordLightAnimation();
                        mRecord_State = RECORD_ING;
                        recorder.start(Environment.getExternalStorageDirectory() + PATH);
                        break;
                    case MotionEvent.ACTION_UP:
                            // 停止动画效果
                            stopRecordLightAnimation();
                            // 修改录音状态
                            recordRelativeLayout.setVisibility(View.GONE);
                            mRecord_State = RECORD_ED;
                            recorder.stop();

                        voicePlayLayout.setVisibility(View.VISIBLE);
                        voicePlay.setImageResource(R.drawable.globle_player_btn_play);
                        voiceProgressbar.setMax(CommonUtil.getMp3TrackLength(new File(Environment.getExternalStorageDirectory() + PATH)));
                        voiceProgressbar.setProgress(0);
                        voiceTime.setText(CommonUtil.getMp3TrackLength(new File(Environment.getExternalStorageDirectory() + PATH)) + "″");
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 获得宝宝列表
     */
    private void  getBabyList(){
        String uri = String.format(InternetURL.GET_BABY_URL + "?uid=%s", account.getUid());
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
                            for (int i=0; i<babies.size(); i++){
                                names.add(babies.get(i).getName());
                            }
                            spinnerAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, names);
                            spinnerAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                            spinner.setAdapter(spinnerAdapter);
                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    Baby baby = babies.get(position);
                                    babyId = baby.getId();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        }catch (Exception e){
                            Toast.makeText(mContext, "获取宝宝列表出错", Toast.LENGTH_SHORT).show();
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
    }
    /**
     * 开始动画效果
     */
    private void startRecordLightAnimation() {
        mRecordLightHandler.sendEmptyMessageDelayed(0, 0);
        mRecordLightHandler.sendEmptyMessageDelayed(1, 1000);
        mRecordLightHandler.sendEmptyMessageDelayed(2, 2000);
    }

    /**
     * 停止动画效果
     */
    private void stopRecordLightAnimation() {
        mRecordLightHandler.sendEmptyMessage(3);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.publish_record_back://返回
                finish();
                break;
            case R.id.publish_record_run://发布
                progressDialog = new ProgressDialog(PublishRecordActivity.this);
                progressDialog.setMessage("正在发布，请稍后");
                progressDialog.show();
                uploadRecord();
                break;
            case R.id.record_activity_delete:
                File file = new File(Environment.getExternalStorageDirectory() + PATH);
                if (file.isFile() && file.exists()){
                    file.delete();
                    voicePlayLayout.setVisibility(View.GONE);
//                    play.setVisibility(View.GONE);
//                    cancel.setVisibility(View.GONE);
                    Toast.makeText(mContext, "删除文件成功", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.voice_display_voice_play://播放音乐
// 播放录音
                if (!mPlayState) {
                    mPlayer = new MediaPlayer();
                    try {
                        // 添加录音的路径
                        mPlayer.setDataSource(Environment.getExternalStorageDirectory() + PATH);
                        // 准备
                        mPlayer.prepare();
                        // 播放
                        mPlayer.start();
                        // 根据时间修改界面
                        new Thread(new Runnable() {
                            public void run() {
                                voiceProgressbar.setMax(CommonUtil.getMp3TrackLength(new File(Environment.getExternalStorageDirectory() + PATH)));
                                mPlayCurrentPosition = 0;
                                while (mPlayer.isPlaying()) {
                                    mPlayCurrentPosition = mPlayer.getCurrentPosition() / 1000;
                                    voiceProgressbar.setProgress(mPlayCurrentPosition);
                                }
                            }
                        }).start();
                        // 修改播放状态
                        mPlayState = true;
                        // 修改播放图标
                        voicePlay.setImageResource(R.drawable.globle_player_btn_stop);

                        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            // 播放结束后调用
                            public void onCompletion(MediaPlayer mp) {
                                // 停止播放
                                mPlayer.stop();
                                // 修改播放状态
                                mPlayState = false;
                                // 修改播放图标
                                voicePlay.setImageResource(R.drawable.globle_player_btn_play);
                                // 初始化播放数据
                                mPlayCurrentPosition = 0;
                                voiceProgressbar.setProgress(mPlayCurrentPosition);
                            }
                        });

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (mPlayer != null) {
                        // 根据播放状态修改显示内容
                        if (mPlayer.isPlaying()) {
                            mPlayState = false;
                            mPlayer.stop();
                            voicePlay.setImageResource(R.drawable.globle_player_btn_play);
                            mPlayCurrentPosition = 0;
                            voiceProgressbar.setProgress(mPlayCurrentPosition);
                        } else {
                            mPlayState = false;
                            voicePlay.setImageResource(R.drawable.globle_player_btn_play);
                            mPlayCurrentPosition = 0;
                            voiceProgressbar.setProgress(mPlayCurrentPosition);
                        }
                    }
                }
                break;
        }
    }

    private void uploadRecord(){
        if (StringUtil.isNullOrEmpty(babyId)){
            progressDialog.dismiss();
            Toast.makeText(mContext, "请选择宝宝", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtil.isNullOrEmpty(Environment.getExternalStorageDirectory() + PATH)){
            progressDialog.dismiss();
            Toast.makeText(mContext, "请录制音频，然后发布", Toast.LENGTH_SHORT).show();
            return;
        }
        File file = new File(Environment.getExternalStorageDirectory() + PATH);
        if (!file.exists() || !file.isFile()){
            progressDialog.dismiss();
            Toast.makeText(mContext, "请录制音频，然后发布", Toast.LENGTH_SHORT).show();
            return;
        }
        if (CommonUtil.getMp3TrackLength(new File(Environment.getExternalStorageDirectory() + PATH)) < 1){
            progressDialog.dismiss();
            Toast.makeText(mContext, "时长小于一秒，请重新录制", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, File> files = new HashMap<String, File>();
        files.put("file", file);
        Map<String, String> params = new HashMap<String, String>();
        addPutUploadFileRequest(
                InternetURL.UPLOAD_FILE,
                files,
                params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (CommonUtil.isJson(s)) {
                            UploadDATA data = getGson().fromJson(s, UploadDATA.class);
                            if (data.getCode() == 200){
                                publishRun(data.getUrl());
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                },
                null);
    }

    private void publishRun(final String recordPath){

        final String user_type = getGson().fromJson(sp.getString(Constants.IDENTITY, ""), String.class);

        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GROWING_PUSH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (CommonUtil.isJson(s)){
                            ErrorDATA data  = getGson().fromJson(s, ErrorDATA.class);
                            if (data.getCode() == 200){
                                if (progressDialog != null){
                                    progressDialog.dismiss();
                                }
                                Toast.makeText(mContext, "发布成功", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (progressDialog != null){
                            progressDialog.dismiss();
                        }
                        Toast.makeText(mContext, "发布失败，请稍后重试 ", Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("uid",account.getUid());
                params.put("user_type", user_type);
                params.put("type","3");
                params.put("child_id", babyId);
                params.put("url", recordPath);
                params.put("content", content.getText().toString());
                if (isShare.isChecked()){
                    params.put("is_share", "1");
                }
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        getRequestQueue().add(request);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recorder.stop();
    }
}
