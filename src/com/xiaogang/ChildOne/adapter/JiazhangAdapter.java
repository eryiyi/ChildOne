package com.xiaogang.ChildOne.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaogang.ChildOne.HomeBabyApplication;
import com.xiaogang.ChildOne.R;
import com.xiaogang.ChildOne.entity.Jiazhang;

import java.util.List;

/**
 * author: ${zhanghailong}
 * Date: 2014/11/13
 * Time: 21:33
 * 类的功能、说明写在此处.
 */
public class JiazhangAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<Jiazhang> list;
    private Context context;

    public JiazhangAdapter(List<Jiazhang> list, Context context) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.jiazhang_item,null);
            holder.pic = (ImageView)convertView.findViewById(R.id.pic);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.guanxi = (TextView) convertView.findViewById(R.id.guanxi);
            holder.mobile = (TextView) convertView.findViewById(R.id.mobile);

            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        final Jiazhang cell = list.get(position);

        holder.name.setText(cell.getName()) ;
        holder.guanxi.setText(cell.getRelation());
        holder.mobile.setText(cell.getMobile());
        try {
            imageLoader.displayImage(cell.getCover(), holder.pic, HomeBabyApplication.options, animateFirstListener);
        }catch (Exception e){
            Log.d("没有网络图片", e.getMessage());
        }
        return convertView;
    }

    class ViewHolder{
        ImageView pic;
        TextView name;
        TextView guanxi;
        TextView mobile;

    }
}
