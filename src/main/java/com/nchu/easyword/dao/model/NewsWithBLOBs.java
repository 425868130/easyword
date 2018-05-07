package com.nchu.easyword.dao.model;

public class NewsWithBLOBs extends News {
    private String htmlContent;

    private String englishText;

    private String translationText;

    public NewsWithBLOBs() {
    }

    public NewsWithBLOBs(String htmlContent, String englishText, String translationText) {
        this.htmlContent = htmlContent;
        this.englishText = englishText;
        this.translationText = translationText;
    }

    public NewsWithBLOBs(String titleEn, String titleCn, String summary, String coverPic, Integer wordNum, String source, String voice, String htmlContent, String englishText, String translationText) {
        super(titleEn, titleCn, summary, coverPic, wordNum, source, voice);
        this.htmlContent = htmlContent;
        this.englishText = englishText;
        this.translationText = translationText;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent == null ? null : htmlContent.trim();
    }

    public String getEnglishText() {
        return englishText;
    }

    public void setEnglishText(String englishText) {
        this.englishText = englishText == null ? null : englishText.trim();
    }

    public String getTranslationText() {
        return translationText;
    }

    public void setTranslationText(String translationText) {
        this.translationText = translationText == null ? null : translationText.trim();
    }
}