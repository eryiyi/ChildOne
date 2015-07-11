package com.xiaogang.ChildOne.util;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;

public class FileUtils {

	/**
	 * 保存图片到指定的目录
	 * @param bit
	 * @param fileName 文件名
	 * @return
	 */
	public static String saveBitToSD(Bitmap bit, String fileName) {
		if (bit == null || bit.isRecycled()) return "";
		
		File file = new File(Environment.getExternalStorageDirectory(), "/ebaebo/photoCache");
		File dirFile = new File(file.getAbsolutePath());
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		File pathFile = new File(dirFile, fileName);
		if (pathFile.exists()) {
			return pathFile.getAbsolutePath();
		} else {
			ImageUtils.Bitmap2File(bit, pathFile.getAbsolutePath());
			return pathFile.getAbsolutePath();
		}
	}
	
	public static File createFile(){
		File file = null;
		String state = Environment.getExternalStorageState(); 
		if (state.equals(Environment.MEDIA_MOUNTED)) { 
		    String saveDir = Environment.getExternalStorageDirectory() 
		            + "/ebaebo/photoCache"; 
		    file = new File(saveDir); 
		    if (!file.exists()) { 
		        file.mkdir(); 
		    } 
		}else{
			 String saveDir ="/storage/emmc/ebaebo/photoCache";
			 file = new File(saveDir); 
		}
		return file;
	}
}
