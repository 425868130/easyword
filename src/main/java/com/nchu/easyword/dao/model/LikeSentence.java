package com.nchu.easyword.dao.model;

import java.util.Date;

/**
 * 用户点赞记录表对应的实体对象
 */
public class LikeSentence {
    private Long id;

    private Long userId;

    private Long sentenceId;

    private Date gmtCreate;

    public LikeSentence() {
    }

    public LikeSentence(Long userId, Long sentenceId, Date gmtCreate) {
        this.userId = userId;
        this.sentenceId = sentenceId;
        this.gmtCreate = gmtCreate;
    }

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

    public Long getSentenceId() {
        return sentenceId;
    }

    public void setSentenceId(Long sentenceId) {
        this.sentenceId = sentenceId;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
}