package com.nchu.easyword.dao.model;

import java.util.List;

public class Words {
    private Long id;

    private String word;

    private String exchange;

    private String voice;

    private Integer times;
    /*词条释义*/
    private List<MeansView> meansList;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word == null ? null : word.trim();
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange == null ? null : exchange.trim();
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice == null ? null : voice.trim();
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public List<MeansView> getMeansList() {
        return meansList;
    }

    public void setMeansList(List<MeansView> meansList) {
        this.meansList = meansList;
    }
}