package com.xiaogang.ChildOne.entity;

import java.io.Serializable;

/**
 * Created by liuzwei on 2014/11/16.
 */
public class Favours implements Serializable {
    private String name;//会员民称
    private String cover;//头像
    private String time;//时间
    private String uid;//会员id
    private String content;//评论内容

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
