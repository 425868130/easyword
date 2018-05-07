package com.nchu.easyword.dao.mapperInterface;

import com.nchu.easyword.dao.model.PersonalGlossary;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonalGlossaryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PersonalGlossary record);

    int insertSelective(PersonalGlossary record);

    PersonalGlossary selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PersonalGlossary record);

    int updateByPrimaryKey(PersonalGlossary record);

    /**
     * 移除生词本中的指定单词
     *
     * @param word_id 要移除的单词id
     * @param userId  用户id
     * @return 操作结果
     */
    int removeWord(@Param("word_id") long word_id, @Param("userId") long userId);

    /**
     * 分页查询
     *
     * @param start
     * @param limit
     * @param userId
     * @return
     */
    List<PersonalGlossary> getGlossaryByPage(@Param("start") int start, @Param("limit") int limit, @Param("userId") long userId, @Param("isRemember") boolean isRemember);

    /**
     * 获取指定用户的生词总数
     *
     * @param userId 用户id
     * @return 返回总记录数
     */
    long countWordByUser(@Param("userId") long userId, @Param("isRemember") boolean isRemember);

    /**
     * 通过wordID和userID获取生词记录
     *
     * @param wordID 单词id
     * @param userId 生词表所属用户id
     * @return 生词记录实体
     */
    PersonalGlossary selectByWordID(@Param("wordID") long wordID, @Param("userId") long userId);

    /**
     * 获取用户近期添加的前10个生词
     *
     * @param userId 要获取的用户id
     * @return 生词列表
     */
    List<PersonalGlossary> recentGlossary(@Param("userId") long userId);
}