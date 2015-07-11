package com.xiaogang.ChildOne.ui;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.xiaogang.ChildOne.R;
import com.xiaogang.ChildOne.data.BooksData;
import com.xiaogang.ChildOne.data.ErrorDATA;
import com.xiaogang.ChildOne.entity.Account;
import com.xiaogang.ChildOne.util.InternetURL;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BookBorrowActivity extends BaseActivity implements OnClickListener{
	
	private ImageView title_back;
	private EditText book_name;
	private EditText book_num;
	private EditText book_id;
	private ImageView scan_id;
	private TextView borrow_btn;
	private Account account;
	private String msgString;
	private ProgressDialog progressDialog;
	private RequestQueue mRequestQueue;
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			borrow_btn.setClickable(true);
             progressDialog.dismiss();
			switch(msg.what){
			case 0x01:
				Toast.makeText(BookBorrowActivity.this, msgString, Toast.LENGTH_LONG).show();
				break;
			case 0x02:
				Toast.makeText(BookBorrowActivity.this, msgString, Toast.LENGTH_LONG).show();
				break;
			}
			
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.book_borrow_ui);
		 mRequestQueue = Volley.newRequestQueue(this);
		account = getGson().fromJson(sp.getString(Constants.ACCOUNT_KEY, ""), Account.class);
		iniView();
	}
	
	
	private void iniView(){
		title_back = (ImageView) this.findViewById(R.id.title_back);
		book_name = (EditText) this.findViewById(R.id.book_name);
		book_num = (EditText) this.findViewById(R.id.book_num);
		book_id = (EditText) this.findViewById(R.id.book_id);
		scan_id = (ImageView) this.findViewById(R.id.scan_id);
		borrow_btn = (TextView) this.findViewById(R.id.borrow_btn);
		title_back.setOnClickListener(this);
		borrow_btn.setOnClickListener(this);
		scan_id.setOnClickListener(this);
	}


	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
		case R.id.title_back:
			this.finish();
			break;
		case R.id.scan_id:
			Intent bt_intent = new Intent(BookBorrowActivity.this,CameraActivity.class);
			startActivityForResult(bt_intent, 0);
			break;
		case R.id.borrow_btn:
			String book_name_str = book_name.getText().toString().trim();
			String book_num_str = book_num.getText().toString().trim();
			String book_id_str = book_id.getText().toString().trim();
			if(book_name_str.equals("")){
				Toast.makeText(this, "书名不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			if(book_num_str.equals("")){
				Toast.makeText(this, "书数量不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			if(book_id_str.equals("")){
				Toast.makeText(this, "书ID不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			 progressDialog = new ProgressDialog(BookBorrowActivity.this);
             progressDialog.setMessage("订阅中...");
             progressDialog.setCanceledOnTouchOutside(false);
             progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                 @Override
                 public void onCancel(DialogInterface dialog) {
                	 borrow_btn.setClickable(true);
                 }
             });
             progressDialog.show();
			getData(book_name_str,book_num_str,book_id_str);
			break;
			
		}
		
	}
	
	
	 private void getData(String book_name,String book_num,String book_id){
	        String uri = String.format(InternetURL.BOOK_BORROW + "?uid=%s&book_name=%s&book_num=%s&book_id=%s", account.getUid(),book_name,book_num,book_id);
	        StringRequest request = new StringRequest(Request.Method.GET,
	                uri,
	                new Response.Listener<String>() {
	                    @Override
	                    public void onResponse(String s) {
	                       try {
	                    	   if(s!=null){
	                    		   JSONObject json = new JSONObject(s);
	                    		   int code = json.getInt("code");
	                    			msgString  = json.getString("msg");
	                    			handler.sendEmptyMessage(0x01);
	                    		   
		                    	}
	                    	  
							
						} catch (JSONException e) {
							msgString = "网络错误";
							handler.sendEmptyMessage(0x02);
							e.printStackTrace();
						}
	                       
	                    }
	                },new Response.ErrorListener() {
	            @Override
	            public void onErrorResponse(VolleyError volleyError) {

	            }
	        });
	        mRequestQueue.add(request);
	    }
	
	/**
	 * 扫描返回
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK&&requestCode==0){
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			book_id.setText(scanResult);
			book_id.setFocusable(false);
		}
	}
	
}
