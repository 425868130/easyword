package com.nchu.easyword.controller;

import com.nchu.easyword.dao.model.PersonalGlossary;
import com.nchu.easyword.dao.model.User;
import com.nchu.easyword.dto.PageViewDTO;
import com.nchu.easyword.dto.ResponseDTO;
import com.nchu.easyword.exception.ServiceException;
import com.nchu.easyword.exception.StatusCode;
import com.nchu.easyword.service.inface.GlossaryService;
import com.nchu.easyword.service.inface.UserSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 2018-4-1 11:39:04
 *
 * @author xujw
 * 用户生词表控制器
 */
@RestController
@CrossOrigin(allowCredentials = "true")
@RequestMapping("/glossary")
public class GlossaryController {
    @Autowired
    GlossaryService glossaryService;
    @Autowired
    UserSessionService sessionService;

    /**
     * 添加单词到用户生词表
     *
     * @param word_Id 要添加的单词
     * @param request http请求对象,用于判断用户在线状态
     * @return 返回添加结果, 成功为true
     */
    @RequestMapping(value = "/addToGlossary/{word_Id}", method = RequestMethod.PUT)
    public ResponseDTO addToGlossary(@PathVariable("word_Id") long word_Id, HttpServletRequest request, @RequestAttribute("responseDTO") ResponseDTO responseDTO) throws ServiceException {
        if (glossaryService.addWord(word_Id, sessionService.getUser(request).getId())) {
            return responseDTO.setData(true);
        }
        return responseDTO.setData(false);
    }

    /**
     * 从用户生词表中移除单词
     *
     * @param word_id 要删除的单词id
     * @param request http请求对象,用于判断用户在线状态
     * @return 操作结果
     */
    @RequestMapping(value = "/removeWord/{word_id}", method = RequestMethod.DELETE)
    public ResponseDTO removeWord(@PathVariable("word_id") long word_id, HttpServletRequest request, @RequestAttribute("responseDTO") ResponseDTO responseDTO) throws ServiceException {
        if (glossaryService.removeWord(word_id, sessionService.getUser(request).getId())) {
            return responseDTO.setData(true);
        }
        return responseDTO.setData(false);
    }

    /**
     * 将指定生词表中的生词设置为已记住状态
     *
     * @param record_id 要修改的生词表记录id
     * @param request   http请求对象,用于判断用户在线状态
     * @return 操作结果
     */
    @RequestMapping(value = "/rememberWord/{record_id}", method = RequestMethod.PUT)
    public ResponseDTO rememberWord(@PathVariable("record_id") long record_id, @RequestAttribute("responseDTO") ResponseDTO responseDTO, HttpServletRequest request) throws ServiceException {
        User user = sessionService.getUser(request);
        if (glossaryService.rememberWord(record_id, user.getId())) {
            return responseDTO.setData(true);
        }
        return responseDTO.setData(false);
    }

    /**
     * 以分页方式获取当前用户的生词表
     *
     * @param page     页码
     * @param pageSize 每页大小
     * @param request  http请求对象,用于判断用户在线状态
     * @return
     */
    @RequestMapping(value = "/personalGlossaryPage", method = RequestMethod.GET)
    public ResponseDTO<PageViewDTO<PersonalGlossary>> getPersonalGlossary(
            @RequestParam("page") int page, @RequestParam("pageSize") int pageSize, @RequestParam("isRemember") boolean isRemember, HttpServletRequest request, @RequestAttribute("responseDTO") ResponseDTO responseDTO) {
        User user = sessionService.getUser(request);
        responseDTO.setMessage("用户生词表");
        responseDTO.setData(glossaryService.getGlossaryByPage(page, pageSize, user.getId(), isRemember));
        return responseDTO;
    }

    /**
     * 获取用户最近添加的生词表时间排序前10条
     *
     * @param request
     * @return
     * @throws ServiceException
     */
    @RequestMapping(value = "/recentGlossary", method = RequestMethod.GET)
    public ResponseDTO<List<PersonalGlossary>> recentGlossary(HttpServletRequest request, @RequestAttribute("responseDTO") ResponseDTO responseDTO) throws ServiceException {
        User user = sessionService.getUser(request);
        responseDTO.setMessage("最新生词");
        responseDTO.setData(glossaryService.recentGlossary(user.getId()));
        responseDTO.setStatusCode(StatusCode.SUCCESS);
        return responseDTO;
    }
}
