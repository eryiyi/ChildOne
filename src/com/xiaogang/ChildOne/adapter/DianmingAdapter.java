package com.xiaogang.ChildOne.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaogang.ChildOne.HomeBabyApplication;
import com.xiaogang.ChildOne.R;
import com.xiaogang.ChildOne.entity.Child;
import com.xiaogang.ChildOne.util.StringUtil;
import com.xiaogang.ChildOne.util.TimeUtils;

import java.util.List;

/**
 * Created by liuzwei on 2014/11/20.
 */
public class DianmingAdapter extends BaseAdapter {
    private List<Child> list;
    private Context context;
    private ViewHolder holder;
    private String type;//0 签到，1 签退

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private OnClickContentItemListener onClickContentItemListener;
    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }

    public DianmingAdapter(List<Child> list, Context context, String type){
        this.list = list;
        this.context = context;
        this.type = type;
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
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.dianming_item, null);
            holder.photo = (ImageView) convertView.findViewById(R.id.dianming_photo);
            holder.name = (TextView) convertView.findViewById(R.id.dianming_name);
            holder.time = (TextView) convertView.findViewById(R.id.dianming_time);
            holder.dianming = (Button) convertView.findViewById(R.id.dianming_btn);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        Child child = list.get(position);
        imageLoader.displayImage(child.getCover(), holder.photo, HomeBabyApplication.txOptions, animateFirstListener);
        holder.name.setText(child.getName());
        if (!StringUtil.isNullOrEmpty(child.getDateline())) {
            holder.time.setText(TimeUtils.zhuanhuanTime(Long.parseLong(child.getDateline())));
        }

        if (type.equals("0")) {
            if (child.isIs_come()) {
                holder.dianming.setText("已签到");
                holder.dianming.setEnabled(false);
            } else {
                holder.dianming.setText("签到");
            }
        }else {
            if (!child.isIs_come()) {
                holder.dianming.setText("已签退");
                holder.dianming.setEnabled(false);
            } else {
                holder.dianming.setText("签退");
            }
        }
        holder.dianming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickContentItemListener.onClickContentItem(position, 1, type);
            }
        });

        return convertView;
    }

    class ViewHolder{
        ImageView photo;
        TextView name;
        TextView time;
        Button dianming;
    }
}
