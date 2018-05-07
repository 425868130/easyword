package com.nchu.easyword.controller;

import com.nchu.easyword.dao.model.User;
import com.nchu.easyword.dto.ResponseDTO;
import com.nchu.easyword.dto.WordsDTO;
import com.nchu.easyword.exception.ServiceException;
import com.nchu.easyword.service.inface.UserSessionService;
import com.nchu.easyword.service.inface.WordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 2018-3-7 10:02:25
 *
 * @author xujw
 * 单词相关控制器
 */
@CrossOrigin(allowCredentials = "true")
@RestController
@RequestMapping("/words")
public class WordsController {

    @Autowired
    WordsService wordsService;
    @Autowired
    UserSessionService sessionService;

    @RequestMapping(value = "/searchByKeywordsEn/{keywords}", method = RequestMethod.GET)
    public ResponseDTO searchByKeywordsEn(@PathVariable String keywords, @RequestAttribute("responseDTO") ResponseDTO responseDTO) {
        return responseDTO.setData(wordsService.searchByKeywordsEn(keywords)).setMessage("单词查询");
    }

    /**
     * 获取最近一周的背单词统计数据
     *
     * @param request
     * @return
     * @throws ServiceException
     */
    @RequestMapping(value = "/getWeekRecord", method = RequestMethod.GET)
    public ResponseDTO getWeekRecord(HttpServletRequest request, @RequestAttribute("responseDTO") ResponseDTO responseDTO) {
        User user = sessionService.getUser(request);
        return responseDTO.setData(wordsService.getWeekRecordByUser(user.getId())).setMessage("背诵统计");
    }

    /**
     * 通过单词id 获取单词对象包括单词释义
     *
     * @param wordID 要获取的单词id
     * @return
     */
    @RequestMapping(value = "/getWordById/{wordID}", method = RequestMethod.GET)
    public ResponseDTO<WordsDTO> getWordById(@PathVariable("wordID") long wordID, @RequestAttribute("responseDTO") ResponseDTO responseDTO) {
        /*读取单词对象并转换为对应格式*/
        return responseDTO.setData(wordsService.getById(wordID)).setMessage("单词查询");
    }

    /**
     * 获取随机除单词id为excludeID的三条单词释义,用于每日任务的错误答案显示
     *
     * @param excludeID 要排除的单词id,即正确的单词释义
     * @return
     */
    @RequestMapping(value = "/getRandomMean/{excludeID}", method = RequestMethod.GET)
    public ResponseDTO getRandomMean(@PathVariable("excludeID") long excludeID, @RequestAttribute("responseDTO") ResponseDTO responseDTO) {
        return responseDTO.setData(wordsService.getRandomMean(excludeID)).setMessage("随机单词");
    }
}
