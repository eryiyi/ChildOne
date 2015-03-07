package com.xiaogang.ChildOne.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaogang.ChildOne.R;
import com.xiaogang.ChildOne.adapter.AnimateFirstDisplayListener;
import com.xiaogang.ChildOne.entity.Account;
import com.xiaogang.ChildOne.util.RoundImagePhoto;

/**
 * Created by liuzwei on 2014/11/18.
 */
public class SelectIdentityActivity extends BaseActivity implements View.OnClickListener{
    private LinearLayout father;
    private LinearLayout mother;

    private ImageView fatherPhoto;
    private ImageView motherPhoto;
    private TextView fatherName;
    private TextView motherName;

    private RoundImagePhoto roundImagePhoto;
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_identity_layout);
        initView();
        Account account = (Account) getIntent().getSerializableExtra(Constants.ACCOUNT_KEY);
        roundImagePhoto = new RoundImagePhoto(this);
        if (account != null){
            roundImagePhoto.readBitmapViaVolley(account.getF_cover(), fatherPhoto);
            roundImagePhoto.readBitmapViaVolley(account.getM_cover(), motherPhoto);
//            imageLoader.displayImage(account.getF_cover(), fatherPhoto, EbaeboApplication.txOptions, animateFirstListener);
//            imageLoader.displayImage(account.getM_cover(), motherPhoto, EbaeboApplication.txOptions, animateFirstListener);
            fatherName.setText(account.getF_name());
            motherName.setText(account.getM_name());
        }


    }

    private void initView(){
        father = (LinearLayout) findViewById(R.id.father_linear);
        mother = (LinearLayout) findViewById(R.id.mother_linear);
        fatherPhoto = (ImageView) findViewById(R.id.identity_father);
        fatherName = (TextView) findViewById(R.id.identity_father_name);
        motherPhoto = (ImageView) findViewById(R.id.identity_mother);
        motherName = (TextView) findViewById(R.id.identity_mother_name);
        father.setOnClickListener(this);
        mother.setOnClickListener(this);
        
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.father_linear:
                save(Constants.IDENTITY, "0");
                break;
            case R.id.mother_linear:
                save(Constants.IDENTITY, "1");
                break;
        }
        Intent intent = new Intent(SelectIdentityActivity.this, CenterActivity.class);
        startActivity(intent);
    }
}
