package com.nchu.easyword.service.impl;

import com.alibaba.fastjson.JSON;
import com.nchu.easyword.dao.mapperInterface.DailyTaskMapper;
import com.nchu.easyword.dao.mapperInterface.PracticeTaskMapper;
import com.nchu.easyword.dao.mapperInterface.WordsMapper;
import com.nchu.easyword.dao.model.DailyTask;
import com.nchu.easyword.dao.model.PracticeTask;
import com.nchu.easyword.dao.model.WordTaskJsonObj;
import com.nchu.easyword.dao.model.Words;
import com.nchu.easyword.service.inface.TaskService;
import com.nchu.easyword.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

/**
 * 2018-4-7 17:20:00
 *
 * @author xujw
 * 单词背诵任务相关业务逻辑接口
 */
@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    WordsMapper wordsMapper;
    @Autowired
    DailyTaskMapper dailyTaskMapper;
    @Autowired
    PracticeTaskMapper practiceTaskMapper;

    /**
     * 创建指定用户的今日背单词任务
     *
     * @param userId 要创建任务的用户id
     * @return 新创建的任务
     */
    @Override
    public DailyTask createDailyTask(long userId) {
        /*从单词表中随机选择50个单词做为今日任务*/
        List<Words> words = wordsMapper.getDailyTask();
        /*@TODO 优化:排除用户已经背诵完成的单词*/
        DailyTask dailyTask = new DailyTask();
        dailyTask.setReviewProgress(0);
        dailyTask.setTodayProgress(0);
        dailyTask.setUserId(userId);
        dailyTask.setWordNum(50);
        /*将单词列表转换为带任务属性的列表*/
        dailyTask.setWordList(JSON.toJSONString(WordTaskJsonObj.transFromWords(words)));
        dailyTask.setGmtCreate(DateUtil.getCurrentTimestamp());
        dailyTask.setGmtModified(DateUtil.getCurrentTimestamp());
        if (dailyTaskMapper.insertSelective(dailyTask) == 1) {
            return dailyTask;
        }
        return null;
    }

    /**
     * 刷新今日任务单词
     *
     * @param oldTask 刷新前的今日任务
     * @return 刷新后的今日任务
     */
    @Override
    public DailyTask refreshDailyTask(DailyTask oldTask) {
        /*从单词表中随机选择50个单词做为今日任务*/
        List<Words> words = wordsMapper.getDailyTask();
        /*任务进度归零*/
        oldTask.setReviewProgress(0);
        oldTask.setTodayProgress(0);
        oldTask.setPracticeProgress(0);
        /*设置修改时间*/
        oldTask.setGmtModified(DateUtil.getCurrentTimestamp());
        oldTask.setWordList(JSON.toJSONString(WordTaskJsonObj.transFromWords(words)));
        if (dailyTaskMapper.updateByPrimaryKeySelective(oldTask) == 1) {
            return oldTask;
        }
        return null;
    }

    /**
     * 更新任务进度信息
     *
     * @param dailyTask 新的任务信息(userId字段禁止修改)
     * @return 操作结果
     */
    @Transactional
    @Override
    public boolean updateTaskProgress(DailyTask dailyTask) {
        dailyTask.setGmtModified(DateUtil.getCurrentTimestamp());
        if (dailyTaskMapper.updateByPrimaryKeySelective(dailyTask) == 1) {
            return true;
        }
        return false;
    }

    /**
     * 获取指定用户的指定时间的任务信息
     *
     * @param userId    要获取任务的用户
     * @param timestamp 任务创建时间
     * @return 任务实体
     */
    @Override
    public DailyTask getUserTask(long userId, Timestamp timestamp) {
        DailyTask dailyTask = dailyTaskMapper.getUserTask(userId, timestamp);
        return dailyTask;
    }

    /**
     * 创建测试任务(巩固练习)
     *
     * @param userId 要创建任务的用户
     * @return
     */
    @Override
    public boolean createPracticeTask(long userId) {
        return false;
    }

    /**
     * 获取用户练习任务信息
     *
     * @param userId 用户id
     * @return
     */
    @Override
    public PracticeTask getUserPracticeTask(long userId) {
        return null;
    }

    /**
     * 更新巩固练习任务进度信息
     *
     * @param practiceTask 新的任务信息(userId字段禁止修改)
     * @return 操作结果
     */
    @Override
    public boolean updatePracticeTask(PracticeTask practiceTask) {
        return false;
    }

    /**
     * 获取指定用户的复习任务，即昨天的任务单词
     *
     * @param userId 用户id
     * @return
     */
    @Override
    public DailyTask getReviewTask(Long userId) {
        return dailyTaskMapper.getReviewTask(userId);
    }


}
