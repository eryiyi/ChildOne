package com.xiaogang.ChildOne.ui;


import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaogang.ChildOne.HomeBabyApplication;
import com.xiaogang.ChildOne.R;
import com.xiaogang.ChildOne.adapter.AnimateFirstDisplayListener;
import com.xiaogang.ChildOne.entity.Books;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class BookDailActivity extends BaseActivity implements OnClickListener{
	private ImageView title_back;
	private ImageView book_pic;
	private TextView book_title;
	private Books books;
	ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.book_dail_ui);
		books = this.getIntent().getParcelableExtra("book");
		initView();
	}

	private void initView(){
		title_back = (ImageView) this.findViewById(R.id.title_back);
		book_title = (TextView) this.findViewById(R.id.book_title);
		book_pic = (ImageView) this.findViewById(R.id.book_pic);
		book_title.setText(books.getBook_name());
		TextView book_id = (TextView) this.findViewById(R.id.book_id);
		TextView borrow_id = (TextView) this.findViewById(R.id.borrow_id);
		TextView book_num = (TextView) this.findViewById(R.id.book_num);
		TextView borrow_price = (TextView) this.findViewById(R.id.borrow_price);
		TextView days = (TextView) this.findViewById(R.id.days);
		TextView borrow_time = (TextView) this.findViewById(R.id.borrow_time);
		
		
		book_id.setText(books.getBook_id());
		borrow_id.setText(books.getBorrow_id());
		book_num.setText(books.getBook_num());
		borrow_price.setText(books.getBorrow_price());
		days.setText(books.getDays());
		borrow_time.setText(books.getBorrow_time());
		title_back.setOnClickListener(this);
		new Thread(){
			public void run() {
				imageLoader.displayImage(books.getBook_pic(),book_pic, HomeBabyApplication.txOptions, animateFirstListener);
			};
		}.start();
		
		
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
