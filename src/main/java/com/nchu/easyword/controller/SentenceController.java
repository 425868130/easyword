package com.nchu.easyword.controller;

import com.nchu.easyword.dao.model.User;
import com.nchu.easyword.dao.model.UserSentence;
import com.nchu.easyword.dao.model.WordsSentence;
import com.nchu.easyword.dto.PageViewDTO;
import com.nchu.easyword.dto.ResponseDTO;
import com.nchu.easyword.exception.ServiceException;
import com.nchu.easyword.service.inface.SentenceService;
import com.nchu.easyword.service.inface.UserService;
import com.nchu.easyword.service.inface.UserSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 2018-4-9 21:15:42
 * 例句相关控制器
 *
 * @author xujw
 */
@RestController
@CrossOrigin(allowCredentials = "true")
@RequestMapping("/sentence")
public class SentenceController {
    @Autowired
    SentenceService sentenceService;
    @Autowired
    UserSessionService userSessionService;
    @Autowired
    UserService userService;
    /*阿里云oss图片压缩样式*/
    private final static String OssPicStyle = "?x-oss-process=style/head-img-zip";

    /**
     * 获取单词例句
     *
     * @param keywords 要获取例句的单词
     * @return
     */
    @RequestMapping(value = "/getNetSentences/{keywords}", method = RequestMethod.GET)
    public ResponseDTO<List<WordsSentence>> getWordsExample(@PathVariable String keywords, @RequestAttribute("responseDTO") ResponseDTO responseDTO) throws ServiceException {
        return responseDTO.setData(sentenceService.getNetSentences(keywords)).setMessage("单词网络例句");
    }

    /**
     * 分页获取用户例句
     *
     * @param word_id  要获取例句的单词id
     * @param page     页码
     * @param pageSize 每页记录条数
     * @param isCheck  是否通过审核
     * @return 用户例句信息
     */
    @RequestMapping(value = "/getUserSentences", method = RequestMethod.GET)
    public ResponseDTO<PageViewDTO<UserSentence>> getUserSentences(
            @RequestParam("word_id") long word_id, @RequestParam("page") int page, @RequestParam("pageSize") int pageSize, @RequestParam("isCheck") boolean isCheck,
            @RequestAttribute("responseDTO") ResponseDTO responseDTO) {
        /*创建响应数据传输对象*/
        responseDTO.setMessage("单词用户例句");
        /*创建分页数据对象*/
        PageViewDTO<UserSentence> userSentencePageViewDTO = new PageViewDTO(
                page, pageSize, sentenceService.countWordSentences(word_id, isCheck),
                sentenceService.getUserSentencesByWord(page, pageSize, word_id, isCheck), "用户例句");
        return responseDTO.setData(userSentencePageViewDTO);
    }

    /**
     * 创建用户例句
     *
     * @param userSentence 新的用户例句
     * @param request      请求对象
     * @return 操作结果
     */
    @RequestMapping(value = "/createUserSentence", method = RequestMethod.POST)
    public ResponseDTO<Boolean> createUserSentence(@RequestBody UserSentence userSentence, HttpServletRequest request
            , @RequestAttribute("responseDTO") ResponseDTO responseDTO) throws ServiceException {
        User user = userSessionService.getUser(request);
        userSentence.setUserId(user.getId());
        userSentence.setUserNick(user.getNickname());
        /*应用图片压缩样式,提高头像加载速度*/
        userSentence.setUserHeadportrait(user.getHeadportrait() + OssPicStyle);
        responseDTO.setMessage("创建用户例句");
        Boolean res = sentenceService.createUserSentence(userSentence) ? true : false;
        responseDTO.setData(res);
        if (res) {
            /*发布例句成功后增加用户100积分*/
            user.setPoints(user.getPoints() + 100);
            userService.updateUserInfo(user);
            userSessionService.addUser(user, request);
            responseDTO.setUserUpdate(true);
        }
        return responseDTO;
    }

    /**
     * 获取指定例句用户是否点赞，传入例句id列表，返回当前用户是否点赞的布尔值
     *
     * @param sentenceIdList 例句id列表
     * @param request        请求对象
     * @return
     */
    @RequestMapping(value = "/getLikeByUser", method = RequestMethod.GET)
    public ResponseDTO<List> getLikeByUser(@RequestParam("sentenceIdList[]") Long[] sentenceIdList, HttpServletRequest request
            , @RequestAttribute("responseDTO") ResponseDTO responseDTO) {
        User user = userSessionService.getUser(request);
        return responseDTO.setData(sentenceService.getLikeByUser(user.getId(), Arrays.asList(sentenceIdList))).setMessage("点赞列表");
    }

    /**
     * 用户例句点赞
     *
     * @param params  要点赞的例句id参数
     *                <p>前端 request参数为Content-Type:application/json;charset=UTF-8</p>
     *                <p>参数必须为JSON对象格式 { sentenceId:例句id } </p>
     * @param request 请求对象
     * @return 操作结果
     */
    @RequestMapping(value = "/likeSentence", method = RequestMethod.POST)
    public ResponseDTO<Boolean> likeSentence(@RequestBody Map<String, Long> params, HttpServletRequest request
            , @RequestAttribute("responseDTO") ResponseDTO responseDTO) throws ServiceException {
        User user = userSessionService.getUser(request);
        return responseDTO.setData(sentenceService.likeSentence(user.getId(), params.get("sentenceId"))).setMessage("例句点赞");
    }
}
