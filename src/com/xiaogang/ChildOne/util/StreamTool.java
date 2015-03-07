package com.xiaogang.ChildOne.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * author: ${zhanghailong}
 * Date: 2015/1/23
 * Time: 14:02
 * 类的功能、说明写在此处.
 */
public class StreamTool {
    /*
    * 从数据流中获得数据
    */
    public static  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();

    }
}
