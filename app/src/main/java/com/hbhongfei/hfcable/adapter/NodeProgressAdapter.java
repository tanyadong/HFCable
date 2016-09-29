package com.hbhongfei.hfcable.adapter;

import com.hbhongfei.hfcable.pojo.LogisticsData;

import java.util.ArrayList;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-06-28
 * Time: 14:30
 * FIXME
 */
public interface NodeProgressAdapter {

    /**
     * 返回集合大小
     *
     * @return
     */
    int getCount();

    /**
     * 适配数据的集合
     *
     * @return
     */
    ArrayList<LogisticsData> getData();

}
