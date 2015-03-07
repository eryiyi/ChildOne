package com.xiaogang.ChildOne.adapter;
import android.content.Context;
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
import com.xiaogang.ChildOne.entity.Photos;
import com.xiaogang.ChildOne.util.TimeUtils;

import java.util.List;

/**
 * author: ${zhanghailong}
 * Date: 2014/11/13
 * Time: 21:33
 * 类的功能、说明写在此处.
 */
public class PhotoAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<Photos> list;
    private Context context;

    public PhotoAdapter(List<Photos> list, Context context) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.photo_item,null);
            holder.picture = (ImageView)convertView.findViewById(R.id.giftpic);
            holder.name = (TextView) convertView.findViewById(R.id.gifttitle);
            holder.detailphoto = (ImageView) convertView.findViewById(R.id.detailphoto);
            holder.phosnumber = (TextView) convertView.findViewById(R.id.phosnumber);
            holder.datetime = (TextView) convertView.findViewById(R.id.datetimephot);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        final Photos cell = list.get(position);
        holder.name.setText(cell.getName());
        try {
            imageLoader.displayImage(cell.getCover() , holder.picture, HomeBabyApplication.options, animateFirstListener);
        }catch (Exception e){
//            Log.d("没有网络图片", e.getMessage());
        }
        holder.detailphoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onClickContentItemListener.onClickContentItem(position, 1, null);
            }
        });
        holder.datetime.setText(TimeUtils.zhuanhuanTime(Long.parseLong(cell.getDateline())));
        holder.phosnumber.setText("("+cell.getNumber()+")张");
        return convertView;
    }

    class ViewHolder{
        ImageView picture;
        TextView name;
        ImageView detailphoto;
        TextView phosnumber;//张数
        TextView datetime;//时间

    }
}
