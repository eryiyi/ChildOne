package com.xiaogang.ChildOne.data;


import com.xiaogang.ChildOne.entity.Trace;

import java.util.List;

/**
 * Created by liuzwei on 2014/11/17.
 */
public class TraceDATA {
    private int code;
    private String msg;
    private List<Trace> data;

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

    public List<Trace> getData() {
        return data;
    }

    public void setData(List<Trace> data) {
        this.data = data;
    }
}
