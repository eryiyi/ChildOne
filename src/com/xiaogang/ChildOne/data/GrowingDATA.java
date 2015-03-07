package com.xiaogang.ChildOne.data;


import com.xiaogang.ChildOne.entity.Growing;

import java.util.List;

/**
 * Created by liuzwei on 2014/11/16.
 */
public class GrowingDATA {
    private int code;
    private String msg;
    private List<Growing> data;

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

    public List<Growing> getData() {
        return data;
    }

    public void setData(List<Growing> data) {
        this.data = data;
    }
}
