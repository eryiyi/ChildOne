package com.xiaogang.ChildOne.ui;



import com.huamaitel.api.HMDefines;
import com.huamaitel.api.HMDefines.LoginServerInfo;
import com.huamaitel.api.HMDefines.UserInfo;
import com.xiaogang.ChildOne.HomeBabyApplication;
import com.xiaogang.ChildOne.R;
import com.xiaogang.ChildOne.entity.Account;
import com.xiaogang.ChildOne.util.InternetURL;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class PingTaiLoginActivity extends BaseActivity implements OnClickListener{
	protected static final String TAG = "PingTaiLoginActivity";
	protected static final int EVENT_CONNECT_FAIL = 2;
	protected static final int EVENT_CONNECT_SUCCESS = 1;
	protected static final int EVENT_LOGIN_FAIL = 4;
	protected static final int EVENT_LOGIN_SUCCESS = 3;
	private EditText id_edt_username;
	private EditText id_edt_password;
	private Button id_btn_login;
	private ImageView chengzhang_back;
	private HMDefines.LoginServerInfo info = null;
	
	private ProgressDialog progressDialog;
	
	private  Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			case EVENT_CONNECT_SUCCESS:
				progressDialog.dismiss();
				int result = HomeBabyApplication.hmControl.getDeviceList(HomeBabyApplication.serverId);
				if(result != HMDefines.HMEC_OK){
					sendEmptyMessage(EVENT_CONNECT_FAIL);
					return;
				}
				UserInfo userInfo = HomeBabyApplication.hmControl.getUserInfo(HomeBabyApplication.serverId);
				if(userInfo==null){
					sendEmptyMessage(EVENT_CONNECT_FAIL);
					return;
				}
				if(userInfo.useTransferService!=0){
					result = HomeBabyApplication.hmControl.getTransferInfo(HomeBabyApplication.serverId);
					if (result != HMDefines.HMEC_OK) {
						sendEmptyMessage(EVENT_LOGIN_FAIL);
						return;
					}
				}
				HomeBabyApplication.treeId = HomeBabyApplication.getHmJniInterface().getTree(HomeBabyApplication.serverId);
				sendEmptyMessage(EVENT_LOGIN_SUCCESS);
				break;
			case EVENT_CONNECT_FAIL:
				//提示登录失败原因
				progressDialog.dismiss();
				Toast.makeText(PingTaiLoginActivity.this, msg.obj.toString(), 0).show();
				break;
			case EVENT_LOGIN_SUCCESS:
				
				Intent intent = new Intent(PingTaiLoginActivity.this, DeviceActivity.class);
				startActivity(intent);
				PingTaiLoginActivity.this.finish();
				Log.i(TAG, "login success");
				break;
			case EVENT_LOGIN_FAIL:
				progressDialog.dismiss();
				HomeBabyApplication.getHmJniInterface().disconnectServer(HomeBabyApplication.serverId);
				Toast.makeText(PingTaiLoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
				break;
			}
		}
		
	};
	
	private void saveAccount(String pusername, String ppassword){
        save(Constants.PUSERNAME_KEY, pusername);
        save(Constants.PPASSWORD_KEY, ppassword);
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pingtai_login);
		HomeBabyApplication.getHmJniInterface().init();
		intview();
	}
	
	private void intview(){
		id_edt_username = (EditText) this.findViewById(R.id.id_edt_username);
		id_edt_password = (EditText) this.findViewById(R.id.id_edt_password);
		id_btn_login = (Button) this.findViewById(R.id.id_btn_login);
		chengzhang_back = (ImageView) this.findViewById(R.id.chengzhang_back);
		id_btn_login.setOnClickListener(this);
		chengzhang_back.setOnClickListener(this);
		if (id_edt_username != null) {
			id_edt_username.setText(getGson().fromJson(sp.getString(Constants.PUSERNAME_KEY, ""), String.class));
	    }
	    if (id_edt_password!= null) {
	      	id_edt_password.setText(getGson().fromJson(sp.getString(Constants.PPASSWORD_KEY, ""), String.class));
	    }
	}

	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
		case R.id.id_btn_login:
			final String userName = id_edt_username.getText().toString().trim();
			if(userName.equals("")){
				Toast.makeText(this, "请输入帐号",0).show();
				return;
			}
			final String pwd = id_edt_password.getText().toString().trim();
			if(pwd.equals("")){
				Toast.makeText(this, "请输入密码",0).show();
				return;
			}
			progressDialog = new ProgressDialog(PingTaiLoginActivity.this);
            progressDialog.setMessage("登录中...");
            progressDialog.setCanceledOnTouchOutside(false);
            /*progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                	id_btn_login.setClickable(true);
                }
            });*/
            saveAccount(userName,pwd);
            progressDialog.show();
			
			new Thread(){
				public void run() {
					info = new LoginServerInfo(); 
					info.ip = InternetURL.LOGIN_VIDEO_URL;//平台服务器地址
					info.port = InternetURL.POST;//端口
					info.user = userName; // 用户名
					info.password = pwd; //密码
					info.model = android.os.Build.MODEL; // 手机型号
					info.version = android.os.Build.VERSION.RELEASE; // 手机系统版本号
					String erString = jni_connectServer();
					if (erString != null) {
						Log.e(TAG, "Connect server fail.");
						sendEmptyMessage(EVENT_CONNECT_FAIL, erString);
					} else {
						Log.e(TAG, "Connect server SUCCESS.");
						sendEmptyMessage(EVENT_CONNECT_SUCCESS,null);
					}
				};
			}.start();
			break;
		case R.id.chengzhang_back:
			this.finish();
			break;
		}
		
	}
	
	//链接平台服务器
	public String jni_connectServer() {
		StringBuilder error = new StringBuilder();
		HomeBabyApplication.serverId = HomeBabyApplication.getHmJniInterface().connectServer(info, error);
		if (HomeBabyApplication.serverId == -1) {
			return error.toString();
		}
		return null;
	}
	
	
	private void sendEmptyMessage(int msgId, String error) {
		if (handler == null) {
			return;
		}
		Message msg = new Message();
		msg.what = msgId;
		msg.obj = error;
		handler.sendMessage(msg);
	}

}
