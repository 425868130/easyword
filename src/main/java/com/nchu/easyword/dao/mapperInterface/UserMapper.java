package com.nchu.easyword.dao.mapperInterface;

import com.nchu.easyword.dao.model.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {

    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    /**
     * 通过账号获取用户信息
     *
     * @param account 要查询的用户账号
     * @return 用户对象
     */
    User selectByAccount(String account);

    /**
     * 用户登录校验
     *
     * @param account  要校验的账号
     * @param password 要检验的账号密码
     * @return 如果符合返回账号详细信息
     * 对于查询参数有多个的情况，要使用@Param注解指定参数名称
     */
    User loginCheck(@Param("account") String account, @Param("password") String password);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}