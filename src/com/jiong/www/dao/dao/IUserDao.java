package com.jiong.www.dao.dao;

import com.jiong.www.po.User;

import java.io.InputStream;
import java.sql.Connection;

/**
 * @author Mono
 */
public interface IUserDao {
    /**
     * 注册，添加用户信息到用户表
     * @param user 注册的用户信息
     * @param conn 连接
     * @return 判断成功
     */
    int doRegister(Connection conn, User user);
    /**把新注册的用户加入到用户角色表，默认新注册只能为吃瓜群众即1
     * @param userId 用户id
     * @param conn 连接
     * @param roleId 角色
     * */
    void doInsertRole(Connection conn,int userId,int roleId);

    /**
     * 用用户名查用户id
     * @param userName 用户名
     * @return userId
     */
    int doQueryId(String userName);

    /**
     * 用于注册时验证该用户名是否存在
     * @param loginName 用户名
     * @return 判断存在
     */
    int verifyExist(String loginName);

    /**
     * 完善用户信息,实现了每次只改动某个信息，其他的保存为上次的值
     * @param user 用户信息
     * @return 判断成功
     */
    int perfectInformation(User user);

    /**
     * 是否要记住密码
     * @param user 用户信息
     */
    void isRememberPassword(User user);

    /**
     * 登录
     * @param loginName 登录用户名
     * @return 用户信息
     */
    User login(String loginName);

    /**
     * 验证用户的身份，吃瓜群众1管理员2游客3超管4
     * @param userId 用户的id
     * @return roleId
     */
    int verifyRole(int userId);

    /**
     * 验证要修改的密码
     * @param userId 用户id
     * @return 旧密码
     */
    String verifyPassword(int userId);

    /**
     * 修改密码
     * @param user 用户信息
     * @return 判断成功
     */
    int changePassword(User user);

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
     * 保存用户设置的头像文件到数据库
     * @param inputStream 头像的二进制文件
     * @param userId 用户id
     * @param conn 连接
     * @return 判断成功
     */
    int saveIcon(Connection conn,InputStream inputStream, int userId);

    /**
     * 保存用户设置的头像文件本地文件夹
     * @param userId 用户id
     * @param conn 连接
     * @return 头像文件
     */
    InputStream queryIcon(Connection conn,int userId);

    /**
     * 删除用户保存的头像文件
     * @param userId 用户id
     * @param conn 连接
     * @return 判断成功
     */
    int deleteIcon(Connection conn,int userId);
}
