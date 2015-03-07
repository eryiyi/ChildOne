package com.xiaogang.ChildOne.entity;

import java.io.Serializable;

/**
 * author: ${zhanghailong}
 * Date: 2014/11/13
 * Time: 23:52
 * 类的功能、说明写在此处.
 */
public class Pictures implements Serializable {
    private String id;//照片ID
    private String album_id;//所属相册ID
    private String pic;//照片地址
    private String dateline;//时间戳

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public Pictures(String id, String album_id, String pic, String dateline) {
        this.id = id;
        this.album_id = album_id;
        this.pic = pic;
        this.dateline = dateline;
    }
}
