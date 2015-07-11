package com.xiaogang.ChildOne.util;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import java.io.*;

public class ImageUtils {
	
	public static Bitmap createImage(String filepath){
		Bitmap bitmap = null;
		BitmapFactory.Options bfo = new BitmapFactory.Options();
		bfo.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filepath, bfo);
		bfo.inSampleSize = computeSampleSize(bfo,-1,800*600);
		bfo.inJustDecodeBounds =false;
		try{
			bitmap = BitmapFactory.decodeFile(filepath, bfo);  
		}catch (Exception e) {
		
		}
		return bitmap;
	}
	
	
	public static int computeSampleSize(BitmapFactory.Options bfo,
			int minSideLength,int maxNumOfPixels){
		int roundedSize;
		int initialSize = computeInitialSampleSize(bfo, minSideLength,maxNumOfPixels);
		if(initialSize<=8){
		roundedSize=1;
		while(roundedSize<initialSize){
			roundedSize <<= 1;
		}
		}else{
		 roundedSize = (initialSize + 7) / 8 * 8; 
		}
		
		return roundedSize;
	}
	
   public static Bitmap compressImage(Bitmap bitmap){
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
		int options = 100;
		while(bos.toByteArray().length/1024>15){
			 bos.reset();//重置bos即清空bos 
			 bitmap.compress(Bitmap.CompressFormat.JPEG, options, bos);
			 options -= 10;//每次都减少10 
			 Log.i("options", options+"");
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(bos.toByteArray());//把压缩后的数据bos存放到ByteArrayInputStream中
		Bitmap image = BitmapFactory.decodeStream(isBm, null, null);
	
		return image;
	}
	
	public static boolean saveCompressBitmap(File file){
		FileOutputStream outputStream = null;
		Bitmap bitmap = createImage(file.toString());
		bitmap = compressImage(bitmap);
		//Bitmap bitmap = createImage(file.toString());
		try {
			outputStream = new FileOutputStream(file);
			return bitmap.compress(Bitmap.CompressFormat.JPEG, 100,outputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}finally{
			if(outputStream!=null){
				try {
					outputStream.flush();
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
		return false;
	} 

	private static int computeInitialSampleSize(BitmapFactory.Options options,  
           int minSideLength, int maxNumOfPixels) {  
		double w = options.outWidth;  
		double h = options.outHeight;  
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math  
		                .sqrt(w * h / maxNumOfPixels));  
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(  
		                Math.floor(w / minSideLength), Math.floor(h / minSideLength));  
		if (upperBound < lowerBound) {  
		           // return the larger one when there is no overlapping zone.  
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

	/**
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}
		}
		return inSampleSize;
	}

	public static void Bitmap2File(Bitmap bitmap, String filename) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filename);
			fos.write(baos.toByteArray());
			fos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null)
					fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
    
	public static Bitmap getUriBitmap(Context context, Uri uri, int reqWidth, int reqHeight) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = decodeUriAsBitmap(context, uri, newOpts);
		newOpts.inJustDecodeBounds = false;
		newOpts.inSampleSize = calculateInSampleSize(newOpts, reqWidth, reqHeight);
		bitmap = decodeUriAsBitmap(context, uri, newOpts);
		return bitmap;
	}
	
	/**
	 * 根据路径获得图片并压缩，返回bitmap用于显示
	 * 
	 * @param filePath
	 * @return
	 */
	public static Bitmap getSmallBitmap(String filePath) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		options.inSampleSize = calculateInSampleSize(options, 320, 480);
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
		return bitmap;
	}

	public static Bitmap decodeUriAsBitmap(Context context, Uri uri, BitmapFactory.Options options) {

		Bitmap result = null;

		 if (uri != null) {
			ContentResolver cr = context.getContentResolver();
			InputStream inputStream = null;
			try {
				inputStream = cr.openInputStream(uri);
				result = BitmapFactory.decodeStream(inputStream, null, options);
				inputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return result;
	}
	
	/**
	 * 将Bitmap转换成InputStream
	 * 
	 * @param bm
	 * @return
	 */
	public static InputStream bitmap2InputStream(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		InputStream is = new ByteArrayInputStream(baos.toByteArray());
		return is;
	}
}
