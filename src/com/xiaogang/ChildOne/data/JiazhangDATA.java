package com.xiaogang.ChildOne.data;


import com.xiaogang.ChildOne.entity.Jiazhang;

import java.util.List;

public class JiazhangDATA {
    private int code;
    private String msg;
    private List<Jiazhang> data;

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

    public List<Jiazhang> getData() {
        return data;
    }

    public void setData(List<Jiazhang> data) {
        this.data = data;
    }
}
