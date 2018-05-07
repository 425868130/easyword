package com.nchu.easyword.service.inface;

import com.nchu.easyword.dao.model.UserSentence;
import com.nchu.easyword.dao.model.WordsSentence;
import com.nchu.easyword.exception.ServiceException;

import java.util.List;

/**
 * 2018-4-10 08:51:02
 *
 * @author xujw
 * 单词例句相关业务逻辑接口
 */
public interface SentenceService {

    /**
     * 通过网络获取第三方单词例句
     *
     * @param keywords 要获取例句的单词
     * @return 例句
     */
    List<WordsSentence> getNetSentences(String keywords) throws ServiceException;

    /**
     * 通过单词获取用户自定义例句(按点赞数倒序)
     *
     * @param page     页码
     * @param pageSize 页面记录条数
     * @param word_Id  要获取例句的单词
     * @param isCheck  是否通过审核
     * @return 例句列表
     */
    List<UserSentence> getUserSentencesByWord(int page, int pageSize, long word_Id, boolean isCheck);

    /**
     * 统计指定单词有多少用户定义例句
     *
     * @param word_Id 要查询的单词id
     * @param isCheck 是否已审核
     * @return
     */
    long countWordSentences(long word_Id, boolean isCheck);

    /**
     * 添加一个用户例句
     *
     * @param userSentence 用户例句对象
     * @return
     */
    boolean createUserSentence(UserSentence userSentence);

    /**
     * 给用户例句点赞
     *
     * @param userId     点赞人id
     * @param sentenceId 被点赞例句id
     * @return
     */
    boolean likeSentence(long userId, long sentenceId) throws ServiceException;

    /**
     * 获取指定例句用户是否点赞，传入例句id列表，返回当前用户是否点赞的布尔值
     * @param userId 用户id
     * @param sentenceIdList 例句id列表
     * @return
     */
    List<Boolean> getLikeByUser(long userId, List<Long> sentenceIdList);
}
