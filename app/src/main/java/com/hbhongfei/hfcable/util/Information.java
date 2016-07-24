package com.hbhongfei.hfcable.util;

import java.io.Serializable;

/**
 * Created by tyd on 2016/7/23.
 */
public class Information  implements Serializable{
    private String title;   //标题
    private String brief;   //简单介绍
    private String imgUrl;  //图片url
    private String contentUrl; //详细内容url;

    public String getTitle() {
        return title;
    }

    public String getBrief() {
        return brief;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    @Override
    public String toString() {
        return "Information{" +
                "title='" + title + '\'' +
                ", brief='" + brief + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", contentUrl='" + contentUrl + '\'' +
                '}';
    }
}
