package com.nchu.easyword.dao.mapperInterface;

import com.nchu.easyword.dao.model.MeansView;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeansViewMapper {
    int insert(MeansView record);

    int insertSelective(MeansView record);

    /**
     * 获取随机除单词id为excludeID的三条单词释义，用于每日任务的错误答案显示
     *
     * @param excludeID 要排除的单词id,即正确的单词释义
     * @return
     */
    List<MeansView> getRandomMean(@Param("excludeID") long excludeID);
}