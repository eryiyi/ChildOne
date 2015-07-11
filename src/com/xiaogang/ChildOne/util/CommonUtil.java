package com.xiaogang.ChildOne.util;

import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;

import java.io.File;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * yanglq
 */
public class CommonUtil {
    //判断是否为JSOn格式
    public static boolean isJson(String json) {
        if (StringUtil.isNullOrEmpty(json)) {
            return false;
        }
        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonParseException e) {
            return false;
        }
    }

    public static String longToString(long sd){
        Date dat=new Date(sd);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dat);
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return format.format(gc.getTime());
    }

    public static int getMp3TrackLength(File mp3File){
        try {
            MP3File f = (MP3File) AudioFileIO.read(mp3File);
            MP3AudioHeader audioHeader = f.getMP3AudioHeader();
            return audioHeader.getTrackLength();
        } catch (Exception e) {
           return -1;
        }
    }
}
