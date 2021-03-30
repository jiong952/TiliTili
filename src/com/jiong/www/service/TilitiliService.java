package com.jiong.www.service;

import com.jiong.www.dao.TilitiliDao;
import com.jiong.www.po.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mono
 */
//注册
public class TilitiliService {
    private TilitiliDao tilitiliDao = new TilitiliDao();

    //放在类中，才能验证是不是同一个人
    public int register(String loginName,String loginPassword) {
        int row=0;
        int row1=0;
        // 用于接收dao层的返回值
        //封装user对象
        User user = new User();
        user.setLoginName(loginName);
        user.setLoginPassword(loginPassword);
        try {
            row = tilitiliDao.register(user);
            //注册，添加信息到用户表
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            row1=tilitiliDao.addRole(user);
            //添加到用户角色表
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(row1==0){
            row =0;
        }
        //添加判断如果添加到用户角色表失败，则整个过程失败
        //处理dao层的异常
        return row;
        //返回结果集
    }
    //用于注册时验证该用户名是否存在
    public int verifyUsername(String loginName){
        int row=0;
        try {
            row=tilitiliDao.verifyUsername(loginName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row;
        //不存在该用户名重复则row为1抛出
    }
    //完善用户信息
    public int perfectInformation(String userEmail,String userNickName,int userGender,String userDescription,int userId){
        int row =0;
        // 用于接收dao层的返回值
        //封装对象
        User user = new User();
        user.setUserEmail(userEmail);
        user.setUserNickname(userNickName);
        user.setUserGender(userGender);
        user.setUserDescription(userDescription);
        try {
            row=tilitiliDao.perfectInformation(user,userId);
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
             userId= tilitiliDao.login(loginName, loginPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //处理dao层的异常
        return userId;
    }
    //验证要修改的密码
    public int verifyPassword(String oldPassword,int userId){
        int row1=0;
        // 用于接收dao层的返回值
        //封装对象
        try {
            row1=tilitiliDao.verifyPassword(oldPassword,userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //处理dao层的异常
        return row1;
        //返回结果集
    }
    //修改密码
    public int changePassword(String newPassword,int userId){
        int row2=0;
        // 用于接收dao层的返回值
        User user = new User();
        user.setLoginPassword(newPassword);
        try {
            row2=tilitiliDao.changePassword(user,userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row2;
    }
    //查询用户的个人信息
    public List queryUserInformation(int userId){
        List<Object> arr = null;
        //用集合来存数据
        try {
            arr=tilitiliDao.queryUserInformation(userId);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return arr;
    }

}
