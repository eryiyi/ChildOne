package com.xiaogang.ChildOne.adapter;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaogang.ChildOne.HomeBabyApplication;
import com.xiaogang.ChildOne.R;
import com.xiaogang.ChildOne.entity.Pictures;
import com.xiaogang.ChildOne.ui.Constants;
import com.xiaogang.ChildOne.ui.GalleryUrlActivity;

import java.util.List;

/**
 * author: ${zhanghailong}
 * Date: 2014/11/13
 * Time: 21:33
 * 类的功能、说明写在此处.
 */
public class PictureAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<Pictures> list;
    private Context context;

    public PictureAdapter(List<Pictures> list, Context context) {
        this.list = list;
        this.context = context;
    }

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

    private OnClickContentItemListener onClickContentItemListener;
    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.pic_item,null);
            holder.picture = (ImageView)convertView.findViewById(R.id.picture);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        final Pictures cell = list.get(position);
        final String[] pics = new String[list.size()];
        for (int i=0; i< list.size(); i++){
            pics[i] = list.get(i).getPic();
        }
        try {
            imageLoader.displayImage(cell.getPic(), holder.picture, HomeBabyApplication.options, animateFirstListener);
            holder.picture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, GalleryUrlActivity.class);
                    intent.putExtra(Constants.IMAGE_URLS, pics);
                    intent.putExtra(Constants.IMAGE_POSITION, position);
                    context.startActivity(intent);
                }
            });
        }catch (Exception e){
            Log.d("没有网络图片", e.getMessage());
        }

        return convertView;
    }

    class ViewHolder{
        ImageView picture;


    }
}
