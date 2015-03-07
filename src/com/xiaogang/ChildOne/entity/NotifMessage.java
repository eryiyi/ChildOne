package com.xiaogang.ChildOne.entity;

import java.io.Serializable;

/**
 * Created by liuzwei on 2014/11/21.
 */
public class NotifMessage implements Serializable {
    private Integer id;
    private String title;
    private String content;
    private String custom;
    private String time;

    public NotifMessage() {
    }

    public NotifMessage(String title, String content, String custom, String time) {
        this.title = title;
        this.content = content;
        this.custom = custom;
        this.time = time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCustom() {
        return custom;
    }

    public void setCustom(String custom) {
        this.custom = custom;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
