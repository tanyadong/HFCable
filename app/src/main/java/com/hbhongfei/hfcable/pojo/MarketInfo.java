package com.hbhongfei.hfcable.pojo;

import java.io.Serializable;

/**
 * Created by dell1 on 2016/8/22.
 */
public class MarketInfo implements Serializable{
    private String area;//市场地区
    private String productName;
    private String averagePrice;//均价
    private String fallOrise;//跌涨
    private String minPrice;
    private String maxPrice;
    private String factoryArea; //厂家产地
    private String data;//日期
    private String trend; //走势

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(String averagePrice) {
        this.averagePrice = averagePrice;
    }

    public String getFallOrise() {
        return fallOrise;
    }

    public void setFallOrise(String fallOrise) {
        this.fallOrise = fallOrise;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getFactoryArea() {
        return factoryArea;
    }

    public void setFactoryArea(String factoryArea) {
        this.factoryArea = factoryArea;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTrend() {
        return trend;
    }

    public void setTrend(String trend) {
        this.trend = trend;
    }

    @Override
    public String toString() {
        return "MarketInfo{" +
                "area='" + area + '\'' +
                ", productName='" + productName + '\'' +
                ", averagePrice='" + averagePrice + '\'' +
                ", fallOrise='" + fallOrise + '\'' +
                ", minPrice='" + minPrice + '\'' +
                ", maxPrice='" + maxPrice + '\'' +
                ", factoryArea='" + factoryArea + '\'' +
                ", data='" + data + '\'' +
                ", trend='" + trend + '\'' +
                '}';
    }
}
