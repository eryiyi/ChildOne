package com.xiaogang.ChildOne.entity;

import java.util.List;

/**
 * Created by liuzwei on 2014/11/20.
 */
public class ClassData {
    private List<Child> child;
    private MyClass sclass;

    public List<Child> getChild() {
        return child;
    }

    public void setChild(List<Child> child) {
        this.child = child;
    }

    public MyClass getSclass() {
        return sclass;
    }

    public void setSclass(MyClass sclass) {
        this.sclass = sclass;
    }
}
