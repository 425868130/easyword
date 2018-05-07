package com.nchu.easyword.service.inface;

import com.nchu.easyword.dao.model.ReadNews;
import com.nchu.easyword.dao.model.User;
import com.nchu.easyword.exception.ServiceException;

/**
 * 新闻阅读记录相关业务类
 */
public interface ReadNewsService {
    int deleteByPrimaryKey(Long id);

    int insert(ReadNews record);

    int insertSelective(ReadNews record);

    ReadNews selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ReadNews record);

    int updateByPrimaryKey(ReadNews record);

    /**
     * 通过新闻id统计新闻阅读量
     *
     * @param newsId 新闻id
     * @return
     */
    long countViewNumById(long newsId);

    /**
     * 通过用户及新闻id 获取阅读记录，用于判断用户是否完成阅读
     *
     * @param userId 用户id
     * @param newsId 新闻id
     * @return
     */
    ReadNews selectByPrimaryUser(long userId, long newsId);

    /**
     * 完成新闻阅读，创建记录和通知
     *
     * @param newsId
     * @param user
     * @return
     */
    boolean finishRead(long newsId, User user) throws ServiceException;
}
