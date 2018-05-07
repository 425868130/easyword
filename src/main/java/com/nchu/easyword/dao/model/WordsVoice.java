package com.nchu.easyword.dao.model;

/**
 * 2018-3-6 20:56:36
 * @author xujw
 * 单词音标及发音
 */
public class WordsVoice {
    private String ph_en, ph_am, ph_en_mp3, ph_am_mp3, ph_tts_mp3;

    public String getPh_en() {
        return ph_en;
    }

    public void setPh_en(String ph_en) {
        this.ph_en = ph_en;
    }

    public String getPh_am() {
        return ph_am;
    }

    public void setPh_am(String ph_am) {
        this.ph_am = ph_am;
    }

    public String getPh_en_mp3() {
        return ph_en_mp3;
    }

    public void setPh_en_mp3(String ph_en_mp3) {
        this.ph_en_mp3 = ph_en_mp3;
    }

    public String getPh_am_mp3() {
        return ph_am_mp3;
    }

    public void setPh_am_mp3(String ph_am_mp3) {
        this.ph_am_mp3 = ph_am_mp3;
    }

    public String getPh_tts_mp3() {
        return ph_tts_mp3;
    }

    public void setPh_tts_mp3(String ph_tts_mp3) {
        this.ph_tts_mp3 = ph_tts_mp3;
    }
}
