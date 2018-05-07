package com.nchu.easyword.service.inface;

import com.nchu.easyword.dao.model.PersonalGlossary;
import com.nchu.easyword.dto.PageViewDTO;
import com.nchu.easyword.exception.ServiceException;

import java.util.List;

/**
 * 2018-4-1 12:03:45
 *
 * @author xujw
 * 生词本相关业务模块接口
 */
public interface GlossaryService {
    /**
     * 向生词本中添加单词
     *
     * @param word_id 要添加的单词id
     * @param userId  要添加生词的用户id
     * @return 操作结果
     */
    boolean addWord(long word_id, long userId) throws ServiceException;

    /**
     * 移除生词本中的指定单词
     *
     * @param word_id 要移除的单词id
     * @param userId  用户id
     * @return 操作结果
     */
    boolean removeWord(long word_id, long userId) throws ServiceException;

    /**
     * 分页获取用户生词表内容
     *
     * @param page       页码
     * @param pageSize   每页大小
     * @param userId     所属用户id
     * @param isRemember 是否记住
     * @return 生词表的分页数据
     */
    PageViewDTO<PersonalGlossary> getGlossaryByPage(int page, int pageSize, long userId, boolean isRemember);

    /**
     * 记住单词功能:修改生词表中单词为已记住
     *
     * @param record_id 要修改的生词表记录id
     * @param userId  用户id
     * @return 返回操作结果
     */
    boolean rememberWord(long record_id, long userId);

    /**
     * 获取用户近期添加的前10个生词
     *
     * @param userId 要获取的用户id
     * @return 生词列表
     */
    List<PersonalGlossary> recentGlossary(long userId);
}
