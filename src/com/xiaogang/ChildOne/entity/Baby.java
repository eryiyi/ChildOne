package com.xiaogang.ChildOne.entity;

/**
 * Created by liuzwei on 2014/11/18.
 */
public class Baby {
    private String id;//宝宝ID
    private String name;//宝宝姓名

    public Baby(String id, String name){
        this.id = id;
        this.name = name;
    }

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
}
