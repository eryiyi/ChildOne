package com.xiaogang.ChildOne.entity;

/**
 * Created by liuzwei on 2014/11/21.
 */
public class UserData {
    private User from;
    private User to;

    public UserData(User from, User to){
        this.from = from;
        this.to = to;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public User getTo() {
        return to;
    }

    public void setTo(User to) {
        this.to = to;
    }
}
