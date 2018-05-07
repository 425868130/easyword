package com.nchu.easyword.dao.model;

import java.io.Serializable;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * 2018-4-7 22:22:17
 *
 * @author xujw
 * 单词背诵任务的单词列表对象，用于以json格式保存任务信息到数据库中
 */
public class WordTaskJsonObj implements Serializable {
    private static final long serialVersionUID = -3452946215858808471L;
    private Long word_id;
    private String word;
    /*是否记住,用于今日任务中判断是否记住*/
    boolean isRemember;
    /*是否复习,用于复习任务中的判断*/
    boolean isReview;
    /*是否完成巩固练习*/
    boolean isPractice;
    /*是否完成测试*/
    boolean isTest;

    public WordTaskJsonObj() {
    }

    public WordTaskJsonObj(Long word_id, String word, boolean isRemember, boolean isReview, boolean isPractice, boolean isTest) {
        this.word_id = word_id;
        this.word = word;
        this.isRemember = isRemember;
        this.isReview = isReview;
        this.isPractice = isPractice;
        this.isTest = isTest;
    }

    /*从单词列表转换为默认的任务词汇表*/
    public static List<WordTaskJsonObj> transFromWords(List<Words> words) {
        return words.stream().map(word -> new WordTaskJsonObj(word.getId(), word.getWord(), false, false, false, false)).collect(toList());
    }

    public Long getWord_id() {
        return word_id;
    }

    public void setWord_id(Long word_id) {
        this.word_id = word_id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public boolean isRemember() {
        return isRemember;
    }

    public void setRemember(boolean remember) {
        isRemember = remember;
    }

    public boolean isReview() {
        return isReview;
    }

    public void setReview(boolean review) {
        isReview = review;
    }

    public boolean isPractice() {
        return isPractice;
    }

    public void setPractice(boolean practice) {
        isPractice = practice;
    }

    public boolean isTest() {
        return isTest;
    }

    public void setTest(boolean test) {
        isTest = test;
    }
}
