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
import com.xiaogang.ChildOne.entity.AccountMessage;
import com.xiaogang.ChildOne.ui.Constants;

import java.util.List;

/**
 * Created by liuzwei on 2014/11/21.
 */
public class JiaohuAdapter extends BaseAdapter {
    private List<AccountMessage> list;
    private Context context;
    private ViewHolder viewHolder;

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public JiaohuAdapter(List<AccountMessage> list, Context context){
        this.list = list;
        this.context = context;
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
    public View getView(int position, View convertView, ViewGroup parent) {
       if (convertView == null){
           viewHolder = new ViewHolder();
           convertView = LayoutInflater.from(context).inflate(R.layout.jiaohu_item, null);
           viewHolder.photo = (ImageView) convertView.findViewById(R.id.jiaohu_item_photo);
           viewHolder.dept = (TextView) convertView.findViewById(R.id.jiaohu_item_dept);
           viewHolder.name = (TextView) convertView.findViewById(R.id.jiaohu_item_name);
           viewHolder.num = (TextView) convertView.findViewById(R.id.jiaohu_item_num);
           viewHolder.lastMessage = (TextView) convertView.findViewById(R.id.jiaohu_item_lastmessage);
           convertView.setTag(viewHolder);
       }else {
           viewHolder = (ViewHolder) convertView.getTag();
       }

        AccountMessage message = list.get(position);
        imageLoader.displayImage(String.format(Constants.API_HEAD + "%s", message.getCover()),
                viewHolder.photo, HomeBabyApplication.txOptions,animateFirstListener);
        viewHolder.name.setText(message.getName());
        viewHolder.dept.setText(message.getDept());
        viewHolder.lastMessage.setText(message.getLastmessage());
        if (message.getPmnum()>0){
            viewHolder.num.setVisibility(View.VISIBLE);
            viewHolder.num.setText(" "+message.getPmnum());
        }else {
            viewHolder.num.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder{
        ImageView photo;
        TextView name;
        TextView dept;
        TextView lastMessage;
        TextView num;
    }
}
