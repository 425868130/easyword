package com.nchu.easyword.dao.model;

/**
 * 按日期的背诵记录统计实体，记录具体某天的背诵单词数量
 */
public class RecentRecord {
    private String dataTime;
    private int num;

    public String getTime() {
        return dataTime;
    }

    public void setTime(String dataTime) {

        this.dataTime = dataTime;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
