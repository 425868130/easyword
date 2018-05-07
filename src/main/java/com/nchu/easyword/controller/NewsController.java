package com.nchu.easyword.controller;

import com.nchu.easyword.dao.model.News;
import com.nchu.easyword.dao.model.NewsWithBLOBs;
import com.nchu.easyword.dao.model.User;
import com.nchu.easyword.dto.PageViewDTO;
import com.nchu.easyword.dto.ResponseDTO;
import com.nchu.easyword.exception.ServiceException;
import com.nchu.easyword.exception.StatusCode;
import com.nchu.easyword.service.inface.NewsService;
import com.nchu.easyword.service.inface.ReadNewsService;
import com.nchu.easyword.service.inface.UserService;
import com.nchu.easyword.service.inface.UserSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 2018-4-1 11:55:53
 *
 * @author xujw
 * 新闻相关控制器
 */
@RestController
@CrossOrigin(allowCredentials = "true")
@RequestMapping("/news")
public class NewsController {
    @Autowired
    NewsService newsService;
    @Autowired
    UserSessionService userSessionService;
    @Autowired
    ReadNewsService readNewsService;
    @Autowired
    UserService userService;

    /**
     * 抓取新闻
     */
    @RequestMapping(value = "/crawlNews", method = RequestMethod.GET)
    public void crawlNews() {
        newsService.crawlNews();
    }

    /**
     * 创建新闻
     *
     * @return
     */
    @RequestMapping(value = "/createNews", method = RequestMethod.POST)
    ResponseDTO<Boolean> createNews(@RequestBody NewsWithBLOBs news, HttpServletRequest request, @RequestAttribute("responseDTO") ResponseDTO responseDTO) {
        responseDTO.setMessage("创建新闻");
        responseDTO.setData(newsService.createNews(news));
        return responseDTO;
    }

    /**
     * 分页获取新闻列表(不含新闻内容)
     *
     * @param page     页码
     * @param pageSize 页面大小
     * @param order    排序,默认按时间
     * @return
     */
    @RequestMapping(value = "/getNewsByPage", method = RequestMethod.GET)
    ResponseDTO<PageViewDTO> getNewsByPage(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize, @RequestParam("order") String order, @RequestAttribute("responseDTO") ResponseDTO responseDTO) {
        responseDTO.setMessage("新闻列表");
        PageViewDTO<News> newsPageViewDTO = new PageViewDTO(page, pageSize,
                newsService.getNewsCount(), newsService.getNewsByPage(page, pageSize, order), "新闻列表");
        return responseDTO.setData(newsPageViewDTO);
    }

    /**
     * 通过id获取完整新闻信息
     *
     * @param news_id 要获取的新闻id
     * @return
     */
    @RequestMapping(value = "/getNewsById/{news_id}", method = RequestMethod.GET)
    ResponseDTO<NewsWithBLOBs> getNewsById(@PathVariable("news_id") long news_id, @RequestAttribute("responseDTO") ResponseDTO responseDTO) {
        return responseDTO.setData(newsService.getNewsById(news_id)).setMessage("新闻详情");
    }

    /**
     * 更新新闻信息 (后台管理接口)
     *
     * @param news
     * @return
     */
    @RequestMapping(value = "/updateNews", method = RequestMethod.PUT)
    ResponseDTO<Boolean> updateNews(@RequestBody NewsWithBLOBs news, HttpServletRequest request, @RequestAttribute("responseDTO") ResponseDTO responseDTO) {
        return responseDTO.setData(newsService.updateNews(news)).setMessage("新闻更新");
    }

    /**
     * 通过id删除指定新闻(后台管理接口)
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/deleteNewsById", method = RequestMethod.DELETE)
    ResponseDTO<Boolean> deleteNewsById(@RequestParam("id") long id, HttpServletRequest request, @RequestAttribute("responseDTO") ResponseDTO responseDTO) throws ServiceException {
        //User user = userSessionService.getUser(request);
        /*TODO 身份校验*/
        return responseDTO.setData(newsService.deleteNewsById(id)).setMessage("删除新闻");
    }

    /**
     * 完成文章阅读
     *
     * @param newsId  阅读完成的新闻id
     * @param request
     * @return
     */
    @RequestMapping(value = "/readFinish/{newsId}", method = RequestMethod.POST)
    ResponseDTO<Boolean> readFinish(@PathVariable("newsId") long newsId, HttpServletRequest request, @RequestAttribute("responseDTO") ResponseDTO responseDTO) throws ServiceException {
        User user = userSessionService.getUser(request);
        System.out.println("完成阅读");
        /*判断用户是否已经阅读过该文章*/
        if (readNewsService.selectByPrimaryUser(user.getId(), newsId) != null) {
            throw new ServiceException(StatusCode.REPEAT, "你已经读过该新闻,重复阅读无法获取积分!");
        }
        if (readNewsService.finishRead(newsId, user)) {
            userSessionService.addUser(userService.getUserInfoByAccount(user.getAccount()), request);
            responseDTO.setUserUpdate(true).setData(true);
        } else {
            responseDTO.setData(false);
        }
        return responseDTO.setMessage("新闻阅读");
    }
}
