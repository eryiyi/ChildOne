/*
 Copyright (c) 2012 Roman Truba

 Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all copies or substantial
 portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH
 THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.xiaogang.ChildOne.util.imageBrowser.TouchView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.xiaogang.ChildOne.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class UrlTouchImageView extends RelativeLayout {
    protected ProgressBar mProgressBar;
    protected TouchImageView mImageView;

    protected Context mContext;

    public UrlTouchImageView(Context ctx)
    {
        super(ctx);
        mContext = ctx;
        init();

    }
    public UrlTouchImageView(Context ctx, AttributeSet attrs)
    {
        super(ctx, attrs);
        mContext = ctx;
        init();
    }
    public TouchImageView getImageView() { return mImageView; }

    @SuppressWarnings("deprecation")
    protected void init() {
        mImageView = new TouchImageView(mContext);
        LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        mImageView.setLayoutParams(params);
        this.addView(mImageView);
        mImageView.setVisibility(GONE);

        mProgressBar = new ProgressBar(mContext, null, android.R.attr.progressBarStyleLarge);
        params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        params.setMargins(50, 0, 50, 0);
        mProgressBar.setLayoutParams(params);
        mProgressBar.setIndeterminate(false);
        mProgressBar.setMax(100);
        this.addView(mProgressBar);
    }

    public void setUrl(String imageUrl, int width, int height)
    {
        ImageLoadTask imageLoadTask = new ImageLoadTask();
        imageLoadTask.setDisplayWidth(width);
        imageLoadTask.setDisplayHeight(height);
        imageLoadTask.execute(imageUrl);
//        new ImageLoadTask().execute(imageUrl);
    }
    
    public void setScaleType(ScaleType scaleType) {
        mImageView.setScaleType(scaleType);
    }
    
    //No caching load
    public class ImageLoadTask extends AsyncTask<String, Integer, Bitmap>
    {
        private int _displayWidth = 480;
        private int _displayHeight = 800;
        private int _displayPixels = _displayWidth * _displayHeight;
        //获取得到手机分辨率的大小
        public void setDisplayWidth(int width){
            _displayWidth = width;
        }

        public void setDisplayHeight(int height){
            _displayHeight = height;
        }

        public int getDisplayHeight(){
            return _displayHeight;
        }

        public int getDisplayWidth(){
            return _displayWidth;
        }

        public int getDisplayPixels(){
            return _displayPixels;
        }


        @Override
        protected Bitmap doInBackground(String... strings) {
            String url = strings[0];
            Bitmap bm = null;
            try {
//                URL aURL = new URL(url);
//                URLConnection conn = aURL.openConnection();
//                conn.connect();
//                InputStream is = conn.getInputStream();
//                int totalLen = conn.getContentLength();
//                InputStreamWrapper bis = new InputStreamWrapper(is, 8192, totalLen);
//                bis.setProgressListener(new InputStreamProgressListener()
//				{
//					@Override
//					public void onProgress(float progressValue, long bytesLoaded,
//							long bytesTotal)
//					{
//						publishProgress((int)(progressValue * 100));
//					}
//				});
//                BitmapFactory.Options opts = new BitmapFactory.Options();
//                opts.inSampleSize = 2;
//                bm = BitmapFactory.decodeStream(bis,null, opts);
//                bis.close();
//                is.close();
                bm = getBitmap(url, _displayPixels, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bm;
        }
        
        @Override
        protected void onPostExecute(Bitmap bitmap) {
        	if (bitmap == null) 
        	{
        		mImageView.setScaleType(ScaleType.CENTER);
        		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.no_photo);
        		mImageView.setImageBitmap(bitmap);
        	}
        	else 
        	{
        		mImageView.setScaleType(ScaleType.MATRIX);
	            mImageView.setImageBitmap(bitmap);
        	}
            mImageView.setVisibility(VISIBLE);
            mProgressBar.setVisibility(GONE);
        }

		@Override
		protected void onProgressUpdate(Integer... values)
		{
			mProgressBar.setProgress(values[0]);
		}

        public Bitmap getBitmap(String url, int _displayPixels, Boolean isBig) throws IOException {
            Bitmap bitmap = null;
            BitmapFactory.Options options = new BitmapFactory.Options();

            InputStream stream = new URL(url).openStream();
            byte[] bytes = getBytes(stream);
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
            options.inSampleSize = computeSampleSize(options, -1,  _displayWidth * _displayHeight);

            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
            return bitmap;
        }

        /**
         * 数据流转换成byte[] 数组
         * @param is
         * @return
         */
        private byte[] getBytes(InputStream is){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] b = new byte[2048];
            int len = 0;
            try {
                while ((len = is.read(b, 0, 2048)) != -1){
                    baos.write(b, 0, len);
                    baos.flush();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
            byte[] bytes = baos.toByteArray();
            return bytes;
        }

        /****
         *    处理图片bitmap size exceeds VM budget （Out Of Memory 内存溢出）
         */
        private int computeSampleSize(BitmapFactory.Options options,
                                      int minSideLength, int maxNumOfPixels) {
            int initialSize = computeInitialSampleSize(options, minSideLength,
                    maxNumOfPixels);

            int roundedSize;
            if (initialSize <= 8) {
                roundedSize = 1;
                while (roundedSize < initialSize) {
                    roundedSize <<= 1;
                }
            } else {
                roundedSize = (initialSize + 7) / 8 * 8;
            }
            return roundedSize;
        }

        private int computeInitialSampleSize(BitmapFactory.Options options,
                                             int minSideLength, int maxNumOfPixels) {
            double w = options.outWidth;
            double h = options.outHeight;
            int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                    .sqrt(w * h / maxNumOfPixels));
            int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
                    Math.floor(w / minSideLength), Math.floor(h / minSideLength));

            if (upperBound < lowerBound) {
                return lowerBound;
            }

            if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
                return 1;
            } else if (minSideLength == -1) {
                return lowerBound;
            } else {
                return upperBound;
            }
        }
    }
}
