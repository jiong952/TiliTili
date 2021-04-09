package com.jiong.www.service;

import com.jiong.www.dao.UserDao;
import com.jiong.www.po.User;

import java.sql.Date;
import java.sql.SQLException;

public class UserService {
    private UserDao userDao = new UserDao();
    //放在类中，才能验证是不是同一个人
    public int register(String loginName,String loginPassword) {
        int row=0;
        // 用于接收dao层的返回值
        //封装user对象
        User user = new User();
        user.setLoginName(loginName);
        user.setLoginPassword(loginPassword);
        try {
            row = userDao.register(user);
            //注册，添加信息到用户表 把新注册的用户加入到用户角色表，默认新注册只能为吃瓜群众即1
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //处理dao层的异常
        return row;
        //返回结果集
    }
    //用于注册时验证该用户名是否存在
    public int verifyUsername(String loginName){
        int row=0;
        try {
            row=userDao.verifyUsername(loginName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row;
        //0不存在，1存在
    }
    //完善用户信息
    public int perfectInformation(String userEmail, String userNickName, int userGender, String userDescription, int userId, Date userBirthday){
        int row =0;
        // 用于接收dao层的返回值
        //封装对象
        User user = new User();
        user.setUserEmail(userEmail);
        user.setUserNickname(userNickName);
        user.setUserGender(userGender);
        user.setUserDescription(userDescription);
        user.setUserBirthday(userBirthday);
        try {
            row=userDao.perfectInformation(user,userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //处理dao层的异常
        return row;
        //返回结果集
    }
    //登录
    public int login(String loginName,String loginPassword){
        // 用于接收dao层的返回值
        int userId=0;
        //用户的id
        try {
            userId= userDao.login(loginName, loginPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //处理dao层的异常
        return userId;
    }
    //验证用户的身份，吃瓜群众1管理员2游客3超管4
    public int verifyRole(int userId){
        int roleId=0;
        try {
            roleId=userDao.verifyRole(userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roleId;
    }
    //验证要修改的密码
    public int verifyPassword(String oldPassword,int userId){
        int row=0;
        // 用于接收dao层的返回值
        //封装对象
        try {
            row=userDao.verifyPassword(oldPassword,userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //处理dao层的异常
        return row;
        //返回结果集
    }
    //修改密码
    public int changePassword(String newPassword,int userId){
        int row2=0;
        // 用于接收dao层的返回值
        User user = new User();
        user.setLoginPassword(newPassword);
        try {
            row2=userDao.changePassword(user,userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row2;
    }
    //查询用户的个人信息
    public User queryUserInformation(int userId){
        User userQuery = new User();
        //用集合来存数据
        try {
            userQuery=userDao.queryUserInformation(userId);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userQuery;
    }
}
