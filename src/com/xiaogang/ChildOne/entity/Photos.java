package com.xiaogang.ChildOne.entity;

import java.io.Serializable;
import java.util.List;

/**
 * author: ${zhanghailong}
 * Date: 2014/11/13
 * Time: 23:52
 * 类的功能、说明写在此处.
 */
public class Photos implements Serializable {
    private String id;//相册ID
    private String name;//相册名称
    private String publisher;//相册建立者
    private String publish_uid;//建立者ID
    private String class_id;//班级ID
    private String number;//数量
    private String cover;//封面
    private String school_id;//学校ID
    private String dateline;//时间戳
    

    private List<Pictures> list;//相册中的图片

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Pictures> getList() {
        return list;
    }

    public void setList(List<Pictures> list) {
        this.list = list;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublish_uid() {
        return publish_uid;
    }

    public void setPublish_uid(String publish_uid) {
        this.publish_uid = publish_uid;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
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

    public Photos(String id, String name, String publisher, String publish_uid, String class_id, String number, String cover, String school_id, String dateline, List<Pictures> list) {
        this.id = id;
        this.name = name;
        this.publisher = publisher;
        this.publish_uid = publish_uid;
        this.class_id = class_id;
        this.number = number;
        this.cover = cover;
        this.school_id = school_id;
        this.dateline = dateline;
        this.list = list;
    }
}
