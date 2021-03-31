package com.jiong.www.service;

import com.jiong.www.dao.TilitiliDao;
import com.jiong.www.po.Event;
import com.jiong.www.po.EventGroup;
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
        // 用于接收dao层的返回值
        //封装user对象
        User user = new User();
        user.setLoginName(loginName);
        user.setLoginPassword(loginPassword);
        try {
            row = tilitiliDao.register(user);
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
            row=tilitiliDao.verifyUsername(loginName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row;
        //0不存在，1存在
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
    //验证用户的身份，吃瓜群众1管理员2游客3超管4
    public int verifyRole(int userId){
        int roleId=0;
        try {
            roleId=tilitiliDao.verifyRole(userId);
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
            row=tilitiliDao.verifyPassword(oldPassword,userId);
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
            row2=tilitiliDao.changePassword(user,userId);
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
            userQuery=tilitiliDao.queryUserInformation(userId);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userQuery;
    }
    //创建瓜圈
    public int createEventGroup(int userId,String eventGroupName,String eventGroupDescription){
        int row=0;
        // 用于接收dao层的返回值
        //封装eventGroup对象
        EventGroup eventGroup = new EventGroup();
        eventGroup.setEventGroupName(eventGroupName);
        eventGroup.setEventGroupDescription(eventGroupDescription);
        try {
            row=tilitiliDao.createEventGroup(userId,eventGroup);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row;
    }
    //验证瓜圈名是否存在
    public int verifyEventGroupName(String eventGroupName){
        int row=0;
        //默认0不存在
        try {
            row=tilitiliDao.verifyEventGroupName(eventGroupName);
            //0则无数据，1则有数据
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row;

    }
    //验证是否是该管理员管理的瓜圈
    public int verifyEventGroupOfAdmin(int userId,String eventGroupName){
        int row=0;
        //默认0不是管理员管理的瓜圈
        try {
            row=tilitiliDao.verifyEventGroupOfAdmin(userId,eventGroupName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row;
    }
    //删除瓜圈，同时在管理员所管理的数据删除关系
    public int deleteEventGroup(String deleteEventGroupName,int userId){
        int row=0;
        // 用于接收dao层的返回值
        try {
            row=tilitiliDao.deleteEventGroup(deleteEventGroupName,userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row;
    }
    //创建瓜
    public int createEvent(int userId,int eventGroupId,String eventName,String eventContent){
        int row=0;
        // 用于接收dao层的返回值
        //封装event对象
        Event event = new Event();
        event.setEventName(eventName);
        event.setEventContent(eventContent);
        try {
            row=tilitiliDao.createEvent(userId,eventGroupId,event);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row;
    }
    //验证瓜名是否存在
    public int verifyEventName(String eventGroupName){
        int row=0;
        //默认0不存在
        try {
            row=tilitiliDao.verifyEventName(eventGroupName);
            //1则无数据，0则有数据
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row;
    }

}
