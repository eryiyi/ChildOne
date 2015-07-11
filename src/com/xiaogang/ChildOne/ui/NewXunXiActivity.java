package com.xiaogang.ChildOne.ui;

import com.xiaogang.ChildOne.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

public class NewXunXiActivity extends BaseActivity implements OnClickListener{
	private ImageView title_back;
	private WebView xunxi_wv;
	private String url;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_xunxi);
		url = this.getIntent().getStringExtra("url");
		initView();
	}

	private void initView(){
		title_back = (ImageView) this.findViewById(R.id.title_back);
		xunxi_wv = (WebView) this.findViewById(R.id.xunxi_wv);
		title_back.setOnClickListener(this);
		xunxi_wv.getSettings().setJavaScriptEnabled(true);
		xunxi_wv.loadUrl(url);
        xunxi_wv.setWebViewClient(new WebViewClient(){
			@Override
			public void onPageFinished(WebView view, String url) {
				
				view.setVisibility(view.VISIBLE);
				if(view != null){
					
				}
				super.onPageFinished(view, url);
			}
		});
	}

	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
		case R.id.title_back:
			this.finish();
			break;
		}
		
	}
}
