package com.xiaogang.ChildOne.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaogang.ChildOne.HomeBabyApplication;
import com.xiaogang.ChildOne.R;

import java.util.ArrayList;

public class GridImageAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<String> dataList;
	private ViewHolder viewHolder;

	ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	public GridImageAdapter(Context c, ArrayList<String> dataList) {
		this.mContext = c;
		this.dataList = dataList;
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_grid_img, null);
			convertView.setTag(viewHolder);
			viewHolder.imageview = (ImageView) convertView.findViewById(R.id.row_gridview_imageview);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String path = dataList.get(position);
        if (path.contains("camera_default")){
            viewHolder.imageview.setImageResource(R.drawable.addphoto_button_pressed);
        }else {
            imageLoader.displayImage("file://"+path, viewHolder.imageview, HomeBabyApplication.tpOptions);
		}
		return convertView;
	}
	
	static class ViewHolder {
		ImageView imageview;
	}
}
