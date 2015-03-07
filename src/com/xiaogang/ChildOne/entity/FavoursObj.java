package com.xiaogang.ChildOne.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liuzwei on 2014/11/17.
 */
public class FavoursObj implements Serializable {
    private int count;
    private List<Favours> list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Favours> getList() {
        return list;
    }

    public void setList(List<Favours> list) {
        this.list = list;
    }
}
