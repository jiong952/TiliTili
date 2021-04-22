package com.jiong.www.service.service;

import com.jiong.www.po.User;

import java.io.InputStream;
import java.sql.Date;

/**
 * @author Mono
 */
public interface IUserService {
    /**
     * @param loginName 用户名
     * @param loginPassword 密码
     * @param roleId 角色id
     * @return 验证判断
     */
    int register(String loginName, String loginPassword,int roleId);

    /**
     * 用于注册时验证该用户名是否存在
     * @param loginName 用户名
     * @return 验证判断
     */
    int verifyUsername(String loginName);

    /**
     * 完善用户信息
     * @param userEmail 邮箱
     * @param userNickName 昵称
     * @param userGender 性别
     * @param userDescription 用户自我简介
     * @param userId 用户id
     * @param userBirthday 生日
     * @return 判断成功
     */
    int perfectInformation(String userEmail, String userNickName, int userGender, String userDescription, int userId, Date userBirthday);

    /**
     * 是否要记住密码
     * @param isRememberPassword 是否记住密码
     * @param userId 用户id
     */
    void isRememberPassword(int isRememberPassword, int userId);

    /**
     * 登录
     * @param loginName 用户名
     * @param loginPassword 登录密码
     * @param isRememberPassword 上一次是否记住密码,判断是否要进行加密
     * @return 判断成功
     */
    int login(String loginName, String loginPassword,int isRememberPassword);

    /**
     * 验证用户的身份，吃瓜群众1管理员2游客3超管4
     * @param userId 用户id
     * @return roleId
     */
    int verifyRole(int userId);

    /**
     * 验证要修改的密码
     * @param oldPassword 旧密码
     * @param userId 用户id
     * @return 判断正确
     */
    int verifyPassword(String oldPassword, int userId);

    /**
     * 修改密码
     * @param newPassword 新密码
     * @param userId 用户id
     * @return 判断成功
     */
    int changePassword(String newPassword, int userId);

    /**
     * 查询用户的个人信息
     * @param userId 用户id
     * @return 用户信息
     */
    User queryUserInformation(int userId);

    /**
     * 查看是否记住密码，是的话，把密码返回
     * @param loginName 用户名
     * @return 用户信息
     */
    User isRememberPassword(String loginName);

    /**
     * 保存用户设置的头像文件到数据库，把该二进制文件存到特定文件夹
     * @param inputStream 二进制文件
     * @param userId 用户id
     * @return 判断成功
     */
    int saveIcon(InputStream inputStream, int userId);

    /**
     * 删除用户保存的头像文件
     * @param userId 用户id
     * @return 判断成功
     */
    boolean deleteIcon(int userId);
}
