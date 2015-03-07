package com.xiaogang.ChildOne.data;


import com.xiaogang.ChildOne.entity.Photos;

import java.util.List;

public class PhotosDATA {
    private int code;
    private String msg;
    private List<Photos> data;

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

    public List<Photos> getData() {
        return data;
    }

    public void setData(List<Photos> data) {
        this.data = data;
    }
}
