package com.jiong.www.dao;

import com.jiong.www.po.User;
import com.jiong.www.util.JdbcUtils;

import javax.sql.RowSet;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Mono
 */
public class TilitiliDao {
    //注册，添加用户信息到用户表
    public int register(User user) throws SQLException {
        Connection conn = JdbcUtils.getConnection();
        int row=0;
        String sql ="INSERT INTO `user` (`login_name`,`login_password`,`user_nickname`) VALUES(?,?,?)";
        //`login_name`加了主键约束，在数据库设计上可以防止重名
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1,user.getLoginName());
        ps.setString(2,user.getLoginPassword());
        ps.setString(3,user.getLoginName());
        //昵称默认为用户名
        row= ps.executeUpdate();
        //sql语句返回结果判断
        //row是返回值，用于判断
        JdbcUtils.release(conn,ps,null);
        //释放连接
        return row;
        //向上抛出到view层
    }
    //用于注册时验证该用户名是否存在
    public int verifyUsername(String loginName) throws SQLException {
        Connection connection = JdbcUtils.getConnection();
        int row=1;
        //用来抛出到view层做判断
        String sql1="SELECT *FROM `user` WHERE `login_name`=?";
        PreparedStatement ps0 = connection.prepareStatement(sql1);
        ps0.setString(1,loginName);
        ResultSet resultSet = ps0.executeQuery();
        if(resultSet.isBeforeFirst()){
            row=0;
            //表里有数据则row=0
        }
        return row;
        //抛出到view层判断
    }
    //把新注册的用户加入到用户角色表，默认新注册只能为吃瓜群众即1
    public int addRole(User user) throws SQLException {
        Connection conn = JdbcUtils.getConnection();
        String sql ="INSERT INTO `user_role`(`user_id`,`role_id`)VALUES((SELECT `user_id`FROM `user` WHERE `login_name`=?),1)";
        //1为提前设计的吃瓜群众对于的role_id
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1,user.getLoginName());
        int row1 = ps.executeUpdate();
        //sql语句返回结果判断
        //row是返回值，用于判断
        JdbcUtils.release(conn,ps,null);
        //释放连接
        return row1;
        //向上抛出到view层
    }
    //完善用户信息,实现了每次只改动一个信息，其他的保存为上次的值
    public int perfectInformation(User user,int userId) throws SQLException {
        Connection conn = JdbcUtils.getConnection();
        String sql1="SELECT *FROM `user` WHERE `user_id`=?";
        PreparedStatement ps1 = conn.prepareStatement(sql1);
        ps1.setInt(1,userId);
        ResultSet resultSet = ps1.executeQuery();
        String defaultEmail = null;
        String defaultNickname = null;
        int defaultGender = 0;
        String defaultDescirption = null;
        while(resultSet.next()){
            defaultEmail = resultSet.getString("user_e-mail");
            defaultNickname=resultSet.getString("user_nickname");
            defaultGender=resultSet.getInt("user_gender");
            defaultDescirption=resultSet.getString("user_description");
        }
        //查询并储存该用户的信息的原先值
        String sql ="UPDATE `user` SET `user_e-mail`=?,`user_nickname`=?,`user_gender`=?,`user_description`=? WHERE `user_id`=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        if(user.getUserEmail() ==null){
            ps.setString(1,defaultEmail);
        }else {
            ps.setString(1, user.getUserEmail());
        }
        if(user.getUserNickname() ==null){
            ps.setString(2, defaultNickname);
        }else {
            ps.setString(2,user.getUserNickname());
        }
        if(user.getUserGender() ==2){
            //2是性别为空的默认值
            ps.setInt(3, defaultGender);
        }else {
            ps.setInt(3,user.getUserGender());
        }
        if(user.getUserDescription() ==null){
            ps.setString(4, defaultDescirption);
        }
        else {
            ps.setString(4,user.getUserDescription());
        }
        //如果用户没有修改该栏信息，则保留上次的值,修改则覆盖
        ps.setInt(5,userId);
        int row = ps.executeUpdate();
        //sql语句返回结果判断
        //row是返回值，用于判断
        JdbcUtils.release(conn,ps,null);
        //释放连接
        return row;
    }
    //登录
    public int login(String loginName,String loginPassword) throws SQLException {
        Connection conn = JdbcUtils.getConnection();
        String sql ="SELECT `user_id`,`login_password`FROM `user` WHERE `login_name`=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1,loginName);
        ResultSet rs = ps.executeQuery();
        int userId=0;
        //用户的id
        if(!rs.isBeforeFirst()){
            userId=0;
        }
        //结果集为空，则令返回值userId为0
        else {
            String realPassword=null;
            //数据库中用户名对应的正确密码
            while (rs.next()){
                realPassword=rs.getString("login_password");
                userId=rs.getInt("user_id");
                //查询userId
            }
            if (!realPassword.equals(loginPassword)){
                //loginPassword是用户输入的密码
                //查无用户名或者密码错误都无法进入
                userId=0;
                //令userId为0
            }
        }

        return userId;
    }
    //验证要修改的密码
    public int verifyPassword(String oldPassword,int userId) throws SQLException {
        int row1=0;
        Connection conn = JdbcUtils.getConnection();
        String sql ="SELECT *FROM `user` WHERE `user_id`=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1,userId);
        ResultSet rs = ps.executeQuery();
        String realPassword=null;
        //数据库中用户名对应的正确密码
        while(rs.next()){
            realPassword=rs.getString("login_password");
        }
        if(realPassword.equals(oldPassword)){
            //验证密码成功则令结果为1
            row1=1;
        }
        return row1;
        //row1返回到view层用于验证
    }
    //修改密码
    public int changePassword(User user,int userId) throws SQLException {
        int row2=0;
        Connection conn = JdbcUtils.getConnection();
        String sql ="UPDATE `user` SET `login_password`=? WHERE `user_id`=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1,user.getLoginPassword());
        ps.setInt(2,userId);
        row2 = ps.executeUpdate();
        //sql语句返回结果判断
        //row2是返回值，用于判断
        JdbcUtils.release(conn,ps,null);
        //释放连接
        return row2;
    }
    //查询用户的个人信息
    public List queryUserInformation(int userId) throws SQLException {
        List<Object> arr = new ArrayList();
        Connection conn = JdbcUtils.getConnection();
        String sql ="SELECT *FROM `user` WHERE `user_id`=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1,userId);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            arr.add(rs.getString("login_name"));
            arr.add(rs.getString("user_e-mail"));
            arr.add(rs.getString("user_nickname"));
            arr.add(rs.getInt("user_gender"));
            arr.add(rs.getString("user_description"));
        }
        //把查询的结果集返回到service层
        return arr;
    }

}
