package com.xiaogang.ChildOne.ui;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


import com.baidu.mapapi.map.Text;
import com.huamaitel.api.HMCallback;
import com.huamaitel.api.HMDefines;
import com.huamaitel.api.HMJniInterface;
import com.xiaogang.ChildOne.HomeBabyApplication;
import com.xiaogang.ChildOne.R;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class VideoSupercisionActivity extends BaseActivity implements OnClickListener, OnGestureListener{
    private static final String TAG = "VideoSupercisionActivity";
//	private int screenWidth, screenHeight;//屏幕的宽度和高度
    private ProgressDialog progressDialog; 
    private int mNodeId;
    private int mChannelIndex;
    private int mVideoStream;
    
    private boolean mIsPlaying = false; // Is playing video
    private boolean mIfLogin = false; // If login...
    private TextView video_name;
    private String video_name_str;
    private boolean mIfExit = false; // If exit the activity...
    private ImageView chengzhang_back;
    private GestureDetector gestureScanner;
    private boolean flag = true; 
    
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_supervision);
		gestureScanner = new GestureDetector(this);
		 gestureScanner.setOnDoubleTapListener(new OnDoubleTapListener() {
			
			@Override
			public boolean onSingleTapConfirmed(MotionEvent arg0) {
				return false;
			}
			
			@Override
			public boolean onDoubleTapEvent(MotionEvent arg0) {
				
				return false;
			}
			
			@Override
			public boolean onDoubleTap(MotionEvent arg0) {
				if(flag){
				   setContentView(R.layout.video_supervision_full);
					flag = false;
				}else{
					setContentView(R.layout.video_supervision);
					initView();
					flag = true;
				}
				return true;
			}
		});
		 initView();
		playVideo();
	}
	
	private void  initView(){
		video_name = (TextView) this.findViewById(R.id.video_name);
		chengzhang_back = (ImageView) this.findViewById(R.id.chengzhang_back);
	    mNodeId = this.getIntent().getIntExtra(HomeBabyApplication.NODE_ID, 0);
		mChannelIndex = this.getIntent().getIntExtra(HomeBabyApplication.CHANNEL, 0);
		mVideoStream = this.getIntent().getIntExtra(HomeBabyApplication.VIDEO_STREAM, HMDefines.CodeStream.MAIN_STREAM);
		video_name_str = this.getIntent().getStringExtra("video_name_str");
		video_name.setText(video_name_str);
		chengzhang_back.setOnClickListener(this);
	}
	
	
	
	
	private void playVideo(){
		new Thread(){
			public void run() {
				if (mNodeId == 0) {
						return;
				}
				// Step 1: Login the device.

				HomeBabyApplication.mUserId = HomeBabyApplication.getHmJniInterface().loginEx(mNodeId, HMDefines.ConnectPolicy.CONN_POLICY_DEFAULT);
				if (HomeBabyApplication.mUserId == 0) {
						return;
				}
				HomeBabyApplication.mDeviceInfo = HomeBabyApplication.hmControl.getDeviceInfo(HomeBabyApplication.mUserId);
				HomeBabyApplication.mChannelCapacity = HomeBabyApplication.hmControl.getChannelCapacity(HomeBabyApplication.mUserId);
				if (HomeBabyApplication.mDeviceInfo == null) {
					return;
				}
				// Step 3: Start video
				HMDefines.OpenVideoParam param = new HMDefines.OpenVideoParam();
				param.channel = mChannelIndex;
				param.codeStream = HMDefines.CodeStream.MAIN_STREAM;
				param.videoType = HMDefines.VideoStream.REAL_STREAM;
				HMDefines.OpenVideoRes res = new HMDefines.OpenVideoRes();
				mIfLogin = true;
				HomeBabyApplication.mVideoHandle = HomeBabyApplication.getHmJniInterface().startVideo(HomeBabyApplication.mUserId, param, res);

				if (HomeBabyApplication.mVideoHandle == -1) {
					mIsPlaying = false;
					return;
				}
				/**
				 * TODO：异常处理 startVideo成功后 ，执行 setNetworkCallback 回调函数检测网络数据。 为保证设备信息获取到，可以再执行一次getDeviceInfo操作。 如添加了进度条，在此时进行释放。
				 * 
				 */
				mIsPlaying = true;
			
			};
		}.start();
		
	}
	
	
	 @Override
		public void onBackPressed() {
			new AlertDialog.Builder(this).setTitle("确定要退出视频播放吗？").setIcon(android.R.drawable.ic_dialog_info).setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// Exit the play activity for exiting.
					if (!mIfExit) {
						exitPlayActivity();
					}
					VideoSupercisionActivity.this.finish();
				}
			}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// do nothing……
				}
			}).show();
		}
	 
	 private void exitPlayActivity() {
			// Avoid calling this function many times.
			mIfExit = true;

			// Close some resources.
			new Thread() {
				public void run() {
					releaseResources();
				}

			}.start();

			// Back to the device list activity.
			backtoDeviceList();
		}

		private void backtoDeviceList() {
			VideoSupercisionActivity.this.finish();
		}
		
		private void releaseResources() {
			if (mIsPlaying) {
				int result = HomeBabyApplication.getHmJniInterface().stopVideo(HomeBabyApplication.mVideoHandle);
				if (result != 0) {
					Log.e(TAG, "stopvideo fail.");
				} else {
					Log.i(TAG, "stopvideo success.");
				}
				mIsPlaying = false;
			}



			if (mIfLogin) {
				int result = HomeBabyApplication.getHmJniInterface().logout(HomeBabyApplication.mUserId);
				if (result != 0) {
					Log.e(TAG, "logout fail.");
				} else {
					Log.i(TAG, "logout success.");
				}
				mIfExit = false;
			}

		}




		@Override
		public void onClick(View arg0) {
			switch(arg0.getId()){
			case R.id.chengzhang_back:
				exitPlayActivity();
				VideoSupercisionActivity.this.finish();
				break;
			}
			
		}




		@Override
		public boolean onDown(MotionEvent arg0) {
			// TODO Auto-generated method stub
			return false;
		}




		@Override
		public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
				float arg3) {
			// TODO Auto-generated method stub
			return false;
		}




		@Override
		public void onLongPress(MotionEvent arg0) {
			// TODO Auto-generated method stub
			
		}




		@Override
		public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
				float arg3) {
			// TODO Auto-generated method stub
			return false;
		}




		@Override
		public void onShowPress(MotionEvent arg0) {
			// TODO Auto-generated method stub
			
		}




		@Override
		public boolean onSingleTapUp(MotionEvent arg0) {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			return gestureScanner.onTouchEvent(event);
		}
	
}
	
 class PlayView extends GLSurfaceView{
		private PlayRenderer playRenderer;
		private boolean isFirstIn = true;
		public PlayView(Context context, AttributeSet attrs) {
			super(context);
			// Create an OpenGL ES 2.0 context.
			setEGLContextClientVersion(2);

			// Set the Renderer for drawing on the GLSurfaceView
			playRenderer = new PlayRenderer();
			setRenderer(playRenderer);

			// Set render mode.
			setRenderMode(RENDERMODE_WHEN_DIRTY);

			// Register the OpenGL render callback.
			HomeBabyApplication.getHmJniInterface().setRenderCallback(new HMCallback.RenderCallback() {
				@Override
				public void onRequest() {
					requestRender(); // Force to render if video data comes.
				}
			});
		}
		
		
	 // 这个接口定义了在一个OpenGL的GLSurfaceView中绘制图形所需要的方法。
	  class PlayRenderer implements GLSurfaceView.Renderer {

		  // 设置OpenGL的环境变量，或是初始化OpenGL的图形物体。
			public void onSurfaceChanged(GL10 gl, int w, int h) {
					HomeBabyApplication.getHmJniInterface().gLResize(w, h);
			}
			// 这个方法主要完成绘制图形的操作。
			public void onDrawFrame(GL10 gl) {
				if (isFirstIn) {
					isFirstIn = false;
							return;
				}

				HomeBabyApplication.getHmJniInterface().gLRender();
			}

			// 主要用来对GLSurfaceView容器的变化进行响应。
			@Override
			public void onSurfaceCreated(GL10 gl, EGLConfig config) {
					HomeBabyApplication.getHmJniInterface().gLInit();
			}
	  }

	   @Override
	   public void surfaceDestroyed(SurfaceHolder holder) {
					super.surfaceDestroyed(holder);
					HomeBabyApplication.getHmJniInterface().gLUninit();
		}
	   
	   
	   
	  
}
	
