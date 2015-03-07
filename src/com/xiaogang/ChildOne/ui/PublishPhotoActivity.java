package com.xiaogang.ChildOne.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
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
public class PublishPhotoActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back;
    private EditText content;
    private ImageView photoImage;
    private TextView publish;
    private Spinner spinner;
    private GridView gridView;
    private ArrayAdapter<String> spinnerAdapter;
    private String babyId;//要发布的宝宝ID
    private ProgressDialog progressDialog;

    private static int REQUEST_CODE = 1;
    private File file;
    private Bitmap photo;
    private String pictureDir;
    private Account account;
    private List<Baby> babies = new ArrayList<Baby>();//下拉列表宝宝
    private CheckBox isShare;//是否分享给老师

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_photo_layout);
        account = getGson().fromJson(sp.getString(Constants.ACCOUNT_KEY, ""), Account.class);
        initView();
        getBabyList();
        openCamera();
    }

    /**
     * 销毁图片文件
     */
    private void destoryBimap() {
        if (photo != null && !photo.isRecycled()) {
            photo.recycle();
            photo = null;
        }
    }

    private void openCamera(){
        destoryBimap();
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            String saveDir = Environment.getExternalStorageDirectory()
                    + "/ebaebo/photoCache";
            File dir = new File(saveDir);
            if (!dir.exists()) {
                dir.mkdir();
            }
            file = new File(saveDir, "photo.jpg");
            file.delete();
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(PublishPhotoActivity.this,
                            "文件为空",
                            Toast.LENGTH_LONG).show();
                    return;
                }
            }
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            startActivityForResult(intent, REQUEST_CODE);
        } else {
            Toast.makeText(PublishPhotoActivity.this,
                    "检查有无内存卡 ", Toast.LENGTH_LONG)
                    .show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                finish();
                return;
            }
            if (file != null && file.exists()) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                photo = BitmapFactory.decodeFile(file.getPath(), options);
                photoImage.setBackgroundDrawable(new BitmapDrawable(photo));
                pictureDir = file.getPath();
            } else {
                Toast.makeText(this, "图片文件为空", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void initView(){
        back = (ImageView) findViewById(R.id.publish_photo_back);
        photoImage = (ImageView) findViewById(R.id.publish_photo_imv);
        content = (EditText) findViewById(R.id.publish_photo_content);
        publish = (TextView) findViewById(R.id.publish_photo_run);
        spinner = (Spinner) findViewById(R.id.publish_photo_spinner);
        isShare = (CheckBox) findViewById(R.id.publish_photo_cb);

        back.setOnClickListener(this);
        publish.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.publish_photo_back://返回
                finish();
                break;

            case R.id.publish_photo_run://发布
                publishPhoto();
                break;
        }
    }

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

    private void publishPhoto(){
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

    private void publishRun(final String picPath){
        if (StringUtil.isNullOrEmpty(babyId)){
            Toast.makeText(mContext, "请选择宝宝", Toast.LENGTH_SHORT).show();
            return;
        }
        final String contentStr = content.getText().toString();
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
                params.put("content",contentStr);
                params.put("uid",account.getUid());
                params.put("user_type", user_type);
                params.put("type","1");
                params.put("child_id", babyId);
                params.put("url", picPath);
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
}
