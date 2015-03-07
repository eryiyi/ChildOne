package com.xiaogang.ChildOne.entity;

import java.io.Serializable;

/**
 * Created by liuzwei on 2014/11/21.
 *
 * 交互信息首页返回的消息对象
 */
public class AccountMessage implements Serializable {
    private String uid;
    private String cover;
    private String name;
    private String dept;//职称
    private int pmnum;
    private String lastmessage;//最近一条消息
    private String time;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public int getPmnum() {
        return pmnum;
    }

    public void setPmnum(int pmnum) {
        this.pmnum = pmnum;
    }

    public String getLastmessage() {
        return lastmessage;
    }

    public void setLastmessage(String lastmessage) {
        this.lastmessage = lastmessage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
