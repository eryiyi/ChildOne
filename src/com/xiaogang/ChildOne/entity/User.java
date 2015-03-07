package com.xiaogang.ChildOne.entity;

/**
 * Created by liuzwei on 2014/11/21.
 */
public class User {
    private String name;
    private String cover;

    public User(){

    }
    public User(String name, String cover){
        this.name = name;
        this.cover = cover;
    }

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
}
