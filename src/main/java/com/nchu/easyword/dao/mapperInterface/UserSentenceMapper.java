package com.nchu.easyword.dao.mapperInterface;

import com.nchu.easyword.dao.model.UserSentence;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSentenceMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserSentence record);

    int insertSelective(UserSentence record);

    UserSentence selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserSentence record);

    int updateByPrimaryKey(UserSentence record);

    /**
     * 通过单词获取用户自定义例句(按点赞数倒序)
     *
     * @param page     页码
     * @param pageSize 页面记录条数
     * @param word_id  要获取例句的单词
     * @param isCheck  是否通过审核
     * @return 例句列表
     */
    List<UserSentence> getUserSentencesByWord(@Param("page") int page, @Param("pageSize") int pageSize, @Param("word_id") long word_id, @Param("isCheck") boolean isCheck);

    /**
     * 通过单词获取用户自定义例句总记录条数,用于分页控件的总记录条数显示
     *
     * @param word_id 单词id
     * @param isCheck 是否已审核
     * @return
     */
    long SentencesByWordTotal(@Param("word_id") long word_id, @Param("isCheck") boolean isCheck);

    /**
     * 获取指定例句用户是否点赞，传入例句id列表，返回当前记录数
     *
     * @param userId      用户id
     * @param sentence_id 例句id列表
     * @return
     */
    int userLikeRecord(@Param("userId") long userId, @Param("sentence_id") long sentence_id);
}