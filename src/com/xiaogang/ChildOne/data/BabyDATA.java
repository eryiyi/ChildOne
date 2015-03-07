package com.xiaogang.ChildOne.data;


import com.xiaogang.ChildOne.entity.Baby;

import java.util.List;

/**
 * Created by liuzwei on 2014/11/18.
 */
public class BabyDATA {
    private int code;
    private String msg;
    private List<Baby> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Baby> getData() {
        return data;
    }

    public void setData(List<Baby> data) {
        this.data = data;
    }
}
