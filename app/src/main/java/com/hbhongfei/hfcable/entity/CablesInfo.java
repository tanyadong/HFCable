package com.hbhongfei.hfcable.entity;

/**
 * Created by 苑雪元 on 2016/8/2.
 * 电缆信息
 */
public class CablesInfo {

    protected String Id;
    protected String name;
    protected boolean isChoosed;
    private String imageUrl;
    private String introduce;
    private double price;
    private int count;
    private int position;// 绝对位置，只在ListView构造的购物车中，在删除时有效
    private int goodsImg;


    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChoosed() {
        return isChoosed;
    }

    public void setChoosed(boolean choosed) {
        isChoosed = choosed;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(int goodsImg) {
        this.goodsImg = goodsImg;
    }

    public CablesInfo(String id, String name, boolean isChoosed, String imageUrl, String introduce, double price, int count, int position, int goodsImg) {
        Id = id;
        this.name = name;
        this.isChoosed = isChoosed;
        this.imageUrl = imageUrl;
        this.introduce = introduce;
        this.price = price;
        this.count = count;
        this.position = position;
        this.goodsImg = goodsImg;
    }
    public CablesInfo(String id, String name, String introduce, double price, int count,
                     int goodsImg) {
        Id = id;
        this.name = name;
        this.introduce = introduce;
        this.price = price;
        this.count = count;
        this.goodsImg=goodsImg;
    }
    public CablesInfo() {
    }
}
