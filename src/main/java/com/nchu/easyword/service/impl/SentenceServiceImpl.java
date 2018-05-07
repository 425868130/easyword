package com.nchu.easyword.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nchu.easyword.dao.mapperInterface.LikeSentenceMapper;
import com.nchu.easyword.dao.mapperInterface.UserSentenceMapper;
import com.nchu.easyword.dao.model.LikeSentence;
import com.nchu.easyword.dao.model.UserSentence;
import com.nchu.easyword.dao.model.WordsSentence;
import com.nchu.easyword.exception.ServiceException;
import com.nchu.easyword.exception.StatusCode;
import com.nchu.easyword.service.inface.NotificationService;
import com.nchu.easyword.service.inface.SentenceService;
import com.nchu.easyword.utils.DateUtil;
import com.nchu.easyword.utils.HttpsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

/**
 * 2018-4-10 08:51:02
 *
 * @author xujw
 * 单词例句相关业务逻辑接口
 */
@Service
public class SentenceServiceImpl implements SentenceService {
    /*扇贝单词API地址*/
    private static final String ShanbayApi = "https://api.shanbay.com/bdc";
    @Autowired
    UserSentenceMapper sentenceMapper;

    @Autowired
    LikeSentenceMapper likeSentenceMapper;
    @Autowired
    NotificationService notificationService;

    /**
     * 通过网络获取第三方单词例句
     *
     * @param keywords 要获取例句的单词
     * @return 例句
     */
    @Override
    public List<WordsSentence> getNetSentences(String keywords) throws ServiceException {
        String tempWord = HttpsUtil.httpsRequest(ShanbayApi + "/search/?word=" + keywords, "GET", null);
        /*通过正则表达式提取出对应单词在扇贝网上的id值,用于进行下一步获取例句*/
        String reg = "\"id\":\\s?([0-9]+)\\s?,";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(tempWord);
        if (!matcher.find()) {
            throw new ServiceException(StatusCode.REQUEST_FAILED, "查询的单词不存在");
        }
        /*使用上一步的wordsID获取单词例句*/
        String wordsExample = HttpsUtil.httpsRequest(ShanbayApi + "/example/?vocabulary_id=" + matcher.group(1) + "&type=sys", "GET", null);
        JSONObject jsonObject = JSON.parseObject(wordsExample);
        List<WordsSentence> wordsExampleList;
        /*扇贝网例句api返回的例句被包装在json对象的data数据中，这里利用fastJson提取出需要的字段数据*/
        wordsExampleList = JSON.parseArray(jsonObject.getJSONArray("data").toJSONString(), WordsSentence.class);
        return wordsExampleList;
    }

    /**
     * 通过单词获取用户自定义例句(按点赞数倒序)
     *
     * @param page     页码
     * @param pageSize 页面记录条数
     * @param word_Id  要获取例句的单词
     * @param isCheck  是否通过审核
     * @return 例句列表
     */
    @Override
    public List<UserSentence> getUserSentencesByWord(int page, int pageSize, long word_Id, boolean isCheck) {
        return sentenceMapper.getUserSentencesByWord((page - 1) * pageSize, pageSize, word_Id, isCheck);
    }

    /**
     * 统计指定单词有多少用户定义例句
     *
     * @param word_Id 要查询的单词id
     * @param isCheck 是否已审核
     * @return
     */
    @Override
    public long countWordSentences(long word_Id, boolean isCheck) {
        return sentenceMapper.SentencesByWordTotal(word_Id, isCheck);
    }

    /**
     * 添加一个用户例句
     *
     * @param userSentence 用户例句对象
     * @return
     */
    @Transactional
    @Override
    public boolean createUserSentence(UserSentence userSentence) {
        /*设置创建时间*/
        userSentence.setGmtCreate(DateUtil.getCurrentTimestamp());
        userSentence.setGmtModified(DateUtil.getCurrentTimestamp());
        if (sentenceMapper.insertSelective(userSentence) == 1) {
            /*还要生成一条用户的系统消息记录*/
            notificationService.newNotification(NotificationServiceImpl.genSimpleNotification(userSentence.getUserId(), "发布用户例句获得 100 点积分,请再接再厉哦!"));
            return true;
        }
        return false;
    }

    /**
     * 给用户例句点赞
     *
     * @param userId     点赞人id
     * @param sentenceId 被点赞例句id
     * @return
     */
    @Override
    public boolean likeSentence(long userId, long sentenceId) throws ServiceException {
        /*先判断用户是否已经点过赞*/
        LikeSentence record = likeSentenceMapper.getUserRecord(userId, sentenceId);
        if (record != null) {
            throw new ServiceException(StatusCode.REPEAT, "你已经点赞过该例句,请勿重复操作!");
        }
        /*未点过赞则创建新的点赞记录*/
        record = new LikeSentence(userId, sentenceId, DateUtil.getCurrentTimestamp());
        if (likeSentenceMapper.insertSelective(record) == 1) {
            UserSentence userSentence = sentenceMapper.selectByPrimaryKey(sentenceId);
            /*点赞数加1*/
            userSentence.setLikeNum(userSentence.getLikeNum() + 1);
            userSentence.setGmtModified(DateUtil.getCurrentTimestamp());
            sentenceMapper.updateByPrimaryKeySelective(userSentence);
            return true;
        }
        return false;
    }

    /**
     * 获取指定例句用户是否点赞，传入例句id列表，返回当前用户是否点赞的布尔值
     *
     * @param userId         用户id
     * @param sentenceIdList 例句id列表
     * @return
     */
    @Override
    public List<Boolean> getLikeByUser(long userId, List<Long> sentenceIdList) {
        List<Boolean> likeList = sentenceIdList.stream().map(sentenceId ->
                /*如果查询结果为null说明用户未点赞该条例句，否则已点赞*/
                sentenceMapper.userLikeRecord(userId, sentenceId) == 0 ? false : true
        ).collect(toList());
        return likeList;
    }
}
