package com.xiaogang.ChildOne.entity;

import java.io.Serializable;

/**
 * author: ${zhanghailong}
 * Date: 2014/11/16
 * Time: 23:36
 * 类的功能、说明写在此处.
 */
public class Yuying implements Serializable {
    private String id;
    private String title;
    private String pic;
    private String summary;
    private String content;
    private String publish_uid;
    private String publisher;
    private String school_id;
    private String dateline;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPublish_uid() {
        return publish_uid;
    }

    public void setPublish_uid(String publish_uid) {
        this.publish_uid = publish_uid;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getSchool_id() {
        return school_id;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public Yuying(String id, String title, String pic, String summary, String content, String publish_uid, String publisher, String school_id, String dateline) {
        this.id = id;
        this.title = title;
        this.pic = pic;
        this.summary = summary;
        this.content = content;
        this.publish_uid = publish_uid;
        this.publisher = publisher;
        this.school_id = school_id;
        this.dateline = dateline;
    }
}

