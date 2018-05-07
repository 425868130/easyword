package com.nchu.easyword.dao.mapperInterface;

import com.nchu.easyword.dao.model.LikeSentence;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 用户例句点赞记录表映射文件
 */
@Repository
public interface LikeSentenceMapper {
    int deleteByPrimaryKey(Long id);

    int insert(LikeSentence record);

    int insertSelective(LikeSentence record);

    LikeSentence selectByPrimaryKey(Long id);

    /**
     * 获取用户对某条例句的点赞记录
     *
     * @param userId      用户id
     * @param sentence_id 要查询的例句id
     * @return 点赞记录
     */
    LikeSentence getUserRecord(@Param("userId") long userId, @Param("sentence_id") long sentence_id);

    int updateByPrimaryKeySelective(LikeSentence record);

    int updateByPrimaryKey(LikeSentence record);
}