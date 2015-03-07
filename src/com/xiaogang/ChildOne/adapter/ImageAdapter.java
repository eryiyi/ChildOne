package com.xiaogang.ChildOne.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaogang.ChildOne.HomeBabyApplication;
import com.xiaogang.ChildOne.R;

/**
 * author: liuzwei
 * Date: 2014/8/1
 * Time: 11:37
 * 类的功能、说明写在此处.
 */
public class ImageAdapter extends BaseAdapter {
    private String[] imageUrls;
    private Context mContext;
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

    public ImageAdapter(String[] imageUrls, Context mContext) {
        this.imageUrls = imageUrls;
        this.mContext = mContext;

    }

    @Override
    public int getCount() {
        return imageUrls.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ImageView imageView;
        if (convertView == null) {
            imageView = (ImageView) LayoutInflater.from(mContext).inflate(R.layout.item_grid_image, parent, false);
        } else {
            imageView = (ImageView) convertView;
        }
        // 将图片显示任务增加到执行池，图片将被显示到ImageView当轮到此ImageView
        imageLoader.displayImage(imageUrls[position], imageView, HomeBabyApplication.tpOptions);
        return imageView;
    }
}
