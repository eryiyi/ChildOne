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
import com.xiaogang.ChildOne.entity.Tongxunlu;

import java.util.List;

/**
 * author: ${zhanghailong}
 * Date: 2014/11/13
 * Time: 21:33
 * 类的功能、说明写在此处.
 */
public class TongxunluAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<Tongxunlu> list;
    private Context context;

    public TongxunluAdapter(List<Tongxunlu> list, Context context) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.tongxunlu_item,null);
            holder.tx = (ImageView)convertView.findViewById(R.id.tx);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.select = (ImageView) convertView.findViewById(R.id.select);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        final Tongxunlu cell = list.get(position);
        String name  = cell.getName();
        if(cell.getName().length() > 15){
            name = cell.getName().substring(0,15);
        }
        holder.name.setText(name);
        try {
            imageLoader.displayImage(cell.getCover(), holder.tx, HomeBabyApplication.options, animateFirstListener);
        }catch (Exception e){
            Log.d("没有网络图片", e.getMessage());
        }
        holder.select.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onClickContentItemListener.onClickContentItem(position, 1, null);
            }
        });
        return convertView;
    }

    class ViewHolder{
        ImageView tx;
        TextView name;
        ImageView select;

    }
}
