package com.nchu.easyword.dao.model;

import java.util.Date;

public class News {
    private Long id;

    private String titleEn;

    private String titleCn;

    private String summary;

    private String coverPic;

    private Integer wordNum;

    private String source;

    private String voice;

    private Long views;

    private Integer point;

    private Integer experience;

    private Date gmtCreate;

    private Date gmtModified;

    public News() {
    }

    public News(String titleEn, String titleCn, String summary, String coverPic, Integer wordNum, String source, String voice) {
        this.titleEn = titleEn;
        this.titleCn = titleCn;
        this.summary = summary;
        this.coverPic = coverPic;
        this.wordNum = wordNum;
        this.source = source;
        this.voice = voice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn == null ? null : titleEn.trim();
    }

    public String getTitleCn() {
        return titleCn;
    }

    public void setTitleCn(String titleCn) {
        this.titleCn = titleCn == null ? null : titleCn.trim();
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary == null ? null : summary.trim();
    }

    public String getCoverPic() {
        return coverPic;
    }

    public void setCoverPic(String coverPic) {
        this.coverPic = coverPic == null ? null : coverPic.trim();
    }

    public Integer getWordNum() {
        return wordNum;
    }

    public void setWordNum(Integer wordNum) {
        this.wordNum = wordNum;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source == null ? null : source.trim();
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice == null ? null : voice.trim();
    }

    public Long getViews() {
        return views;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
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
}