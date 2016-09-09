package com.hbhongfei.hfcable.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间格式的转换
 * Created by 苑雪元 on 2016/9/9.
 */
public class DateUtils {
    /**
     * @return 返回unix时间戳 (1970年至今的秒数)
     */
    public static long getUnixStamp(){
        return System.currentTimeMillis()/1000;
    }

    /**
     * @return 得到昨天的日期
     */
    public static String getYestoryDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String yestoday = sdf.format(calendar.getTime());
        return yestoday;
    }

    /**
     * @return 得到今天的日期
     */
    public static  String getTodayDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());
        return date;
    }

    /**
     * 时间戳转化为时间格式
     * @param timeStamp
     * @return 得到格式化的时间日期 yyyy-MM-dd HH:mm:ss 24小时制 hh表示12小时制
     */
    public static String timeStampToStr(long timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(timeStamp * 1000);
        return date;
    }

    /**
     * 得到日期   yyyy-MM-dd
     * @param timeStamp  时间戳
     * @return 得到格式化的时间日期 yyyy-MM-dd 24小时制
     */
    public static String formatDate(long timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(timeStamp*1000);
        return date;
    }

    /**
     * 得到时间  HH:mm:ss
     * @param timeStamp   时间戳
     * @return 得到格式化的时分秒HH:mm:ss
     */
    public static String getTime(long timeStamp) {
        String time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(timeStamp * 1000);
        String[] split = date.split("\\s");
        if ( split.length > 1 ){
            time = split[1];
        }
        return time;
    }

    /**
     * 将一个时间戳转换成提示性时间字符串，如刚刚，1秒前
     *
     * @param timeStamp
     * @return 得到人性化的日期时间
     */
    public static String convertTimeToFormat(long timeStamp) {
        long curTime =getUnixStamp() ;
        long time = curTime - timeStamp/1000;

        if (time < 60 && time >= 0) {
            return "刚刚";
        } else if (time >= 60 && time < 3600) {
            return time / 60 + "分钟前";
        } else if (time >= 3600 && time < 3600 * 24) {
            return time / 3600 + "小时前";
        } else if (time >= 3600 * 24 && time < 3600 * 24 * 30) {
            return time / 3600 / 24 + "天前";
        } else if (time >= 3600 * 24 * 30 && time < 3600 * 24 * 30 * 12) {
            return time / 3600 / 24 / 30 + "个月前";
        } else if (time >= 3600 * 24 * 30 * 12) {
            return time / 3600 / 24 / 30 / 12 + "年前";
        } else {
            return "111";
        }
    }

    /**
     * 将一个时间戳转换成提示性时间字符串，(多少分钟)
     *
     * @param timeStamp
     * @return
     */
    public static String timeStampToFormat(long timeStamp) {
        long curTime =getUnixStamp() ;
        long time = curTime - timeStamp;
        return time/60 + "";
    }

}
