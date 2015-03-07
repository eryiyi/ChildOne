package com.xiaogang.ChildOne.util;

import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * author: liuzwei
 * Date: 2014/8/20
 * Time: 14:31
 * 类的功能、说明写在此处.
 */
public class DownloadUtil implements Runnable {
    private String url;
    private static int MB = 1024 * 1024;

    public DownloadUtil(String url) {
        this.url = url;
    }

    public static void downloadImage(String fileUrl) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Log.d("TAG", "monted sdcard");
        } else {
            Log.d("TAG", "has no sdcard");
        }
        HttpURLConnection con = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;
        File imageFile = null;
        try {
            URL url = new URL(fileUrl);
            con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(5 * 1000);
            con.setReadTimeout(15 * 1000);
            con.setDoInput(true);
            con.setDoOutput(true);
            bis = new BufferedInputStream(con.getInputStream());
            imageFile = new File(getFilePath(fileUrl));
            fos = new FileOutputStream(imageFile);
            bos = new BufferedOutputStream(fos);
            byte[] b = new byte[1024];
            int length;
            while ((length = bis.read(b)) != -1) {
                bos.write(b, 0, length);
                bos.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.close();
                }
                if (con != null) {
                    con.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (imageFile != null) {
//            Bitmap bitmap = ImageLoader.decodeSampledBitmapFromResource(imageFile.getPath(),
//                    columnWidth);
//            if (bitmap != null) {
//                imageLoader.addBitmapToMemoryCache(imageUrl, bitmap);
//            }
        }
    }

    /**
     * 获取文件的本地存储路径。
     *
     * @param fileUrl 文件的URL地址。
     * @return 文件的本地存储路径。
     */
    public static String getFilePath(String fileUrl) {
        int lastSlashIndex = fileUrl.lastIndexOf("/");
        String imageName = fileUrl.substring(lastSlashIndex + 1);
        String imageDir = Environment.getExternalStorageDirectory().getPath()
                + "/ebaebo/receive";
        File file = new File(imageDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        String imagePath = imageDir + "/"  + imageName;
        return imagePath;
    }

    @Override
    public void run() {
        String filePath = getFilePath(url);
        File file = new File(filePath);
        if (file.exists()){
            return;
        }
        downloadImage(url);
    }

    /**
     * 判断手机是否有SD卡。
     *
     * @return 有SD卡返回true，没有返回false。
     */
    public static boolean hasSDCard() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }


    /** * 计算sdcard上的剩余空间 * @return */
    public static int freeSpaceOnSd() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
                .getPath());
        double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat
                .getBlockSize()) / MB;

        return (int) sdFreeMB;
    }
}
