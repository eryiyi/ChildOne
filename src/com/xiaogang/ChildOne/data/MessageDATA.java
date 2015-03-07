package com.xiaogang.ChildOne.data;


import com.xiaogang.ChildOne.entity.MessageData;

/**
 * Created by liuzwei on 2014/11/21.
 */
public class MessageDATA{
    private int code;
    private String msg;
    private MessageData data;

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

    public MessageData getData() {
        return data;
    }

    public void setData(MessageData data) {
        this.data = data;
    }
}
