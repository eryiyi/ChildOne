package com.xiaogang.ChildOne.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * 获得时间的工具类
 */
public class TimeUtils {

	public static String getCurrentTime(String format) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
		String currentTime = sdf.format(date);
		return currentTime;
	}

	public static String getCurrentTime() {
		return getCurrentTime("yyyy-MM-dd  HH:mm:ss");
	}
	
	public static  String getLoginTime(){
		return getCurrentTime("yyyy-MM-dd  HH:mm");
	}
    public static String zhuanhuanTime(long sd){
        Date dat=new Date(sd * 1000);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dat);
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String sb=format.format(gc.getTime());
        return sb;
    }
    
    /**
	 * 文件按时间命名+随机数
	 * @return
	 */
	public static String getFileName(){
		String str = null;
		Date date = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		date = new Date();
		int number = (int)(Math.random()*100);
		str = format.format(date);
		String filaName = str+number+".jpg";
		return filaName;
	}
}
