package com.jiong.www.dao;

import com.jiong.www.po.User;
import com.jiong.www.util.ImageUtils;
import com.jiong.www.util.JdbcUtils;

import java.io.InputStream;
import java.sql.*;

/**
 * @author Mono
 */
public class UserDao {
    /**注册，添加用户信息到用户表, 把新注册的用户加入到用户角色表，默认新注册只能为吃瓜群众即1*/
    public int register(User user) throws SQLException {
        Connection conn = JdbcUtils.getConnection();
        int row;
        conn.setAutoCommit(false);
        String sql ="INSERT INTO `user` (`login_name`,`login_password`,`user_nickname`) VALUES(?,?,?)";
        String sql1 ="INSERT INTO `user_role`(`user_id`,`role_id`)VALUES((SELECT `user_id`FROM `user` WHERE `login_name`=?),1)";
        //`login_name`加了唯一约束，在数据库设计上可以防止重名
        PreparedStatement ps = conn.prepareStatement(sql);
        PreparedStatement ps1 = conn.prepareStatement(sql1);
        ps.setString(1,user.getLoginName());
        ps.setString(2,user.getLoginPassword());
        ps.setString(3,user.getLoginName());
        ps1.setString(1,user.getLoginName());
        //昵称默认为用户名
        row= ps.executeUpdate();
        ps1.executeUpdate();
        //sql语句返回结果判断
        conn.commit();
        JdbcUtils.release(conn,ps,null);
        JdbcUtils.release(conn,ps1,null);
        //释放连接
        return row;
        //向上抛出到view层
    }
    /**用于注册时验证该用户名是否存在*/
    public int verifyUsername(String loginName) throws SQLException {
        Connection conn = JdbcUtils.getConnection();
        int row=0;
        //默认为0不存在
        //用来抛出到view层做判断
        String sql="SELECT *FROM `user` WHERE `login_name`=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1,loginName);
        ResultSet rs = ps.executeQuery();
        if(rs.isBeforeFirst()){
            row=1;
            //表里有数据则row=1
        }
        JdbcUtils.release(conn,ps,rs);
        return row;
        //抛出到view层判断
    }
    /**完善用户信息,实现了每次只改动一个信息，其他的保存为上次的值*/
    public int perfectInformation(User user,int userId) throws SQLException {
        Connection conn = JdbcUtils.getConnection();
        //查询并储存该用户的信息的原先值
        String sql ="UPDATE `user` SET `user_e-mail`=?,`user_nickname`=?,`user_gender`=?,`user_description`=?,`user_birthday`=?WHERE `user_id`=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, user.getUserEmail());
        ps.setString(2,user.getUserNickname());
        ps.setInt(3,user.getUserGender());
        ps.setString(4,user.getUserDescription());
        ps.setDate(5, user.getUserBirthday());
        //如果用户没有修改该栏信息，则保留上次的值,修改则覆盖
        ps.setInt(6,userId);
        int row = ps.executeUpdate();
        //sql语句返回结果判断
        //row是返回值，用于判断
        JdbcUtils.release(conn,ps,null);
        //释放连接
        return row;
    }
    /**是否记住密码*/
    public int isRememberPassword(int isRememberPassword,int userId) throws SQLException {
        Connection conn = JdbcUtils.getConnection();
        //查询并储存该用户的信息的原先值
        String sql ="UPDATE `user` SET `password_remember`=? WHERE `user_id`=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        //如果用户没有修改该栏信息，则保留上次的值,修改则覆盖
        ps.setInt(1,isRememberPassword);
        ps.setInt(2,userId);
        int row = ps.executeUpdate();
        //sql语句返回结果判断
        //row是返回值，用于判断
        JdbcUtils.release(conn,ps,null);
        //释放连接
        return row;
    }
    /**登录*/
    public int login(String loginName,String loginPassword) throws SQLException {
        Connection conn = JdbcUtils.getConnection();
        String sql ="SELECT `user_id`,`login_password`FROM `user` WHERE `login_name`=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1,loginName);
        ResultSet rs = ps.executeQuery();
        int userId=0;
        //用户的id
        if(rs.isBeforeFirst()){
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
        //结果集为空，则令返回值userId为0
        JdbcUtils.release(conn,ps,rs);
        return userId;
    }
    /**验证用户的身份，吃瓜群众1管理员2游客3超管4*/
    public int verifyRole(int userId) throws SQLException {
        Connection conn = JdbcUtils.getConnection();
        String sql ="SELECT *FROM `user_role`WHERE`user_id`=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1,userId);
        ResultSet rs = ps.executeQuery();
        int roleId=0;
        //用户角色的id
        while (rs.next()){
            roleId=rs.getInt("role_id");
        }
        JdbcUtils.release(conn,ps,rs);
        return roleId;
    }
    /**验证要修改的密码*/
    public int verifyPassword(String oldPassword,int userId) throws SQLException {
        int row=0;
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
            row=1;
        }
        JdbcUtils.release(conn,ps,rs);
        return row;
        //row1返回到view层用于验证
    }
    /**修改密码*/
    public int changePassword(User user,int userId) throws SQLException {
        int row2;
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
    /**查询用户的个人信息*/
    public User queryUserInformation(int userId) throws SQLException {
        User userQuery = new User();
        Connection conn = JdbcUtils.getConnection();
        String sql ="SELECT *FROM `user` WHERE `user_id`=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1,userId);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            userQuery.setLoginName(rs.getString("login_name"));
            userQuery.setUserEmail(rs.getString("user_e-mail"));
            userQuery.setUserNickname(rs.getString("user_nickname"));
            userQuery.setUserGender(rs.getInt("user_gender"));
            userQuery.setUserDescription(rs.getString("user_description"));
            userQuery.setUserBirthday(rs.getDate("user_birthday"));
            userQuery.setLoginPassword(rs.getString("login_password"));
            userQuery.setIsRememberPassword(rs.getInt("password_remember"));
        }
        JdbcUtils.release(conn,ps,rs);
        //把查询的结果集返回到service层
        return userQuery;
    }
    /**用户输入用户名，查看是否存在，存在则查看是否记住密码，是的话，把密码返回*/
    public User isRememberPassword(String loginName) throws SQLException {
        User user = new User();
        Connection conn = JdbcUtils.getConnection();
        String sql ="SELECT *FROM `user` WHERE `login_name`=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1,loginName);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            user.setIsRememberPassword(rs.getInt("password_remember"));
            user.setLoginPassword(rs.getString("login_password"));
        }
        return user;
    }
    /**保存用户设置的头像文件到数据库，把该二进制文件存到特定文件夹*/
    public int saveIcon(InputStream inputStream,int userId) throws SQLException {
        Connection conn = JdbcUtils.getConnection();
        conn.setAutoCommit(false);
        String sql ="UPDATE `user` SET `icon` = ? WHERE `user_id` = ?";
        String sql1 = "SELECT `icon` FROM `user` WHERE `user_id`=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        PreparedStatement ps1 = conn.prepareStatement(sql1);
        ps.setBinaryStream(1,inputStream);
        ps.setInt(2,userId);
        ps1.setInt(1,userId);
        int judge = ps.executeUpdate();
        ResultSet rs = ps1.executeQuery();
        while (rs.next()){
            InputStream binaryStream = rs.getBinaryStream("icon");
            new ImageUtils().readBlob(binaryStream,"C:\\Users\\Mono\\Desktop\\TiliTili照片\\" + userId + ".jpg");
        }
        JdbcUtils.release(conn,ps,null);
        JdbcUtils.release(conn,ps1,rs);
        return judge;
    }
    /**删除用户保存的头像文件*/
    public int deleteIcon(int userId) throws SQLException {
        int judge;
        Connection conn = JdbcUtils.getConnection();
        String sql ="UPDATE `user` SET `icon` = ? WHERE `user_id` = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setBinaryStream(1,null);
        ps.setInt(2,userId);
        judge=ps.executeUpdate();
        return judge;
    }
}
