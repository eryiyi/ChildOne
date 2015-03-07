package com.xiaogang.ChildOne.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.xiaogang.ChildOne.R;
import com.xiaogang.ChildOne.adapter.PictureAdapter;
import com.xiaogang.ChildOne.entity.Photos;
import com.xiaogang.ChildOne.entity.Pictures;


import java.util.ArrayList;
import java.util.List;

/**
 * author: ${zhanghailong}
 * Date: 2014/11/16
 * Time: 23:06
 * 类的功能、说明写在此处.
 */
public class PictureActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back;
    private TextView title;
    private PictureAdapter adapter;
    private ListView clv;

    List<Pictures> list = new ArrayList<Pictures>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pictures);
        initView();
        setTheme(R.style.index_theme);
        Photos photos = (Photos) getIntent().getExtras().get("photo");

        if(photos!=null){
            list = photos.getList();
            title.setText(photos.getName());
        }
        adapter = new PictureAdapter(list, this);
        clv.setAdapter(adapter);
    }

    private void initView() {
        back = (ImageView) this.findViewById(R.id.picturesback);
        title  = (TextView) this.findViewById(R.id.title);
        clv = (ListView) this.findViewById(R.id.lstv);

        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.picturesback:
                finish();
                break;
        }
    }

}
