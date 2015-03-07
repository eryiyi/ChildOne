package com.xiaogang.ChildOne.entity;

import java.io.Serializable;

/**
 * Created by apple on 14-9-15.
 */
public class Message implements Serializable, Comparable {
    private String id;
    private String uid;
    private String to_uids;
    private String pmid;
    private String dateline;
    private String type;
    private String content;
    private String url;

    private int time;
    private boolean isComMsg;

    public Message() {
    }

    public Message(String uid, String to_uids, String dateline, String type, String content){
        this.uid = uid;
        this.to_uids = to_uids;
        this.dateline = dateline;
        this.type = type;
        this.content = content;
    }
    public Message(String uid, String to_uids, String dateline, String type, String content, int time, boolean isComMsg) {
        this.uid = uid;
        this.to_uids = to_uids;
        this.dateline = dateline;
        this.type = type;
        this.content = content;
        this.time = time;
        this.isComMsg = isComMsg;
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

    public String getTo_uids() {
        return to_uids;
    }

    public void setTo_uids(String to_uids) {
        this.to_uids = to_uids;
    }

    public String getPmid() {
        return pmid;
    }

    public void setPmid(String pmid) {
        this.pmid = pmid;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public boolean isComMsg() {
        return isComMsg;
    }

    public void setComMsg(boolean isComMsg) {
        this.isComMsg = isComMsg;
    }

    @Override
    public int compareTo(Object another) {
        Message otherMessage = (Message) another;
        return this.getDateline().compareTo(otherMessage.getDateline());
    }
}
