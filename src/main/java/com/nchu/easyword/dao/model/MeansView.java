package com.nchu.easyword.dao.model;

public class MeansView {
    private Long wordid;

    private String means;

    private String proName;

    private String proMeans;

    public Long getWordid() {
        return wordid;
    }

    public void setWordid(Long wordid) {
        this.wordid = wordid;
    }

    public String getMeans() {
        return means;
    }

    public void setMeans(String means) {
        this.means = means == null ? null : means.trim();
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName == null ? null : proName.trim();
    }

    public String getProMeans() {
        return proMeans;
    }

    public void setProMeans(String proMeans) {
        this.proMeans = proMeans == null ? null : proMeans.trim();
    }
}