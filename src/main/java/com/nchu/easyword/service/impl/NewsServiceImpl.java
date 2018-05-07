package com.nchu.easyword.service.impl;

import com.nchu.easyword.dao.mapperInterface.NewsMapper;
import com.nchu.easyword.dao.model.News;
import com.nchu.easyword.dao.model.NewsWithBLOBs;
import com.nchu.easyword.service.inface.NewsService;
import com.nchu.easyword.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

/**
 * 2018-4-17 14:15:45
 *
 * @author xujw
 * 新闻相关业务类
 */

@Service
public class NewsServiceImpl implements NewsService {
    @Autowired
    SpiderService spiderService;
    @Autowired
    NewsMapper newsMapper;

    /**
     * 抓取新闻
     */
    @Override
    public void crawlNews() {
        /*抓取新闻前先清空之前的新闻*/
        newsMapper.clearAll();
        new Thread(() -> {
            try {
                spiderService.startCrawl(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * 创建新闻
     *
     * @param news 新闻对象
     * @return
     */
    @Transactional
    @Override
    public boolean createNews(NewsWithBLOBs news) {
        /*设置经验值等于单词数*/
        news.setExperience(news.getWordNum());
        /*设置积分点为单词数一半*/
        news.setPoint(news.getWordNum() / 2);
        news.setGmtCreate(DateUtil.getCurrentTimestamp());
        news.setGmtModified(DateUtil.getCurrentTimestamp());
        if (newsMapper.insertSelective(news) == 1) {
            return true;
        }
        return false;
    }

    /**
     * 分页获取新闻列表(不含新闻内容)
     *
     * @param page     页码
     * @param pageSize 页面大小
     * @param order    排序,默认按时间
     * @return
     */
    @Override
    public List<News> getNewsByPage(int page, int pageSize, String order) {
        if (order == null) {
            order = "gmt_create";
        }
        return newsMapper.getNewsByPage((page - 1) * pageSize, pageSize, order);
    }

    /**
     * 获取新闻总数条数,用于分页查询
     *
     * @return
     */
    @Override
    public long getNewsCount() {
        return newsMapper.getNewsCount();
    }

    /**
     * 通过id获取完整新闻信息
     *
     * @param id
     * @return
     */
    @Override
    public NewsWithBLOBs getNewsById(long id) {
        return newsMapper.selectByPrimaryKey(id);
    }

    /**
     * 更新新闻信息
     *
     * @param news
     * @return
     */
    @Override
    public boolean updateNews(NewsWithBLOBs news) {
        if (newsMapper.updateByPrimaryKeySelective(news) == 1) {
            return true;
        }
        return false;
    }

    /**
     * 通过id删除指定新闻
     *
     * @param id
     * @return
     */
    @Override
    public boolean deleteNewsById(long id) {
        if (newsMapper.deleteByPrimaryKey(id) == 1) {
            return true;
        }
        return false;
    }
}
