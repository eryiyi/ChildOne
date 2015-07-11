package com.xiaogang.ChildOne.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.xiaogang.ChildOne.HomeBabyApplication;
import com.xiaogang.ChildOne.R;
import com.xiaogang.ChildOne.adapter.PictureAdapter;
import com.xiaogang.ChildOne.data.ErrorDATA;
import com.xiaogang.ChildOne.data.PhotosDATA;
import com.xiaogang.ChildOne.data.UploadDATA;
import com.xiaogang.ChildOne.entity.Photos;
import com.xiaogang.ChildOne.entity.Pictures;
import com.xiaogang.ChildOne.util.CommonUtil;
import com.xiaogang.ChildOne.util.FileUtils;
import com.xiaogang.ChildOne.util.InternetURL;
import com.xiaogang.ChildOne.util.TimeUtils;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

/**
 * author: ${zhanghailong}
 * Date: 2014/11/16
 * Time: 23:06
 * 类的功能、说明写在此处.
 */
public class PictureActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back;
    private TextView title;
    private PictureAdapter adapter;
    private ListView clv;
    private TextView shangchuan_tv;
    
    private Button paizhao_bt = null;
   	private Button xiangce_bt = null;
   	private Button cancle_bt = null;
   	private AlertDialog dialog = null;
   	
   	private String imageName = null;
   	private Uri mPhotoOnSDCardUri = null;
   	public  File imageFile = null;
   	private Cursor cursor = null;
    private RequestQueue mRequestQueue;
   	
   	private  Photos photos;
   	private ProgressDialog pd;

    List<Pictures> list = new ArrayList<Pictures>();
    private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			case 0x01:
				Toast.makeText(mContext, "上传失败", 0).show();
				pd.dismiss();
				break;
			case 0x02:
				pd.dismiss();
				adapter.notifyDataSetChanged();
				break;
			}
		}
    	
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pictures);
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        initView();
        setTheme(R.style.index_theme);
        photos = (Photos) getIntent().getExtras().get("photo");
        mRequestQueue = Volley.newRequestQueue(this);
        if(photos!=null){
            list = photos.getList();
            title.setText(photos.getName());
        }
        adapter = new PictureAdapter(list, this);
        clv.setAdapter(adapter);
    }

    private void initView() {
        back = (ImageView) this.findViewById(R.id.picturesback);
        title  = (TextView) this.findViewById(R.id.title);
        shangchuan_tv = (TextView) this.findViewById(R.id.shangchuan_tv);
        clv = (ListView) this.findViewById(R.id.lstv);
        shangchuan_tv.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.picturesback:
                finish();
                break;
            case R.id.shangchuan_tv:
            	dialog();
            	break;
            case R.id.cancle_bt:
            	dialog.dismiss();
            	break;
            case R.id.paizhao_bt:
            	imageName = TimeUtils.getFileName();
				imageFile = new File(FileUtils.createFile(),imageName);
            	paiZhao();
            	dialog.dismiss();
            	break;
            case R.id.xiangce_bt:
            	xiangCe();
            	dialog.dismiss();
            	break;
        }
    }
    
    /**
     * 选择对话框
     * @param code
     */
	private void dialog(){
	//	this.mFile = file;
		LayoutInflater inflater = ((Activity) this).getLayoutInflater();
	//	LayoutInflater inflater = LayoutInflater.from(ChatActivity.this);
		View layout = inflater.inflate(R.layout.paizhao_xiangce,null);
		LinearLayout dailog_ui_ll = (LinearLayout) layout.findViewById(R.id.paizhao_ll);
		paizhao_bt = (Button) layout.findViewById(R.id.paizhao_bt);
		xiangce_bt = (Button) layout.findViewById(R.id.xiangce_bt);
		cancle_bt = (Button) layout.findViewById(R.id.cancle_bt);
		dialog = new AlertDialog.Builder(this).setCancelable(false)
		.setView(dailog_ui_ll).create();
		paizhao_bt.setOnClickListener(this);
		
		xiangce_bt.setOnClickListener(this);
		cancle_bt.setOnClickListener(this);
		Window window = dialog.getWindow();
		window.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置   
		window.setWindowAnimations(R.style.mystyle);  //添加动画   
		dialog.show();  
	}
	

	/**
	 * 拍照
	 * @param code
	 */
	private void paiZhao(){
		Intent camaraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		imageName = TimeUtils.getFileName();
		mPhotoOnSDCardUri =  Uri.fromFile(imageFile);
		camaraIntent.putExtra(MediaStore.EXTRA_OUTPUT,mPhotoOnSDCardUri);
		this.startActivityForResult(camaraIntent, 10);
	}
	
	 /**
		 * 从相册中获取选择
		 * @param code
		 */
		private void xiangCe(){
			Intent intent = new Intent(Intent.ACTION_PICK, null);
			intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
			startActivityForResult(intent,15);
		}
	
	/**
	 * 从相册选择图片
	 * @param data
	 * @return
	 */
	private String startImageAction(Intent data) {
		String picPath = null;
		if(data == null)
		{
			Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
			return "";
		}
		Uri photoUri = data.getData();
		if(photoUri == null )
		{
			Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
			return "";
		}
		String[] pojo = {MediaStore.Images.Media.DATA};
		cursor = managedQuery(photoUri, pojo, null, null, null);
		if(cursor != null )
		{
		int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
		cursor.moveToFirst();
		picPath = cursor.getString(columnIndex);
		//cursor.close();
		}
		if(picPath != null && ( picPath.endsWith(".png") || picPath.endsWith(".PNG") ||picPath.endsWith(".jpg") ||picPath.endsWith(".JPG") ))
		{
		
			return picPath;
		}else{
			Toast.makeText(this, "选择图片文件不正确", Toast.LENGTH_LONG).show();
			return "";
		}
	 }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK){
			switch(requestCode){
			case 10:
				if(imageFile==null){
					Toast.makeText(this, "拍照失败，请重新拍照", 0).show();
					return;
				}
				String imagUrl = imageFile.toString();
				sendFile(imagUrl);
				pd.show();
				break;
			case 15:
				if (data == null) {
					return;
				}
		        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
		          Toast.makeText(this, "SD不可用",1).show();
		          return;
		        }
		        String menFile = startImageAction(data);
		        sendFile(menFile);
		        pd.show();
				break;
			}
			
		}
	}
	
	private void sendFile(String fileUrl){
		 //上传文件
	    File file = new File(fileUrl);
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
	                	 UploadDATA data = getGson().fromJson(s, UploadDATA.class);
	                	 if(data!=null){
	                		 addXiangce(data.getUrl());
	                	 }else{
	                		 handler.sendEmptyMessage(0x01);
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
	
	
	 private void addXiangce(String pic){
	        String uri = String.format(InternetURL.ADD_PHONO + "?uid=%s&op=add&pic=%s&album_id=%s", HomeBabyApplication.uid,pic,photos.getId());
	        StringRequest request = new StringRequest(Request.Method.GET,
	                uri,
	                new Response.Listener<String>() {
	                    @Override
	                    public void onResponse(String s) {
	                        Gson gson = new Gson();
	                        try {
	                        	JSONObject json = new JSONObject(s);
		                		String jsonStr  = json.getString("data");
		                		Pictures data = gson.fromJson(jsonStr, Pictures.class);
			                    if(data!=null){
			                    	list.add(data);
			                        handler.sendEmptyMessage(0x02);
			                    }else{
			                    	handler.sendEmptyMessage(0x01);
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
}
