package com.xiaogang.ChildOne.util;

import java.util.HashMap;
import java.util.Map;

/**
 * author: liuzwei
 * Date: 2014/8/11
 * Time: 14:12
 * 静态数据类，用来存储整个程序运行中需要到的参数对象
 */
public class ShellContext {
    public static final String ACCOUNT = "account";

    private static Map<String, Object> SHELL_CONTEXT;

    static {
        SHELL_CONTEXT = new HashMap<String, Object>();
    }

    public static Object getAttribute(String key){
        return SHELL_CONTEXT.get(key);
    }

    public static void setAttribute(String key, Object value){
        SHELL_CONTEXT.put(key, value);
    }

    public static void clear(){
        SHELL_CONTEXT.clear();
    }
}
