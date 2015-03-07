package com.xiaogang.ChildOne.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.xiaogang.ChildOne.R;


public abstract class AbsActivity extends FragmentActivity {
	protected AbsActivity mActThis = null;
	protected ImageLoader loader;
	protected DisplayImageOptions options;
	
//	@SuppressLint("HandlerLeak")
//	public final Handler mHandlerEx = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			onHandleMessage(msg);
//		}
//	};
	
//	protected abstract void onHandleMessage(Message message);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActThis = this;
		
		loader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
	    .imageScaleType(ImageScaleType.EXACTLY)
	    .bitmapConfig(Bitmap.Config.RGB_565)
	    .showImageOnLoading(R.drawable.pic_loading)
	    .cacheInMemory(true)
	    .cacheOnDisc(true)
	    .build();
	}
	
	
//
//	protected void sendMessageWithArg(int what, int arg) {
//		Message msg = mHandlerEx.obtainMessage(what);
//		msg.what = what;
//		msg.arg1 = arg;
//		mHandlerEx.sendMessage(msg);
//	}
//		
//	protected void sendMessageWithObj(int what, Object obj) {
//		Message msg = mHandlerEx.obtainMessage(what);
//		msg.what = what;
//		msg.obj = obj;
//		mHandlerEx.sendMessage(msg);
//	}
	
}
