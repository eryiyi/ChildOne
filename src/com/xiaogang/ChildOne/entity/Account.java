package com.xiaogang.ChildOne.entity;

import java.io.Serializable;

/**
 * author: liuzwei
 * Date: 2014/8/11
 * Time: 14:29
 * 类的功能、说明写在此处.
 */
public class Account implements Serializable {
    private String user_name;//账号
    private String uid;//uid 用户ID
    private String email;//邮件
    private String mobile;//手机号
    private String class_id ;//所属班级ID
    private String school_id;//所属幼儿园ID
    private String is_student;//是否家长登陆  1 是  0 否
    private String f_cover;//father头像， is_student为1 返回
    private String m_cover;//mother头像， is_student为1 返回
    private String f_name;//father名字，
    private String m_name;//mother名称
    private String f_user_type;//father类型为1
    private String m_user_type;//mother类型为2
    private String is_teacher;//是否教师登陆  1是  0否
    private String cover;//教师头像地址  is_teacher为1 返回
    private String nick_name;//教师名称  is_teacher为1 返回
    private String dept;//职称


    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getSchool_id() {
        return school_id;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }

    public String getIs_student() {
        return is_student;
    }

    public void setIs_student(String is_student) {
        this.is_student = is_student;
    }

    public String getF_cover() {
        return f_cover;
    }

    public void setF_cover(String f_cover) {
        this.f_cover = f_cover;
    }

    public String getM_cover() {
        return m_cover;
    }

    public void setM_cover(String m_cover) {
        this.m_cover = m_cover;
    }

    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public String getM_name() {
        return m_name;
    }

    public void setM_name(String m_name) {
        this.m_name = m_name;
    }

    public String getF_user_type() {
        return f_user_type;
    }

    public void setF_user_type(String f_user_type) {
        this.f_user_type = f_user_type;
    }

    public String getM_user_type() {
        return m_user_type;
    }

    public void setM_user_type(String m_user_type) {
        this.m_user_type = m_user_type;
    }

    public String getIs_teacher() {
        return is_teacher;
    }

    public void setIs_teacher(String is_teacher) {
        this.is_teacher = is_teacher;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }
}
