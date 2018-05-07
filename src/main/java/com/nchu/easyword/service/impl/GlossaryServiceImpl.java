package com.nchu.easyword.service.impl;

import com.nchu.easyword.dao.mapperInterface.PersonalGlossaryMapper;
import com.nchu.easyword.dao.model.PersonalGlossary;
import com.nchu.easyword.dao.model.Words;
import com.nchu.easyword.dto.PageViewDTO;
import com.nchu.easyword.dto.WordsDTO;
import com.nchu.easyword.exception.ServiceException;
import com.nchu.easyword.exception.StatusCode;
import com.nchu.easyword.service.inface.GlossaryService;
import com.nchu.easyword.service.inface.WordsService;
import com.nchu.easyword.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 2018-4-1 12:03:45
 *
 * @author xujw
 * 生词本相关业务模块接口实现类
 */
@Service
public class GlossaryServiceImpl implements GlossaryService {

    @Autowired
    PersonalGlossaryMapper glossaryMapper;
    @Autowired
    WordsService wordsService;

    /**
     * 向生词本中添加单词
     *
     * @param word_id 要添加的单词id
     * @param userId  要添加生词的用户id
     * @return 操作结果
     */
    @Override
    public boolean addWord(long word_id, long userId) throws ServiceException {
        /*查询数据库中是否有该单词*/
        WordsDTO word = wordsService.getById(word_id);
        if (word == null) {
            throw new ServiceException(StatusCode.REQUEST_FAILED, "要添加的单词不存在!");
        }
        /*查询当前用户生词表中时否有该单词*/
        if (glossaryMapper.selectByWordID(word_id, userId) != null) {
            throw new ServiceException(StatusCode.REPEAT, "该单词已经添加到生词本,请勿重复操作");
        }
        /*创建一个生词对象并存入数据库*/
        PersonalGlossary personalGlossary = new PersonalGlossary();
        personalGlossary.setWord(word.getWord());
        personalGlossary.setWordId(word_id);
        personalGlossary.setUserId(userId);
        personalGlossary.setIsremembered(false);
        personalGlossary.setGmtCreate(DateUtil.getCurrentTimestamp());
        personalGlossary.setGmtModified(DateUtil.getCurrentTimestamp());
        try {
            glossaryMapper.insertSelective(personalGlossary);
        } catch (Exception e) {
            throw new ServiceException(StatusCode.SYSTEM_ERROR, "系统异常,添加失败,请重试!");
        }
        return true;
    }

    /**
     * 移除生词本中的指定单词
     *
     * @param word_id 要移除的单词id
     * @param userId  用户id
     * @return 操作结果
     */
    @Override
    public boolean removeWord(long word_id, long userId) throws ServiceException {
        if (glossaryMapper.removeWord(word_id, userId) == 1) {
            return true;
        }
        throw new ServiceException(StatusCode.REQUEST_FAILED, "生词本中没有该单词!");
    }

    /**
     * 分页获取用户生词表内容
     *
     * @param page       页码
     * @param pageSize   每页大小
     * @param userId     所属用户id
     * @param isRemember 是否记住
     * @return 生词表的分页数据
     */
    @Override
    public PageViewDTO<PersonalGlossary> getGlossaryByPage(int page, int pageSize, long userId, boolean isRemember) {
        if (page > 0 && pageSize > 0) {
            return new PageViewDTO(page, pageSize, glossaryMapper.countWordByUser(userId, isRemember),
                    glossaryMapper.getGlossaryByPage((page - 1) * pageSize, pageSize, userId, isRemember), "用户生词表");
        }
        return null;
    }

    /**
     * 记住单词功能:修改生词表中单词为已记住
     *
     * @param record_id 要修改的生词表记录id
     * @param userId    用户id
     * @return 返回操作结果
     */
    @Override
    public boolean rememberWord(long record_id, long userId) {
        PersonalGlossary personalGlossary = new PersonalGlossary();
        personalGlossary.setId(record_id);
        personalGlossary.setIsremembered(true);
        try {
            glossaryMapper.updateByPrimaryKeySelective(personalGlossary);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 获取用户近期添加的前10个生词
     *
     * @param userId 要获取的用户id
     * @return 生词列表
     */
    @Override
    public List<PersonalGlossary> recentGlossary(long userId) {
        return glossaryMapper.recentGlossary(userId);
    }
}
