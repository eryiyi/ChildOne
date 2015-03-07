package com.xiaogang.ChildOne.data;


import com.xiaogang.ChildOne.entity.Notice;
import com.xiaogang.ChildOne.entity.Yuying;

import java.util.List;

public class NoticeDATA {
    private int code;
    private String msg;
    private List<Notice> data;

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

    public List<Notice> getData() {
        return data;
    }

    public void setData(List<Notice> data) {
        this.data = data;
    }
}
