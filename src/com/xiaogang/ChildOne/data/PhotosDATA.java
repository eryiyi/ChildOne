package com.xiaogang.ChildOne.data;


import com.xiaogang.ChildOne.entity.Photos;

import java.util.List;

public class PhotosDATA {
    private int code;
    private String msg;
    private List<Photos> data;
    
    //{"code":"200","msg":"sucess","data":{"id":"32","name":"\u6728\u5934","publisher":"\u4e07\u8001\u5e08",
    //"publish_uid":"101","class_id":"1","number":"0","cover":" ","school_id":"1","dateline":"1428995465"}}

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
