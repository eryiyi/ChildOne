package com.xiaogang.ChildOne.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.xiaogang.ChildOne.R;
import com.xiaogang.ChildOne.ui.DianmingActivity;

/**
 * Created by liuzwei on 2015/1/6.
 */
public class DianmingDialog extends Dialog implements View.OnClickListener{
    private Button come;
    private Button out;
    private Context context;

    public DianmingDialog(Context context , int them) {
        super(context, them);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dianming_dialog);
        initView();
    }

    private void initView(){
        come = (Button) this.findViewById(R.id.come);
        out = (Button) this.findViewById(R.id.out);
        come.setOnClickListener(this);
        out.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String type="";
        switch (v.getId()){
            case R.id.come://入园
                type = "0";
                break;
            case R.id.out://离园
                type="1";
                break;
        }

        Intent dianming = new Intent(context, DianmingActivity.class);
        dianming.putExtra("type", type);
        context.startActivity(dianming);
        DianmingDialog.this.dismiss();
    }
}
