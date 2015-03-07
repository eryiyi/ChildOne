package com.xiaogang.ChildOne.entity;

import java.io.Serializable;

/**
 * author: ${zhanghailong}
 * Date: 2014/11/16
 * Time: 23:36
 * 类的功能、说明写在此处.
 */
public class Jiazhang implements Serializable {
    private String relation_id;
    private String name;
    private String relation;
    private String cover;
    private String mobile;
    private String call_name;


    public String getRelation_id() {
        return relation_id;
    }

    public void setRelation_id(String relation_id) {
        this.relation_id = relation_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCall_name() {
        return call_name;
    }

    public void setCall_name(String call_name) {
        this.call_name = call_name;
    }
}

