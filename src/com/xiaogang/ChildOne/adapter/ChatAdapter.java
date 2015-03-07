package com.xiaogang.ChildOne.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaogang.ChildOne.HomeBabyApplication;
import com.xiaogang.ChildOne.R;
import com.xiaogang.ChildOne.entity.Account;
import com.xiaogang.ChildOne.entity.Message;
import com.xiaogang.ChildOne.entity.UserData;
import com.xiaogang.ChildOne.ui.Constants;
import com.xiaogang.ChildOne.util.TimeUtils;
import com.xiaogang.ChildOne.util.face.FaceConversionUtil;

import java.util.List;

/**
 * Created by liuzwei on 2014/11/21.
 */
public class ChatAdapter extends BaseAdapter {
    public static interface IMsgViewType {
        int IMVT_COM_MSG = 0;
        int IMVT_TO_MSG = 1;
    }
    public int getItemViewType(int position) {
        // TODO Auto-generated method stub
        Message message = list.get(position);
        Account account = gson.fromJson(sp.getString(Constants.ACCOUNT_KEY, ""), Account.class);
        if (message.getTo_uids().contains(account.getUid())) {
            return IMsgViewType.IMVT_COM_MSG;
        } else {
            return IMsgViewType.IMVT_TO_MSG;
        }

    }
    private List<Message> list;
    private Context context;
    private ViewHolder viewHolder;
    private SharedPreferences sp;
    private static Gson gson = new Gson();
    private static UserData userData;
    private static boolean isMe = true;
    private MediaPlayer mMediaPlayer = new MediaPlayer();

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public ChatAdapter(List<Message> list, Context context, SharedPreferences sp, UserData userData){
        this.list = list;
        this.context = context;
        this.sp = sp;
        this.userData = userData;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Message message = list.get(position);
        Account account = gson.fromJson(sp.getString(Constants.ACCOUNT_KEY, ""), Account.class);

        if (convertView == null){
            if(message.getUid().equals(account.getUid())){
                convertView = LayoutInflater.from(context).inflate(R.layout.chatting_item_msg_text_right, null);
                isMe = true;
            }else {
                convertView = LayoutInflater.from(context).inflate(R.layout.chatting_item_msg_text_left, null);
                isMe = false;
            }
            viewHolder = new ViewHolder();
            viewHolder.sendTime = (TextView) convertView.findViewById(R.id.tv_sendtime);
            viewHolder.photo = (ImageView) convertView.findViewById(R.id.iv_userhead);
            viewHolder.name = (TextView) convertView.findViewById(R.id.tv_username);
            viewHolder.content = (TextView) convertView.findViewById(R.id.tv_chatcontent);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (!message.getUid().equals(account.getUid())){
            viewHolder.name.setText(userData.getTo().getName());
            imageLoader.displayImage(userData.getTo().getCover(), viewHolder.photo, HomeBabyApplication.txOptions, animateFirstListener);
        }else {
            viewHolder.name.setText(userData.getFrom().getName());
            imageLoader.displayImage(userData.getFrom().getCover(), viewHolder.photo, HomeBabyApplication.txOptions, animateFirstListener);
        }
        try {
            viewHolder.sendTime.setText(TimeUtils.zhuanhuanTime(Long.parseLong(message.getDateline())));
        }catch (Exception e){
            Log.e("ChatAdapter", e.getMessage());

        }
        if (message.getUrl()!= null && message.getUrl().contains(".mp3")) {
            viewHolder.content.setText("");
            viewHolder.content.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.chatto_voice_playing, 0);
        } else {
            SpannableString spannableString = FaceConversionUtil.getInstace().getExpressionString(context,message.getContent());
            viewHolder.content.setText(spannableString);
            viewHolder.content.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
        viewHolder.content.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (message.getUrl() != null && message.getUrl().contains(".mp3")) {
                    playMusic(message.getContent()) ;
                }
            }
        });
//        viewHolder.content.setText(message.getContent());
        return convertView;
    }

    /**
     * @Description
     * @param name
     */
    private void playMusic(String name) {
        try {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(name);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    class ViewHolder{
        TextView sendTime;
        ImageView photo;
        TextView name;
        TextView content;
    }
}
