package com.nchu.easyword.dao.mapperInterface;

import com.nchu.easyword.dao.model.Words;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Words record);

    int insertSelective(Words record);

    Words selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Words record);

    int updateByPrimaryKey(Words record);

    /**
     * 通过关键词查找单词
     *
     * @param keywords
     * @return
     */
    Words searchByKeywords(String keywords);

    /**
     * 通过id获取单词对象包括中文解释
     *
     * @param wordID
     * @return
     */
    Words getWordWithMeanById(@Param("wordID") long wordID);

    /**
     * 获取每日任务
     *
     * @return
     */
    List<Words> getDailyTask();

    /**
     * 随机获取指定条连续的单词记录
     *
     * @param excludeID 要排除的单词id
     * @return
     */
    Words getRandomWord(@Param("excludeID") long excludeID);
}