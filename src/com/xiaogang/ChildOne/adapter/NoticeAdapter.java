package com.xiaogang.ChildOne.adapter;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaogang.ChildOne.HomeBabyApplication;
import com.xiaogang.ChildOne.R;
import com.xiaogang.ChildOne.entity.Notice;
import com.xiaogang.ChildOne.entity.Yuying;
import com.xiaogang.ChildOne.util.MxgsaTagHandler;
import com.xiaogang.ChildOne.util.StringUtil;
import com.xiaogang.ChildOne.util.TimeUtils;

import java.util.List;

/**
 * author: ${zhanghailong}
 * Date: 2014/11/13
 * Time: 21:33
 * 类的功能、说明写在此处.
 */
public class NoticeAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<Notice> list;
    private Context context;

    public NoticeAdapter(List<Notice> list, Context context) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.notice_item,null);
            holder.pic = (ImageView)convertView.findViewById(R.id.pic);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.content = (TextView) convertView.findViewById(R.id.content);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.liner = (LinearLayout) convertView.findViewById(R.id.liner);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        final Notice cell = list.get(position);
        String title  = cell.getTitle();
        if(cell.getTitle().length() > 15){
            title = cell.getTitle().substring(0,15);
        }
        holder.title.setText(title) ;
        String cont = cell.getSummary();
        //去除样式
        cont = StringUtil.StripHtml(cont).replace("rn","").replace("&nbsp;", "");
        holder.content.setText(Html.fromHtml(cont, null, new MxgsaTagHandler(context)));
        holder.content.setClickable(true);
        holder.content.setMovementMethod(LinkMovementMethod.getInstance());
        holder.time.setText(TimeUtils.zhuanhuanTime(Long.parseLong(cell.getDateline())));

        try {
            imageLoader.displayImage(cell.getPic(), holder.pic, HomeBabyApplication.options, animateFirstListener);
        }catch (Exception e){
            Log.d("没有网络图片", e.getMessage());
        }

        holder.liner.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onClickContentItemListener.onClickContentItem(position, 1, null);
            }
        });

        return convertView;
    }

    class ViewHolder{
        ImageView pic;
        TextView title;
        TextView content;
        TextView time;
        LinearLayout liner;
    }
}
