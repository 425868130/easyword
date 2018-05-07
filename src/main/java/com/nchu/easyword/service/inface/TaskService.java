package com.nchu.easyword.service.inface;

import com.nchu.easyword.dao.model.DailyTask;
import com.nchu.easyword.dao.model.PracticeTask;

import java.sql.Timestamp;

/**
 * 2018-4-7 17:20:00
 *
 * @author xujw
 * 单词背诵任务相关业务逻辑接口
 */
public interface TaskService {
    /**
     * 创建指定用户的今日背单词任务
     *
     * @param userId 要创建任务的用户id
     * @return 操作结果
     */
    DailyTask createDailyTask(long userId);

    /**
     * 刷新今日任务单词
     *
     * @param oldTask 刷新前的今日任务
     * @return 刷新后的今日任务
     */
    DailyTask refreshDailyTask(DailyTask oldTask);

    /**
     * 更新任务进度信息
     *
     * @param dailyTask 新的任务信息(userId字段禁止修改)
     * @return 操作结果
     */
    boolean updateTaskProgress(DailyTask dailyTask);

    /**
     * 获取指定用户的指定时间的任务信息
     *
     * @param userId    要获取任务的用户
     * @param timestamp 任务创建时间
     * @return 任务实体
     */
    DailyTask getUserTask(long userId, Timestamp timestamp);

    /**
     * 创建测试任务(巩固练习)
     *
     * @param userId 要创建任务的用户
     * @return
     */
    boolean createPracticeTask(long userId);

    /**
     * 获取用户练习任务信息
     *
     * @param userId 用户id
     * @return
     */
    PracticeTask getUserPracticeTask(long userId);

    /**
     * 更新巩固练习任务进度信息
     *
     * @param practiceTask 新的任务信息(userId字段禁止修改)
     * @return 操作结果
     */
    boolean updatePracticeTask(PracticeTask practiceTask);

    /**
     * 获取指定用户的复习任务，即昨天的任务单词
     * @param userId 用户id
     * @return
     */
    DailyTask getReviewTask(Long userId);
}
