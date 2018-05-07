package com.nchu.easyword.service.inface;

import com.nchu.easyword.dao.model.News;
import com.nchu.easyword.dao.model.NewsWithBLOBs;

import java.util.List;

public interface NewsService {
    /**
     * 抓取新闻
     */
    void crawlNews();

    /**
     * 创建新闻
     *
     * @return
     */
    boolean createNews(NewsWithBLOBs news);

    /**
     * 分页获取新闻列表(不含新闻内容)
     *
     * @param page     页码
     * @param pageSize 页面大小
     * @param order    排序,默认按时间
     * @return
     */
    List<News> getNewsByPage(int page, int pageSize, String order);

    /**
     * 获取新闻总数条数,用于分页查询
     *
     * @return
     */
    long getNewsCount();

    /**
     * 通过id获取完整新闻信息
     *
     * @param id
     * @return
     */
    NewsWithBLOBs getNewsById(long id);

    /**
     * 更新新闻信息
     *
     * @param news
     * @return
     */
    boolean updateNews(NewsWithBLOBs news);

    /**
     * 通过id删除指定新闻
     *
     * @param id
     * @return
     */
    boolean deleteNewsById(long id);
}
