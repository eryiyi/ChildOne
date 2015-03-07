package com.xiaogang.ChildOne.data;


import com.xiaogang.ChildOne.entity.AccountMessage;

import java.util.List;

/**
 * Created by liuzwei on 2014/11/21.
 */
public class AccountMessageDATA {
    private int code;

    private String msg;

    private List<AccountMessage> data;

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

    public List<AccountMessage> getData() {
        return data;
    }

    public void setData(List<AccountMessage> data) {
        this.data = data;
    }
}
