package com.xiaogang.ChildOne.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.xiaogang.ChildOne.R;

/**
 * Created by liuzwei on 2015/1/9.
 */
public class LoadingActivity extends BaseActivity implements Runnable {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load);
        AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
        aa.setDuration(3000);
        this.findViewById(R.id.loading_ad).startAnimation(aa);
        // 启动一个线程
        new Thread(this).start();
    }
    public void run() {
        try {
            // 3秒后跳转到登录界面
            Thread.sleep(5000);
            startActivity(new Intent(LoadingActivity.this, LoginActivity.class));
            finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
