package com.hbhongfei.hfcable.entity;

import java.util.Date;

/**
 * Created by 苑雪元 on 2016/8/4.
 */
public class User {
    private String id;
    private String phoneNumber;
    private String password;
    private String sex;
    private String nickName;
    private String headPortrait;
    private Date createTime;
    private Date updateTime;

    public User(String id, String phoneNumber, String password, String sex, String nickName, String headPortrait, Date createTime, Date updateTime) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.sex = sex;
        this.nickName = nickName;
        this.headPortrait = headPortrait;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", password='" + password + '\'' +
                ", sex='" + sex + '\'' +
                ", nickName='" + nickName + '\'' +
                ", headPortrait='" + headPortrait + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
