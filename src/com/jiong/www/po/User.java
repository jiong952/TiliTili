package com.jiong.www.po;


import java.sql.Date;

/**
 * @author Mono
 */
public class User {

    private String loginName;
    //登录账号/用户名
    private String loginPassword;
    //登录密码
    private String userEmail;
    //用户邮箱
    private String iconAddress;
    //用户头像的地址
    private String userNickname;
    //用户的昵称
    private int userGender;
    //用户的性别
    //0为女，1为男,默认数据库为2，即没选择
    private Date userBirthday;
    //用户的生日 java.sql.Date这个包才可以直接放进数据库
    //存进去：用StringBuilder按照格式拼接string，把string转化为java.sql.Date可以直接存进去
    //取出来：把java.sql.Date用Calendar类来获取年月日
    private String userDescription;
    //用户的个人说明


    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getIconAddress() {
        return iconAddress;
    }

    public void setIconAddress(String iconAddress) {
        this.iconAddress = iconAddress;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public int getUserGender() {
        return userGender;
    }

    public void setUserGender(int userGender) {
        this.userGender = userGender;
    }

    public Date getUserBirthday() {
        return userBirthday;
    }

    public void setUserBirthday(Date userBirthday) {
        this.userBirthday = userBirthday;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }
}
