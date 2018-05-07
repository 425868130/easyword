package com.nchu.easyword.dao.mapperInterface;

import com.nchu.easyword.dao.model.PracticeTask;
import org.springframework.stereotype.Repository;

@Repository
public interface PracticeTaskMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PracticeTask record);

    int insertSelective(PracticeTask record);

    PracticeTask selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PracticeTask record);

    int updateByPrimaryKeyWithBLOBs(PracticeTask record);

    int updateByPrimaryKey(PracticeTask record);
}