package com.nchu.easyword.service.inface;

import com.nchu.easyword.dao.model.*;
import com.nchu.easyword.dto.WordsDTO;
import com.nchu.easyword.exception.ServiceException;

import java.util.Date;
import java.util.List;

/**
 * 2018年3月4日10:40:182
 *
 * @author xujw
 * 单词业务逻辑接口
 */
public interface WordsService {
    /**
     * 通过id获取单词对象
     *
     * @param word_id 单词id
     * @return 返回单词对象
     */
    WordsDTO getById(long word_id);

    /**
     * 通过关键词查找单词
     *
     * @param keywords
     * @return
     */
    WordsDTO searchByKeywordsEn(String keywords);

    List<RecentRecord> getWeekRecordByUser(long userId);


    /**
     * 获取随机除单词id为excludeID的三条单词释义，用于每日任务的错误答案显示
     *
     * @param excludeID 要排除的单词id,即正确的单词释义
     * @return
     */
    List<WordsDTO> getRandomMean(long excludeID);

    /**
     * 添加一条单词背诵记录
     *
     * @param memoryRecord
     * @return
     */
    boolean addMemoryRecord(MemoryRecord memoryRecord);

    /**
     * 通过用户id及背诵时间获取背诵记录
     *
     * @param userId    用户id
     * @param startTime 起始时间
     * @param endTime   结束时间
     * @return
     */
    List<MemoryRecord> getRecords(long userId, Date startTime, Date endTime);

}
