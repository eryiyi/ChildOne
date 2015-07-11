package com.xiaogang.ChildOne.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.huamaitel.api.HMDefines.ChannelInfo;
import com.huamaitel.api.HMDefines.NodeTypeInfo;
import com.huamaitel.api.HMDefines;
import com.huamaitel.api.HMJniInterface;
import com.xiaogang.ChildOne.HomeBabyApplication;
import com.xiaogang.ChildOne.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class DeviceActivity extends BaseActivity implements OnClickListener{
	protected static final String TAG = "DeviceActivity";
	private List<Map<String, Object>> mListData;
	private ListView select_devic_lv;
	private ImageView select_back;
	private int mVideoStream = HMDefines.CodeStream.MAIN_STREAM;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.device_list_ui);
		initView();
		int rootId = HomeBabyApplication.hmControl.getRoot(HomeBabyApplication.treeId);
		
		/*int treeId = HomeBabyApplication.treeId;
		int rootId = MainApp.getJni().getRoot(treeId);*/
		
		mListData = new ArrayList<Map<String,Object>>();
		HomeBabyApplication.rootList.clear();
		HomeBabyApplication.rootList.add(rootId);
		
		getChildrenByNodeId(rootId);
		
		SimpleAdapter adapter = new SimpleAdapter(this, mListData, R.layout.device_item, new String[] { "img", "name" }, new int[] { R.id.id_img_deviceIcon, R.id.id_device_name });
		select_devic_lv.setAdapter(adapter);
		select_devic_lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				@SuppressWarnings("unchecked")
				Map<String, Object> map = (Map<String, Object>) arg0.getItemAtPosition(position);
				int nodeType = (Integer) map.get("type");
				Log.d("DeviceActivity:", "nodeType:" + nodeType);
				int nodeId = (Integer) map.get("id");
				Log.d("DeviceActivity:", "nodeId:" + nodeId);

				HomeBabyApplication.curNodeHandle = nodeId;
				String name = (String) map.get("name");
				/**
				 * TODO：异常处理 此处注意判断设备是否在线 请参考SDK文档 添加 isOnline这个接口进行判断
				 * 不在线的设备不能点击，不能进入下一步操作。
				 */
				if (nodeType == NodeTypeInfo.NODE_TYPE_DVS || nodeType == NodeTypeInfo.NODE_TYPE_GROUP ) {
					HomeBabyApplication.rootList.add(nodeId);
					getChildrenByNodeId(nodeId);
					((SimpleAdapter) select_devic_lv.getAdapter()).notifyDataSetChanged();
				}
				if (nodeType == NodeTypeInfo.NODE_TYPE_CHANNEL) {
					/**
					 * 注意：针对DVS设备，在获取NodeId时，需要获取父类的Id（getParentId）,用于登录设备。
					 * 通道信息用于获取DVS下面对应的设备通道号
					 */
					int nodeDvsId=HomeBabyApplication.getHmJniInterface().getParentId(nodeId);
					ChannelInfo info = HomeBabyApplication.getHmJniInterface().getChannelInfo(nodeId); // 获取通道信息
					//如果设备在线
					Log.d(TAG, "info:"+info.index+"+"+info.name);
					
					gotoPlayActivity(nodeDvsId, info.index, mVideoStream,name);
				}
				else if (nodeType == NodeTypeInfo.NODE_TYPE_DEVICE) {
					 gotoPlayActivity(nodeId, 0, mVideoStream,name);
					
				}
			}
		});
	}
	
	private void initView(){
		select_devic_lv = (ListView) this.findViewById(R.id.select_devic_lv);
		select_back = (ImageView) this.findViewById(R.id.select_back);
		select_back.setOnClickListener(this);
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (HomeBabyApplication.treeId != 0) {
			HomeBabyApplication.getHmJniInterface().releaseTree(HomeBabyApplication.treeId);
		}

		if (HomeBabyApplication.serverId != 0) {
			HomeBabyApplication.getHmJniInterface().disconnectServer(HomeBabyApplication.serverId);
		}
	}
	
	private void getChildrenByNodeId(int nodeId) {
		if(nodeId!=0){
			HMJniInterface sdk = HomeBabyApplication.getHmJniInterface();
			mListData.clear();
			int count = sdk.getChildrenCount(nodeId);
			for(int i=0;i<count;i++){
				Map<String,Object> obj = new HashMap<String, Object>();
				int childrenNote = sdk.getChildAt(nodeId,i);
				int nodeType = sdk.getNodeType(childrenNote);
				obj.put("type", nodeType);
				if (nodeType == NodeTypeInfo.NODE_TYPE_GROUP) {
						obj.put("img", R.drawable.folder);
				} else if (nodeType == NodeTypeInfo.NODE_TYPE_DEVICE) {
						obj.put("img", R.drawable.device);
				} else if (nodeType == NodeTypeInfo.NODE_TYPE_DVS) {
						obj.put("img", R.drawable.dvs);
				} else if (nodeType == NodeTypeInfo.NODE_TYPE_CHANNEL) {
						obj.put("img", R.drawable.device);
					
				}
				
				obj.put("id", childrenNote);
				 obj.put("name", sdk.getNodeName(childrenNote));
				 mListData.add(obj);
		    }
	    }
	 }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			backView();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void backView(){
		if (HomeBabyApplication.rootList.size() != 1) {
			int nodeId = HomeBabyApplication.rootList.get(HomeBabyApplication.rootList.size() - 2);
			HomeBabyApplication.rootList.remove(HomeBabyApplication.rootList.size() - 1);

			getChildrenByNodeId(nodeId);

			((SimpleAdapter) select_devic_lv.getAdapter()).notifyDataSetChanged();
		}
	}
	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
		case R.id.select_back:
			this.finish();
			break;
		}
		
	}
	
	private void gotoPlayActivity(int nodeId, int channel, int videoStream,String name) {
		Intent intent = new Intent();
		intent.setClass(DeviceActivity.this, VideoSupercisionActivity.class);
		intent.putExtra(HomeBabyApplication.NODE_ID, nodeId);
		intent.putExtra(HomeBabyApplication.CHANNEL, channel);
		intent.putExtra(HomeBabyApplication.VIDEO_STREAM, videoStream);
		intent.putExtra("video_name_str",name);
		startActivity(intent);
		
	}

}
