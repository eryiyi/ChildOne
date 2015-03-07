package com.xiaogang.ChildOne.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liuzwei on 2014/11/15.
 *
 * 成长管理
 */
public class Growing implements Serializable {
    private String id;
    private String uid;
    private String dept;//文字描述
    private String type;//0 文字 1 照片 2 视频
    private String child_name;//宝宝名字
    private String publisher;//发布人
    private String publisher_cover;//发布人的头像
    private String publish_uid;
    private String is_share;
    private String school_id;
    private String pt;
    private long dateline;
    private String user_type;
    private String time;//日期信息
    private String url;//图片地址，多个用逗号隔开
    private String is_favoured;//是否收藏过，0没有  1收藏
    private FavoursObj favours;//收藏列表
    private List<Favours> comments;//喜爱列表
    private boolean play;

    public boolean isPlay() {
        return play;
    }

    public void setPlay(boolean play) {
        this.play = play;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getChild_name() {
        return child_name;
    }

    public void setChild_name(String child_name) {
        this.child_name = child_name;
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

    public String getIs_share() {
        return is_share;
    }

    public void setIs_share(String is_share) {
        this.is_share = is_share;
    }

    public String getSchool_id() {
        return school_id;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }

    public String getPt() {
        return pt;
    }

    public void setPt(String pt) {
        this.pt = pt;
    }

    public long getDateline() {
        return dateline;
    }

    public void setDateline(long dateline) {
        this.dateline = dateline;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIs_favoured() {
        return is_favoured;
    }

    public void setIs_favoured(String is_favoured) {
        this.is_favoured = is_favoured;
    }

    public FavoursObj getFavours() {
        return favours;
    }

    public void setFavours(FavoursObj favours) {
        this.favours = favours;
    }

    public List<Favours> getComments() {
        return comments;
    }

    public void setComments(List<Favours> comments) {
        this.comments = comments;
    }

    public String getPublisher_cover() {
        return publisher_cover;
    }

    public void setPublisher_cover(String publisher_cover) {
        this.publisher_cover = publisher_cover;
    }
}
