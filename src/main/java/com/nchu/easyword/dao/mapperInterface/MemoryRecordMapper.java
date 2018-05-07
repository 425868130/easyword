package com.nchu.easyword.dao.mapperInterface;


import com.nchu.easyword.dao.model.MemoryRecord;
import com.nchu.easyword.dao.model.RecentRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemoryRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MemoryRecord record);

    int insertSelective(MemoryRecord record);

    MemoryRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MemoryRecord record);

    int updateByPrimaryKey(MemoryRecord record);

    /**
     * 查询用户最近一周的单词背诵统计
     *
     * @param userId
     * @return
     */
    List<RecentRecord> getWeekRecordByUser(@Param("userId") long userId);
}