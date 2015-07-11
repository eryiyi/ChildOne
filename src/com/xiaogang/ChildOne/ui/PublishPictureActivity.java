package com.xiaogang.ChildOne.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.google.gson.reflect.TypeToken;
import com.xiaogang.ChildOne.HomeBabyApplication;
import com.xiaogang.ChildOne.R;
import com.xiaogang.ChildOne.adapter.GridImageAdapter;
import com.xiaogang.ChildOne.data.BabyDATA;
import com.xiaogang.ChildOne.data.ErrorDATA;
import com.xiaogang.ChildOne.data.UploadDATA;
import com.xiaogang.ChildOne.entity.Account;
import com.xiaogang.ChildOne.entity.Baby;
import com.xiaogang.ChildOne.util.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by liuzwei on 2014/11/26.
 */
public class PublishPictureActivity extends BaseActivity implements View.OnClickListener{
    private final static int SELECT_LOCAL_PHOTO = 110;

    private GridView gridView;
    private ImageView back;
    private TextView publish;
    private EditText content;
    private GridImageAdapter gridImageAdapter;
    private Spinner spinner;
    private ArrayAdapter<String> spinnerAdapter;

    private ArrayList<String> dataList = new ArrayList<String>();
    private ArrayList<String> tDataList = new ArrayList<String>();
    private List<String> uploadPaths = new ArrayList<String>();
    private List<Baby> babies = new ArrayList<Baby>();//下拉列表宝宝
    private String babyId;
    private ProgressDialog progressDialog;

    private CheckBox isShare;
    private Uri uri;
    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_picture_layout);
        account = getGson().fromJson(sp.getString(Constants.ACCOUNT_KEY, ""), Account.class);
        initView();
        getBabyList();
        openPhotoAlbum();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.publish_picture_back://返回
                finish();
                break;
            case R.id.publish_picture_run://发布
                progressDialog = new ProgressDialog(PublishPictureActivity.this);
                progressDialog.setMessage("正在发布，请稍后");
                progressDialog.show();
                //检查有没有选择图片
                if (dataList.size() == 1){
                    progressDialog.dismiss();
                    Toast.makeText(mContext, R.string.check_is_picture, Toast.LENGTH_SHORT).show();
                    return;
                }
                //检查有没有选择宝宝
                if (StringUtil.isNullOrEmpty(babyId)){
                    progressDialog.dismiss();
                    Toast.makeText(mContext, "请选择宝宝", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (int i=0; i<dataList.size(); i++){
                    File file = new File(dataList.get(i));

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
                                            uploadPaths.add(data.getUrl());
                                        }
                                        //说明文件已经上传完毕
                                        if (uploadPaths.size() == dataList.size()-1){
                                            publishAll();
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

                break;
        }
    }
    //上传完图片后开始发布
    private void publishAll(){

        final String contentStr = content.getText().toString();
        final String user_type = getGson().fromJson(sp.getString(Constants.IDENTITY, ""), String.class);
        final StringBuffer filePath = new StringBuffer();
        for (int i=0; i<uploadPaths.size(); i++){
            filePath.append(uploadPaths.get(i));
            if (i != uploadPaths.size()-1){
                filePath.append(",");
            }
        }

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
                params.put("url", filePath.toString());
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

    private void initView(){
        back = (ImageView) findViewById(R.id.publish_picture_back);
        publish = (TextView) findViewById(R.id.publish_picture_run);

        content = (EditText) findViewById(R.id.publish_picture_content);
        back.setOnClickListener(this);
        publish.setOnClickListener(this);

        gridView = (GridView) findViewById(R.id.gridview_image);
        gridImageAdapter = new GridImageAdapter(mContext, dataList);
        gridView.setAdapter(gridImageAdapter);

        isShare = (CheckBox) findViewById(R.id.publish_picture_cb);
        spinner = (Spinner) findViewById(R.id.publish_picture_spinner);

        gridView.setOnItemClickListener(new GridView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String path = dataList.get(position);
                if (path.contains("default") && position == dataList.size() -1 && dataList.size() -1 != 9) {
                    showSelectImageDialog();
                } else {
                    Intent intent = new Intent(PublishPictureActivity.this, ImageDelActivity.class);
                    intent.putExtra("position", position);
                    intent.putExtra("path", dataList.get(position));
                    startActivityForResult(intent, CommonDefine.DELETE_IMAGE);
                }
            }
        });
    }

    // 选择相册，相机
    private void showSelectImageDialog() {
        final Dialog picAddDialog = new Dialog(PublishPictureActivity.this, R.style.dialog);
        View picAddInflate = View.inflate(PublishPictureActivity.this, R.layout.item_dialog_camera, null);
        TextView camera = (TextView) picAddInflate.findViewById(R.id.camera);
        camera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {// 选择相机
                Intent cameraIntent = new Intent();
                cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.addCategory(Intent.CATEGORY_DEFAULT);
                // 根据文件地址创建文件
                File file = new File(CommonDefine.FILE_PATH);
                if (file.exists()) {
                    file.delete();
                }
                uri = Uri.fromFile(file);
                // 设置系统相机拍摄照片完成后图片文件的存放地址
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                // 开启系统拍照的Activity
                startActivityForResult(cameraIntent, CommonDefine.TAKE_PICTURE_FROM_CAMERA);
                picAddDialog.dismiss();
            }
        });
        TextView mapStroge = (TextView) picAddInflate.findViewById(R.id.mapstorage);
        mapStroge.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {// 选择图库
                Intent intent = new Intent(PublishPictureActivity.this, AlbumActivity.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("dataList", getIntentArrayList(dataList));
                bundle.putString("editContent", content.getText().toString());
                intent.putExtras(bundle);
                startActivityForResult(intent, CommonDefine.TAKE_PICTURE_FROM_GALLERY);
                picAddDialog.dismiss();
//                AlbumEditActivity.this.finish();
            }
        });
        TextView cancel = (TextView) picAddInflate.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                picAddDialog.dismiss();
            }
        });
        picAddDialog.setContentView(picAddInflate);
        picAddDialog.show();
    }

    /**
     * 打开相册
     */
    private void openPhotoAlbum() {
        startActivityForResult(new Intent(PublishPictureActivity.this, AlbumActivity.class), SELECT_LOCAL_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            switch (requestCode){
                case SELECT_LOCAL_PHOTO:
                    tDataList =data.getStringArrayListExtra("datalist");
                    if (tDataList != null) {
                        for (int i = 0; i < tDataList.size(); i++) {
                            String string = tDataList.get(i);
                            dataList.add(string);
                        }
                        if (dataList.size() < 9) {
                            dataList.add("camera_default");
                        }
                        gridImageAdapter.notifyDataSetChanged();
                    }else {
                        finish();
                    }
                    break;
                case CommonDefine.TAKE_PICTURE_FROM_CAMERA:
                    String sdStatus = Environment.getExternalStorageState();
                    if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
                        return;
                    }
                    Bitmap bitmap = ImageUtils.getUriBitmap(this, uri, 400, 400);
                    String cameraImagePath = FileUtils.saveBitToSD(bitmap, System.currentTimeMillis() + "");

//				Bundle bundle = data.getExtras();
//				Bitmap bitmap = (Bitmap) bundle.get("data");
//				String cameraImagePath = ImageUtils.setCameraImage(bitmap);

                    for (int i = 0; i < dataList.size(); i++) {
                        String path = dataList.get(i);
                        if(path.contains("default")) {
                            dataList.remove(dataList.size() - 1);
                        }
                    }
                    dataList.add(cameraImagePath);
                    if(dataList.size() < 9) {
                        dataList.add("camera_default");
                    }
                    gridImageAdapter.notifyDataSetChanged();
                    break;
                case CommonDefine.TAKE_PICTURE_FROM_GALLERY:
//                    Bundle bundle2 = data.getExtras();
                    tDataList = data.getStringArrayListExtra("datalist");
                    if (tDataList != null) {
                        dataList.clear();
                        for (int i = 0; i < tDataList.size(); i++) {
                            String string = tDataList.get(i);
                            dataList.add(string);
                        }
                        if (dataList.size() < 9) {
                            dataList.add("camera_default");
                        }
                        gridImageAdapter.notifyDataSetChanged();
                    }

                    break;
                case CommonDefine.DELETE_IMAGE:
                    int position = data.getIntExtra("position", -1);
                    dataList.remove(position);
                    if(dataList.size() < 9 ) {
                        dataList.add(dataList.size(), "camera_default");
                        for (int i = 0; i < dataList.size(); i++) {
                            String path = dataList.get(i);
                            if(path.contains("default")) {
                                if (i != 8) {
                                    dataList.remove(dataList.size() - 2);
                                }
                            }
                        }
                    }
                    gridImageAdapter.notifyDataSetChanged();
                    break;
            }

        }
    }

    private ArrayList<String> getIntentArrayList(ArrayList<String> dataList) {

        ArrayList<String> tDataList = new ArrayList<String>();

        for (String s : dataList) {
            if (!s.contains("default")) {
                tDataList.add(s);
            }
        }
        return tDataList;
    }



    /**
     * 获得spinner下的宝宝列表
     */
    private void  getBabyList(){
    	String uri ="";
    	if(HomeBabyApplication.is_student.equals("1")){
    		uri = String.format(InternetURL.GET_BABY_URL + "?uid=%s", account.getUid());
    	}else if(HomeBabyApplication.is_student.equals("0")){
    		uri = String.format(InternetURL.TEACHEAR_GET_BABY_URL+"?uid=%s&class_id=%s", account.getUid(),account.getClass_id());
    	}
        StringRequest request = new StringRequest(
                Request.Method.GET,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Gson gson = new Gson();
                        List<String> names = null;
                        try{
                        	if(HomeBabyApplication.is_student.equals("1")){
                       		 BabyDATA data = gson.fromJson(s, BabyDATA.class);
                                babies.addAll(data.getData());
                                names = new ArrayList<String>();
                                for (int i=0; i<babies.size(); i++){
                                    names.add(babies.get(i).getName());
                                }
                       	}else{
                       		JSONObject jsonObject = new JSONObject(s);
                           	String result_code = jsonObject.getString("code");
                           	if(result_code.equals("200")){
                           		JSONArray jsonDatas = jsonObject.getJSONObject("data").getJSONArray("child");
                               	babies = gson.fromJson(jsonDatas.toString(), new TypeToken<List<Baby>>(){}.getType());
                               	names = new ArrayList<String>();
                                    for (int i=0; i<babies.size(); i++){
                                        names.add(babies.get(i).getName());
                                    }
                           	}
                       	}
                            spinnerAdapter = new ArrayAdapter<String>(PublishPictureActivity.this, android.R.layout.simple_spinner_item, names);
                            spinnerAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                            spinner.setAdapter(spinnerAdapter);
                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    Baby baby = babies.get(position);
                                    babyId = baby.getChild_id();
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
        getRequestQueue().add(request);
    }
}
