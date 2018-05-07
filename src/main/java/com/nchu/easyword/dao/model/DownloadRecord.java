package com.nchu.easyword.dao.model;

import java.util.Date;

public class DownloadRecord {
    private Long id;

    private Long userid;

    private Long fileid;

    private Date gmtCreate;

    public DownloadRecord() {
    }

    public DownloadRecord(Long userid, Long fileid, Date gmtCreate) {
        this.userid = userid;
        this.fileid = fileid;
        this.gmtCreate = gmtCreate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Long getFileid() {
        return fileid;
    }

    public void setFileid(Long fileid) {
        this.fileid = fileid;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
}