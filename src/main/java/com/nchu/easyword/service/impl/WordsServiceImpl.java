package com.nchu.easyword.service.impl;

import com.nchu.easyword.dao.mapperInterface.MeansViewMapper;
import com.nchu.easyword.dao.mapperInterface.MemoryRecordMapper;
import com.nchu.easyword.dao.mapperInterface.WordsMapper;
import com.nchu.easyword.dao.model.*;
import com.nchu.easyword.dto.WordsDTO;
import com.nchu.easyword.service.inface.WordsService;
import com.nchu.easyword.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xujw
 * 单词相关业务逻辑接口实现
 */
@Service
@Transactional
public class WordsServiceImpl implements WordsService {
    @Autowired
    WordsMapper wordsMapper;
    @Autowired
    MemoryRecordMapper recordMapper;
    @Autowired
    MeansViewMapper meansViewMapper;

    /**
     * 通过id获取单词对象
     *
     * @param word_id 单词id
     * @return 返回单词对象
     */
    @Override
    public WordsDTO getById(long word_id) {
        return WordsDTO.transFromWords(wordsMapper.getWordWithMeanById(word_id));
    }

    @Override
    public WordsDTO searchByKeywordsEn(String keywords) {
        return WordsDTO.transFromWords(wordsMapper.searchByKeywords(keywords));
    }

    /**
     * 查询用户最近一周的单词背诵统计
     *
     * @param userId
     * @return
     */
    @Override
    public List<RecentRecord> getWeekRecordByUser(long userId) {
        return recordMapper.getWeekRecordByUser(userId);
    }

    /**
     * 获取随机除单词id为excludeID的三条单词释义，用于每日任务的错误答案显示
     *
     * @param excludeID 要排除的单词id,即正确的单词释义
     * @return
     */
    @Override
    public List<WordsDTO> getRandomMean(long excludeID) {
        List<WordsDTO> wordsDTO = new ArrayList<>();
        /*循环查询3次随机单词*/
        for (int i = 0; i < 3; i++) {
            wordsDTO.add(WordsDTO.transFromWords(wordsMapper.getRandomWord(excludeID)));
        }
        return wordsDTO;
    }

    /**
     * 添加一条单词背诵记录
     *
     * @param memoryRecord
     * @return
     */
    @Override
    public boolean addMemoryRecord(MemoryRecord memoryRecord) {
        memoryRecord.setGmtCreate(DateUtil.getCurrentTimestamp());
        memoryRecord.setGmtModified(DateUtil.getCurrentTimestamp());
        if (recordMapper.insertSelective(memoryRecord) == 1) {
            return true;
        }
        return false;
    }

    /**
     * 通过用户id及背诵时间获取背诵记录
     *
     * @param userId    用户id
     * @param startTime 起始时间
     * @param endTime   结束时间
     * @return
     */
    @Override
    public List<MemoryRecord> getRecords(long userId, Date startTime, Date endTime) {
        return null;
    }
}
