package com.xiaogang.ChildOne.entity;

import java.util.List;

/**
 * Created by liuzwei on 2014/11/21.
 */
public class MessageData {
    private List<Message> list;
    private UserData user;

    public List<Message> getList() {
        return list;
    }

    public void setList(List<Message> list) {
        this.list = list;
    }

    public UserData getUser() {
        return user;
    }

    public void setUser(UserData user) {
        this.user = user;
    }
}
