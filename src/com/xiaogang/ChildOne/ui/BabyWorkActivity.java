package com.xiaogang.ChildOne.ui;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

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
import com.xiaogang.ChildOne.adapter.BabyWorkAdapter;
import com.xiaogang.ChildOne.data.BabyWorkDATA;
import com.xiaogang.ChildOne.data.ErrorDATA;
import com.xiaogang.ChildOne.data.UploadDATA;
import com.xiaogang.ChildOne.entity.Account;
import com.xiaogang.ChildOne.entity.BabyWork;
import com.xiaogang.ChildOne.entity.Pictures;
import com.xiaogang.ChildOne.util.FileUtils;
import com.xiaogang.ChildOne.util.InternetURL;
import com.xiaogang.ChildOne.util.TimeUtils;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.UserDictionary.Words;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class BabyWorkActivity extends BaseActivity implements OnClickListener, OnItemClickListener{
	private ImageView title_back;
	private GridView baby_work_gv;
	private Account account;
	private static boolean IS_REFRESH = true;
    private List<BabyWork> list = new ArrayList<BabyWork>();
    private RequestQueue mRequestQueue;
    private BabyWorkAdapter babyWorkAdapter;
    private ImageLoadingListener animateFirstListener;
    private ImageLoader  imageLoader = ImageLoader.getInstance();//图片加载类;
    private ImageView cancle_img;
    private Dialog friendDialog;
    
    private Dialog dialog;
    private EditText et_phono_name;
    private Button cancle_bt,create_bt;
    private TextView send_baby_work_tv;
    
    private Button paizhao_bt = null;
   	private Button xiangce_bt = null;
   	private Button dialogCancle;
   	private AlertDialog aldialog = null;
   	
   	private String imageName = null;
   	private Uri mPhotoOnSDCardUri = null;
   	public  File imageFile = null;
   	private Cursor cursor = null;
	private ProgressDialog pd;
	private String titleStr;
   	
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
				babyWorkAdapter.notifyDataSetChanged();
				break;
			}
		}
    	
    };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baby_work_ui);
		account = getGson().fromJson(sp.getString(Constants.ACCOUNT_KEY, ""), Account.class);
		animateFirstListener = new AnimateFirstDisplayListener();
		mRequestQueue = Volley.newRequestQueue(this);
		 pd = new ProgressDialog(this);
	     pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		initView();
		getData();
		babyWorkAdapter = new BabyWorkAdapter(this,list);
		baby_work_gv.setAdapter(babyWorkAdapter);
	}
	
	
	private void initView(){
		title_back = (ImageView) this.findViewById(R.id.title_back);
		baby_work_gv = (GridView) this.findViewById(R.id.baby_work_gv);
		send_baby_work_tv = (TextView) this.findViewById(R.id.send_baby_work_tv);
		if(HomeBabyApplication.is_student.equals("1")){
			send_baby_work_tv.setVisibility(View.GONE);
		}else if(HomeBabyApplication.is_student.equals("0")){
			send_baby_work_tv.setVisibility(View.VISIBLE);
		}
		baby_work_gv.setHorizontalSpacing(3);
		baby_work_gv.setVerticalSpacing(5);
		title_back.setOnClickListener(this);
		baby_work_gv.setOnItemClickListener(this);
		send_baby_work_tv.setOnClickListener(this);
	}
	 private void getData(){
	        String uri = String.format(InternetURL.BABY_WORK_URL + "?uid=%s&pageIndex=%d&pageSize=%d", account.getUid(),1,20);
	        StringRequest request = new StringRequest(Request.Method.GET,
	                uri,
	                new Response.Listener<String>() {
	                    @Override
	                    public void onResponse(String s) {
	                            Gson gson = new Gson();
	                            //{"code":"200","msg":"sucess","data":[{"class_name":"\u7231\u5b9d\u5b9d\u73ed","title":"\u7231\u5b9d\u5b9d","works_pic":"http:\/\/yey4.xqb668.com\/\/Uploads\/14290187624377.jpg"},
	                            //{"class_name":"\u7231\u5b9d\u5b9d\u73ed","title":"Ghjkkl","works_pic":"http:\/\/yey4.xqb668.com\/\/Uploads\/14290180686478.png"},
	                            //{"class_name":"\u7231\u5b9d\u5b9d\u73ed","title":"\u6211\u770b\u770b","works_pic":"http:\/\/yey4.xqb668.com\/null"},
	                            //{"class_name":"\u7231\u5b9d\u5b9d\u73ed","title":"\u5e55\u5e03","works_pic":"http:\/\/yey4.xqb668.com\/null"},
	                            //{"class_name":"\u7231\u5b9d\u5b9d\u73ed","title":"???","works_pic":"http:\/\/yey4.xqb668.com\/\/Uploads\/14290055265657.jpg"},
	                            //{"class_name":"\u7231\u5b9d\u5b9d\u73ed","title":"???","works_pic":"http:\/\/yey4.xqb668.com\/\/Uploads\/14290048333247.jpg"},
	                            //{"class_name":"\u7231\u5b9d\u5b9d\u73ed","title":"aaaaa","works_pic":"http:\/\/yey4.xqb668.com\/\/Uploads\/2014-09-15\/5417019c4bb9d.jpg"},
	                            //{"class_name":"\u7231\u5b9d\u5b9d\u73ed","title":"aaaaa","works_pic":"http:\/\/yey4.xqb668.com\/\/Uploads\/2014-09-15\/5417019c4bb9d.jpg"},
	                            //{"class_name":"\u7231\u5b9d\u5b9d\u73ed","title":"Happy","works_pic":"http:\/\/yey4.xqb668.com\/\/Uploads\/14281084822035.png"},
	                            //{"class_name":"\u7231\u5b9d\u5b9d\u73ed","title":"\u7231\u4e00\u73ed \u5927\u5934\u8d34","works_pic":"http:\/\/yey4.xqb668.com\/\/Uploads\/14280309669645.png"},
	                            //{"class_name":"\u7231\u5b9d\u5b9d\u73ed","title":"\u5b63\u8282\u7247","works_pic":"http:\/\/yey4.xqb668.com\/\/Uploads\/14280307147461.png"},
	                            //{"class_name":"\u7231\u5b9d\u5b9d\u73ed","title":"Rtttfggg","works_pic":"http:\/\/yey4.xqb668.com\/\/Uploads\/14280304634852.png"},
	                            //{"class_name":" ","title":"\u7f8e\u5973\u5c0f\u6770","works_pic":"http:\/\/yey4.xqb668.com\/\/Uploads\/2014-09-15\/5417019a13847.jpg"}]}
	                            
	                            try {
	                                BabyWorkDATA data = gson.fromJson(s, BabyWorkDATA.class);
	                                if (IS_REFRESH){
	                                    list.clear();
	                                }
	                                list.addAll(data.getData());
	                                babyWorkAdapter.notifyDataSetChanged();
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
	public void onClick(View arg0) {
	  switch(arg0.getId()){
	  case R.id.title_back:
		   this.finish();
		   break;
	  case R.id.cancle_img:
		  friendDialog.dismiss();
		  break;
	  case R.id.send_baby_work_tv:
		  createPhonoName();
		  break;
	  case R.id.create_bt:
		 
		  titleStr = et_phono_name.getText().toString().trim();
		  if(titleStr.equals("")){
			  Toast.makeText(mContext,"请输入宝宝作品名称或者标题",0).show();
			  return;
		  }
		  
		  try {
			  titleStr = URLEncoder.encode(titleStr, "UTF-8");
      	} catch (UnsupportedEncodingException e) {
      		// TODO Auto-generated catch block
      		e.printStackTrace();
      	}
		  dialog.dismiss();
		  phoneDialog();
		  break;
	  case R.id.cancle_bt:
		  aldialog.dismiss();
		  break;
	  case R.id.paizhao_bt:
		  imageName = TimeUtils.getFileName();
		  imageFile = new File(FileUtils.createFile(),imageName);
		  paiZhao();
		  aldialog.dismiss();
		  break;
	  case R.id.xiangce_bt:
		  xiangCe();
		  aldialog.dismiss();
		  break;
	  case R.id.send_cancle_bt:
		  dialog.dismiss();
		  break;
	  }
		
	}
	
	 /**
     * 选择对话框
     * @param code
     */
	private void phoneDialog(){
	//	this.mFile = file;
		//LayoutInflater inflater = ((Activity) this).getLayoutInflater();
		LayoutInflater inflater = LayoutInflater.from(BabyWorkActivity.this);
		View layout = inflater.inflate(R.layout.paizhao_xiangce,null);
		LinearLayout dailog_ui_ll = (LinearLayout) layout.findViewById(R.id.paizhao_ll);
		paizhao_bt = (Button) layout.findViewById(R.id.paizhao_bt);
		xiangce_bt = (Button) layout.findViewById(R.id.xiangce_bt);
		dialogCancle = (Button) layout.findViewById(R.id.cancle_bt);
		aldialog = new AlertDialog.Builder(this).setCancelable(false)
		.setView(dailog_ui_ll).create();
		paizhao_bt.setOnClickListener(this);
		
		xiangce_bt.setOnClickListener(this);
		dialogCancle.setOnClickListener(this);
		Window window = dialog.getWindow();
		window.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置   
		window.setWindowAnimations(R.style.mystyle);  //添加动画   
		aldialog.show();  
	}
	

	/**
	 * 拍照
	 *
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


	public void friendDialog(int position){
			friendDialog = new Dialog(this,R.style.dialog);
			View view = View.inflate(this, R.layout.show_baby_work, null);
			ImageView work_iv = (ImageView) view.findViewById(R.id.work_iv);
			cancle_img = (ImageView) view.findViewById(R.id.cancle_img);
			cancle_img.setOnClickListener(this);
			try{
				
				imageLoader.displayImage(list.get(position).getWorks_pic(), work_iv, HomeBabyApplication.tpOptions, animateFirstListener);
		 	}catch (Exception e){
		 		Log.d("没有网络图片", e.getMessage());
		 	}
			friendDialog.setContentView(view);
			friendDialog.show();
		}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		friendDialog(arg2);
	}
	
	private void createPhonoName(){
		dialog = new Dialog(this, R.style.myDialog);
		dialog.setCancelable(true);
		View view = View.inflate(this, R.layout.send_baby_work_dialog,null);
		et_phono_name = (EditText) view.findViewById(R.id.et_phono_name);
		cancle_bt = (Button) view.findViewById(R.id.send_cancle_bt);
		create_bt = (Button) view.findViewById(R.id.create_bt);
		create_bt.setOnClickListener(this);
		cancle_bt.setOnClickListener(this);
		dialog.setContentView(view);
		dialog.show();
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
	                		 addXiangce(data.getUrl(),titleStr);
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
	
	
	 private void addXiangce(String pic,String title){
	        String uri = String.format(InternetURL.ADD_BABY_WORK + "?uid=%s&title=%s&pic=%s", HomeBabyApplication.uid,title,pic);
	        StringRequest request = new StringRequest(Request.Method.GET,
	                uri,
	                new Response.Listener<String>() {
	                    @Override
	                    public void onResponse(String s) {
	                        Gson gson = new Gson();
	                        try {
	                        	JSONObject json = new JSONObject(s);
		                		String jsonStr  = json.getString("data");
		                		BabyWork data = gson.fromJson(jsonStr, BabyWork.class);
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
