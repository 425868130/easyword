package com.nchu.easyword.dao.model;

/**
 * 各种时态
 */
public class WordsExchange {
    private String word_third, word_done, word_pl, word_est, word_ing, word_er, word_past;

    public String getWord_third() {
        return word_third;
    }

    public void setWord_third(String word_third) {
        this.word_third = word_third.replace("\"", "");
    }

    public String getWord_done() {
        return word_done;
    }

    public void setWord_done(String word_done) {
        this.word_done = word_done.replace("\"", "");
    }

    public String getWord_pl() {
        return word_pl;
    }

    public void setWord_pl(String word_pl) {
        this.word_pl = word_pl.replace("\"", "");
    }

    public String getWord_est() {
        return word_est;
    }

    public void setWord_est(String word_est) {
        this.word_est = word_est.replace("\"", "");
    }

    public String getWord_ing() {
        return word_ing;
    }

    public void setWord_ing(String word_ing) {
        this.word_ing = word_ing.replace("\"", "");
    }

    public String getWord_er() {
        return word_er;
    }

    public void setWord_er(String word_er) {
        this.word_er = word_er.replace("\"", "");
    }

    public String getWord_past() {
        return word_past;
    }

    public void setWord_past(String word_past) {
        this.word_past = word_past.replace("\"", "");
    }
}
