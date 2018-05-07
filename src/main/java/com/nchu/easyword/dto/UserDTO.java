package com.nchu.easyword.dto;

import com.nchu.easyword.dao.model.User;

/**
 * 2018-3-18 15:20:27
 *
 * @author xujw
 * 用户实体对应的数据传输对象,简化部分User类的属性,减少数据传输消耗
 */
public class UserDTO {

    private String account;

    private String password;

    private String nickName;

    private String headPortrait;

    private Boolean sex;

    private String email;

    private String phoneNumber;

    private Integer points;

    private Integer rank;

    private String signature;
    private Long expValue;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Long getExpValue() {
        return expValue;
    }

    public void setExpValue(Long expValue) {
        this.expValue = expValue;
    }

    /**
     * 从User对象转化为UserDTO对象
     *
     * @param user 要转化的User对象
     * @return 返回转化后的结果
     */
    public static UserDTO transFromUser(User user) {
        if (user == null) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setAccount(user.getAccount());
        userDTO.setPassword(user.getPassword());
        userDTO.setEmail(user.getEmail());
        userDTO.setHeadPortrait(user.getHeadportrait());
        userDTO.setRank(user.getRank());
        userDTO.setNickName(user.getNickname());
        userDTO.setPhoneNumber(user.getPhonenumber());
        userDTO.setPoints(user.getPoints());
        userDTO.setSex(user.getSex());
        userDTO.setSignature(user.getSignature());
        userDTO.setExpValue(user.getExpValue());
        return userDTO;
    }
}
