package com.nchu.easyword.dto;

import com.alibaba.fastjson.JSON;
import com.nchu.easyword.dao.model.Means;
import com.nchu.easyword.dao.model.Words;
import com.nchu.easyword.dao.model.WordsExchange;
import com.nchu.easyword.dao.model.WordsVoice;
import com.nchu.easyword.utils.UnicodeUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 2018-3-4 16:30:03
 *
 * @author xujw
 * 单词词条数据传输对象
 */
public class WordsDTO {
    private Long id;
    private String word;
    private WordsExchange exchange;
    private WordsVoice voice;
    private Integer times;
    /*一个单词可以有多条解释,每条解释包含解文本以及单词词性*/
    private List<Means> meansList;

    /**
     * 将单词的查询结果对象words转换为适合传输的数据对象
     *
     * @param
     * @return
     */
    public static WordsDTO transFromWords(Words words) {
        if (words == null) {
            return null;
        }
        WordsDTO wordsDTO = new WordsDTO();
        wordsDTO.setId(words.getId());
        wordsDTO.setTimes(words.getTimes());
        wordsDTO.setWord(words.getWord());
        /*将数据库中取到的JSON字符串转换为Java对象*/
        if (words.getExchange() != null) {
            wordsDTO.setExchange(JSON.parseObject(words.getExchange(), WordsExchange.class));
        }
        if (words.getVoice() != null) {
            wordsDTO.setVoice(JSON.parseObject(UnicodeUtils.decodeUnicode(words.getVoice()), WordsVoice.class));
        }
        List<Means> meansList = words.getMeansList().stream().map(meansView ->
                {
                    Means means = new Means();
                    means.setProName(meansView.getProName());
                    means.setProMeans(meansView.getProMeans());
                    means.setMeans(UnicodeUtils.decodeUnicode(meansView.getMeans().replace("\"", "")));
                    return means;
                }
        ).collect(Collectors.toList());
        wordsDTO.setMeansList(meansList);
        return wordsDTO;
    }

    /**
     * Getter和Setter方法
     *
     * @return
     */

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
        this.word = word;
    }

    public WordsExchange getExchange() {
        return exchange;
    }

    public void setExchange(WordsExchange exchange) {
        this.exchange = exchange;
    }

    public WordsVoice getVoice() {
        return voice;
    }

    public void setVoice(WordsVoice voice) {
        this.voice = voice;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public List<Means> getMeansList() {
        return meansList;
    }

    public void setMeansList(List<Means> meansList) {
        this.meansList = meansList;
    }
}
