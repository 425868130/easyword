package com.nchu.easyword.utils;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Dream Sky on 2017/3/10.
 * 获取在线时长
 */
public class DateUtil {
    static Date date = new Date();

    /*将数据库中的在线时间转换为网页要现实的额格式*/
    public static String getOnlineTime(String loginTime) {
        int hour = Integer.parseInt(loginTime.substring(11, 13));
        int mins = Integer.parseInt(loginTime.substring(14, 16));
        Calendar now = Calendar.getInstance();
        int NowHour = now.get(Calendar.HOUR_OF_DAY);
        int NowMins = now.get(Calendar.MINUTE);
        String ReTime = (NowHour - hour) + "小时" + (NowMins - mins) + "分";
        //System.out.println(ReTime);
        return ReTime;
    }

    /*获取当前时间*/
    public static Date getCurrentTime() {
        date.setTime(System.currentTimeMillis());
        return date;
    }

    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /*获取给定时间与当前时间相差的小时数,用于确定校验码的有效性*/
    public static long TheHourUpToNow(Date date) {
        Date now = getCurrentTime();
        if (now.after(date)) {
            long value = now.getTime() - date.getTime();
            long i = value / 3600000;
            //System.out.println("时间差值：" + i);
            return i;
        }
        return -1;
    }

    /**
     * 获取天日期时间格式为 xxxx-xx-xx
     *
     * @return
     */
    public static String getTodayString() {
        return getCurrentTimestamp().toString().substring(0, 10);
    }
}
