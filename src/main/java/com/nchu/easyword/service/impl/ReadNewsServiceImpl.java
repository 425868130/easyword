package com.nchu.easyword.service.impl;

import com.nchu.easyword.dao.mapperInterface.ReadNewsMapper;
import com.nchu.easyword.dao.model.News;
import com.nchu.easyword.dao.model.ReadNews;
import com.nchu.easyword.dao.model.User;
import com.nchu.easyword.exception.ServiceException;
import com.nchu.easyword.service.inface.NewsService;
import com.nchu.easyword.service.inface.NotificationService;
import com.nchu.easyword.service.inface.ReadNewsService;
import com.nchu.easyword.service.inface.UserService;
import com.nchu.easyword.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReadNewsServiceImpl implements ReadNewsService {
    @Autowired
    ReadNewsMapper readNewsMapper;
    @Autowired
    NotificationService notificationService;
    @Autowired
    NewsService newsService;
    @Autowired
    UserService userService;

    @Transactional
    @Override
    public int deleteByPrimaryKey(Long id) {
        return readNewsMapper.deleteByPrimaryKey(id);
    }

    @Transactional
    @Override
    public int insert(ReadNews record) {
        return readNewsMapper.insert(record);
    }

    @Transactional
    @Override
    public int insertSelective(ReadNews record) {
        return readNewsMapper.insertSelective(record);
    }

    @Override
    public ReadNews selectByPrimaryKey(Long id) {
        return readNewsMapper.selectByPrimaryKey(id);
    }

    @Transactional
    @Override
    public int updateByPrimaryKeySelective(ReadNews record) {
        record.setGmtModified(DateUtil.getCurrentTimestamp());
        return readNewsMapper.updateByPrimaryKeySelective(record);
    }

    @Transactional
    @Override
    public int updateByPrimaryKey(ReadNews record) {
        record.setGmtModified(DateUtil.getCurrentTimestamp());
        return readNewsMapper.updateByPrimaryKey(record);
    }

    /**
     * 通过新闻id统计新闻阅读量
     *
     * @param newsId 新闻id
     * @return
     */
    @Override
    public long countViewNumById(long newsId) {
        return readNewsMapper.countViewNumById(newsId);
    }

    /**
     * 通过用户及新闻id 获取阅读记录，用于判断用户是否完成阅读
     *
     * @param userId 用户id
     * @param newsId 新闻id
     * @return
     */
    @Override
    public ReadNews selectByPrimaryUser(long userId, long newsId) {
        return readNewsMapper.selectByPrimaryUser(userId, newsId);
    }

    /**
     * 完成新闻阅读，创建记录和通知
     *
     * @param newsId
     * @param user
     * @return
     */
    @Transactional
    @Override
    public boolean finishRead(long newsId, User user) throws ServiceException {
        ReadNews readNews = new ReadNews();
        readNews.setUserId(user.getId());
        readNews.setNewsId(newsId);
        readNews.setGmtCreate(DateUtil.getCurrentTimestamp());
        readNews.setGmtCreate(DateUtil.getCurrentTimestamp());
        if (readNewsMapper.insertSelective(readNews) == 1) {
            int point = newsService.getNewsById(newsId).getPoint();
            /*还要生成一条用户的系统消息记录*/
            notificationService.newNotification(NotificationServiceImpl.genSimpleNotification(user.getId(), "完成新闻阅读获得" + point+" 点积分,请再接再厉哦!"));
            /*增加用户积分*/
            user.setPoints(user.getPoints() +point);
            userService.updateUserInfo(user);
            return true;
        }
        return false;
    }
}
