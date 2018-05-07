package com.nchu.easyword.dao.mapperInterface;


import com.nchu.easyword.dao.model.DailyTask;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public interface DailyTaskMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DailyTask record);

    int insertSelective(DailyTask record);

    DailyTask selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DailyTask record);

    int updateByPrimaryKeyWithBLOBs(DailyTask record);

    int updateByPrimaryKey(DailyTask record);

    /**
     * 获取用户的指定日期的任务信息
     *
     * @param userId 用户id
     * @param time   时间
     * @return
     */
    DailyTask getUserTask(@Param("userId") long userId, @Param("time") Timestamp time);

    /**
     * 获取指定用户的复习任务，即昨天的任务单词
     *
     * @param userId 用户id
     * @return
     */
    DailyTask getReviewTask(@Param("userId") Long userId);
}