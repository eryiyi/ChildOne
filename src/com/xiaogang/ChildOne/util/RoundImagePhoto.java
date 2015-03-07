package com.xiaogang.ChildOne.util;

import android.content.Context;
import android.graphics.*;
import android.widget.ImageView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by liuzwei on 2014/11/21.
 */
public class RoundImagePhoto {
    RequestQueue reQueue;
    Context context;

    public RoundImagePhoto(Context context) {
        this.context = context;
    }
// 方式1  获取本地图片做圆头像

    /**
     * 转换图片成圆形
     *
     * @param bitmap 传入Bitmap对象
     * @return
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap) {     //实现圆形头像图层函数
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 6;


            left = 0;
            top = 0;
            right = width;
            bottom = width;


            height = width;


            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 6;


            float clip = (width - height) / 2;


            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;


            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }


        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);


        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);


        paint.setAntiAlias(true);// 设置画笔无锯齿


        canvas.drawARGB(0, 0, 0, 0); // 填充整个Canvas


// 以下有两种方法画圆,drawRounRect和drawCircle
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);// 画圆角矩形，第一个参数为图形显示区域，第二个参数和第三个参数分别是水平圆角半径和垂直圆角半径。
// canvas.drawCircle(roundPx, roundPx, roundPx, paint);


        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));// 设置两张图片相交时的模式,参考http://trylovecatch.iteye.com/blog/1189452
        canvas.drawBitmap(bitmap, src, dst, paint); // 以Mode.SRC_IN模式合并bitmap和已经draw了的Circle


        return output;
    }

    //参数1、是获取图片的url
    //参数2、是你要设置成圆头像的imageview的id

    public void readBitmapViaVolley(String imgUrl, final ImageView imageView) {    //通过volley获取网络图片
        reQueue= Volley.newRequestQueue(context);
        ImageRequest imgRequest = new ImageRequest(imgUrl,
                new Response.Listener<Bitmap>() {
                    public void onResponse(Bitmap bitmap) {
                        Bitmap bmBitmap = RoundImagePhoto.toRoundBitmap(bitmap);
                        imageView.setImageBitmap(bmBitmap);
                    }
                }, 300, 200, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError arg0) {
            }
        });
        reQueue.add(imgRequest);
    }
}
