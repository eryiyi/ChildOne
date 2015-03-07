package com.xiaogang.ChildOne.entity;

/**
 * author: ${zhanghailong}
 * Date: 2014/12/6
 * Time: 8:49
 * 类的功能、说明写在此处.
 */
public class Trace {
    private String id;
    private String line_id;
    private String lat;
    private String lng;
    private String dateline;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLine_id() {
        return line_id;
    }

    public void setLine_id(String line_id) {
        this.line_id = line_id;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }
}
