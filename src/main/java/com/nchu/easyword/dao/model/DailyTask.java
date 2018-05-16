package com.nchu.easyword.dao.model;

import java.util.Date;

public class DailyTask {
    private Long id;

    private Long userId;

    private Integer wordNum;

    private Integer todayProgress;

    private Integer practiceProgress;

    private Integer reviewProgress;

    private Date gmtCreate;

    private Date gmtModified;

    private String wordList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getWordNum() {
        return wordNum;
    }

    public void setWordNum(Integer wordNum) {
        this.wordNum = wordNum;
    }

    public Integer getTodayProgress() {
        return todayProgress;
    }

    public void setTodayProgress(Integer todayProgress) {
        this.todayProgress = todayProgress;
    }

    public Integer getPracticeProgress() {
        return practiceProgress;
    }

    public void setPracticeProgress(Integer practiceProgress) {
        this.practiceProgress = practiceProgress;
    }

    public Integer getReviewProgress() {
        return reviewProgress;
    }

    public void setReviewProgress(Integer reviewProgress) {
        this.reviewProgress = reviewProgress;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getWordList() {
        return wordList;
    }

    public void setWordList(String wordList) {
        this.wordList = wordList == null ? null : wordList.trim();
    }
}