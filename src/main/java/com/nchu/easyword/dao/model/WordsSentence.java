package com.nchu.easyword.dao.model;

/**
 * 2018-3-26 15:37:45
 * @author xujw
 * 单词例句对象
 */
public class WordsSentence {
    /*分别为例句的三部分，其中mid为例句匹配的单词,用于前端高亮处理，按first、mid、last顺序拼接可以还原例句*/
    private String first,mid, last;
    /*例句翻译*/
    private String translation;

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }
}
