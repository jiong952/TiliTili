package com.jiong.www.dao;

import com.jiong.www.po.Event;
import com.jiong.www.po.EventGroup;
import com.jiong.www.po.User;
import com.jiong.www.util.JdbcUtils;

import java.sql.*;
/**
 * @author Mono
 */
public class TilitiliDao {
    //注册，添加用户信息到用户表, 把新注册的用户加入到用户角色表，默认新注册只能为吃瓜群众即1
    public int register(User user) throws SQLException {
        Connection conn = JdbcUtils.getConnection();
        int row=0;
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
        //row是返回值，用于判断
        JdbcUtils.release(conn,ps,null);
        JdbcUtils.release(conn,ps1,null);
        //释放连接
        return row;
        //向上抛出到view层
    }
    //用于注册时验证该用户名是否存在
    public int verifyUsername(String loginName) throws SQLException {
        Connection connection = JdbcUtils.getConnection();
        int row=0;
        //默认为0不存在
        //用来抛出到view层做判断
        String sql1="SELECT *FROM `user` WHERE `login_name`=?";
        PreparedStatement ps = connection.prepareStatement(sql1);
        ps.setString(1,loginName);
        ResultSet resultSet = ps.executeQuery();
        if(resultSet.isBeforeFirst()){
            row=1;
            //表里有数据则row=1
        }
        return row;
        //抛出到view层判断
    }
    //完善用户信息,实现了每次只改动一个信息，其他的保存为上次的值
    public int perfectInformation(User user,int userId) throws SQLException {
        Connection conn = JdbcUtils.getConnection();
        String sql1="SELECT *FROM `user` WHERE `user_id`=?";
        PreparedStatement ps1 = conn.prepareStatement(sql1);
        ps1.setInt(1,userId);
        ResultSet resultSet = ps1.executeQuery();
        User userDeafault = new User();
        //userDeafault用来封装表中数据地初始值
        while(resultSet.next()){
            userDeafault.setUserEmail(resultSet.getString("user_e-mail"));
            userDeafault.setUserNickname(resultSet.getString("user_nickname"));
            userDeafault.setUserGender(resultSet.getInt("user_gender"));
            userDeafault.setUserDescription(resultSet.getString("user_description"));
        }
        //查询并储存该用户的信息的原先值
        String sql ="UPDATE `user` SET `user_e-mail`=?,`user_nickname`=?,`user_gender`=?,`user_description`=? WHERE `user_id`=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        if(user.getUserEmail() ==null){
            ps.setString(1,userDeafault.getUserEmail());
        }else {
            ps.setString(1, user.getUserEmail());
        }
        if(user.getUserNickname() ==null){
            ps.setString(2, userDeafault.getLoginName());
        }else {
            ps.setString(2,user.getUserNickname());
        }
        if(user.getUserGender() ==2){
            //2是性别为空的默认值
            ps.setInt(3, userDeafault.getUserGender());
        }else {
            ps.setInt(3,user.getUserGender());
        }
        if(user.getUserDescription() ==null){
            ps.setString(4,userDeafault.getUserDescription());
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
        JdbcUtils.release(conn,ps1,null);
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
    //验证用户的身份，吃瓜群众1管理员2游客3超管4
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
        return roleId;
    }
    //验证要修改的密码
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
        return row;
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
        }
        //把查询的结果集返回到service层
        return userQuery;
    }
    //创建瓜圈,添加瓜圈信息到瓜圈表，并且把管理员和瓜联系一起
    public int createEventGroup(int userId,EventGroup eventGroup) throws SQLException {
        Connection conn = JdbcUtils.getConnection();
        int row=0;
        String sql ="INSERT INTO `eventgroup`(`eventGroup_name`,`eventGroup_description`) VALUES(?,?)";
        String sql1 ="INSERT INTO `administrator`(`administrator_id`,`administrator_groupid`)VALUES(?,(SELECT `eventGroup_id`FROM `eventgroup` WHERE `eventGroup_name`=?))";
        //`eventGroup_name`加了唯一约束，在数据库设计上可以防止重名
        PreparedStatement ps = conn.prepareStatement(sql);
        PreparedStatement ps1 = conn.prepareStatement(sql1);
        ps.setString(1,eventGroup.getEventGroupName());
        ps.setString(2, eventGroup.getEventGroupDescription());
        ps1.setInt(1,userId);
        ps1.setString(2,eventGroup.getEventGroupName());
        row= ps.executeUpdate();
        ps1.executeUpdate();
        //sql语句返回结果判断
        //row是返回值，用于判断
        JdbcUtils.release(conn,ps,null);
        JdbcUtils.release(conn,ps1,null);
        //释放连接
        return row;
        //向上抛出到view层
    }
    //验证瓜圈名是否存在,避免发生重复
    public int verifyEventGroupName(String eventGroupName) throws SQLException {
        Connection connection = JdbcUtils.getConnection();
        int row=0;
        //用来抛出到view层做判断
        String sql="SELECT *FROM `eventgroup` WHERE `eventGroup_name`=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1,eventGroupName);
        ResultSet resultSet = ps.executeQuery();
        if(resultSet.isBeforeFirst()){
            row=1;
            //表里有数据则row=1
        }
        return row;
        //抛出到view层判断
    }
    //验证是否是该管理员管理的瓜圈
    public int verifyEventGroupOfAdmin(int userId,String eventGroupName) throws SQLException {
        int row=0;
        //0表示不是
        Connection connection = JdbcUtils.getConnection();
        String sql="SELECT `id`\n" +
                "FROM `administrator`\n" +
                "INNER JOIN `eventgroup`\n" +
                "ON `administrator_groupid`=`eventGroup_id`\n" +
                "WHERE `eventGroup_name`=? AND `administrator_id`=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1,eventGroupName);
        ps.setInt(2,userId);
        ResultSet resultSet = ps.executeQuery();
        if(resultSet.isBeforeFirst()){
            row=1;
            //表里有数据则是该管理员管的瓜圈row=1
        }
        return row;
    }
    //删除瓜圈，同时在管理员所管理的数据删除关系,删除瓜圈瓜圈里瓜也要删除
    public int deleteEventGroup(String deleteEventGroupName,int userId) throws SQLException {
        Connection conn = JdbcUtils.getConnection();
        int row;
        //删除瓜圈
        String sql ="DELETE FROM `eventgroup` WHERE `eventGroup_name`=?";
        //删除瓜圈与管理员的关系
        String sql1 ="DELETE FROM `administrator`WHERE `administrator_id`=? AND `administrator_groupid`=(SELECT `eventGroup_id` FROM `eventgroup` WHERE `eventGroup_name`=?)";
        //删除瓜圈里的瓜
        String sql2 ="DELETE FROM `event` WHERE `eventGroup_id`=(SELECT `eventGroup_id` FROM `eventgroup` WHERE `eventGroup_name`=?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        PreparedStatement ps1 = conn.prepareStatement(sql1);
        PreparedStatement ps2= conn.prepareStatement(sql2);
        ps.setString(1,deleteEventGroupName);
        ps1.setInt(1,userId);
        //userId是管理员id
        ps1.setString(2,deleteEventGroupName);
        ps2.setString(1,deleteEventGroupName);
        //先删除瓜圈与管理员的关系
        ps1.executeUpdate();
        //再删除瓜圈里的瓜
        ps2.executeUpdate();
        //最后删除瓜圈
        row= ps.executeUpdate();
        //sql语句返回结果判断
        //row是返回值，用于判断
        JdbcUtils.release(conn,ps,null);
        JdbcUtils.release(conn,ps1,null);
        //释放连接
        return row;
        //向上抛出到view层
    }
    //创建瓜，添加瓜信息到瓜表
    public int createEvent(int userId, int eventGroupId, Event event) throws SQLException {
        Connection conn = JdbcUtils.getConnection();
        int row;
        String sql ="INSERT INTO `event`(`eventGroup_id`,`publisher_id`,`event_name`,`event_content`) VALUES(?,?,?,?)";
        // event_name加了唯一约束，在数据库设计上可以防止重名
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1,eventGroupId);
        ps.setInt(2,userId);
        ps.setString(3,event.getEventName());
        ps.setString(4,event.getEventContent());
        row= ps.executeUpdate();
        //sql语句返回结果判断
        //row是返回值，用于判断
        JdbcUtils.release(conn,ps,null);
        //释放连接
        return row;
        //向上抛出到view层
    }
    //验证瓜名是否存在
    public int verifyEventName(String eventName) throws SQLException {
        Connection connection = JdbcUtils.getConnection();
        int row=1;
        //row为1表示不存在
        //用来抛出到view层做判断
        String sql="SELECT *FROM `event`WHERE `event_name`=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1,eventName);
        ResultSet resultSet = ps.executeQuery();
        if(resultSet.isBeforeFirst()){
            row=0;
            //row=0则表里有数据
        }
        return row;
        //抛出到view层判断,1则无数据，0则有数据
    }
    //验证是不是用户发的瓜
    public int verifyEventOfUser(int userId,String eventName) throws SQLException {
        Connection connection = JdbcUtils.getConnection();
        int row=0;
        //默认不是用户的瓜
        //用来抛出到view层做判断
        String sql="SELECT *FROM `eventgroup` WHERE `eventGroup_name`=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1,eventName);
        ResultSet resultSet = ps.executeQuery();
        if(resultSet.isBeforeFirst()){
            row=1;
            //表里有数据,是用户的瓜则row=1
        }
        return row;
        //抛出到view层判断
    }
    //删除瓜，用户只能删除自己的瓜，管理员可以删除自己瓜圈里的所有瓜
    public int deleteEvent(int userId,int roleId,String deleteEventName) throws SQLException {
        int row=0;
        String eventGroupName=null;
        Connection conn = JdbcUtils.getConnection();
        if(roleId==1){
            //为普通用户
            //验证这个瓜是不是该用户发的
            int row1=verifyEventOfUser(userId,deleteEventName);
            if(row1==1){
                //row1==1表示是该用户发的
                //进行删除
            }else {

            }
        }
        else if(roleId==2){
            //管理员
            //先查这个瓜在哪个组,查出瓜圈名
            String sql ="SELECT `eventGroup_name`\n" +
                    "FROM `eventgroup` s\n" +
                    "INNER JOIN `event` p\n" +
                    "ON s.`eventGroup_id`=p.`eventGroup_id`\n" +
                    "WHERE `event_name`=?";
            PreparedStatement ps0 = conn.prepareStatement(sql);
            ResultSet rs = ps0.executeQuery();
            while (rs.next()){
                eventGroupName=rs.getString("eventGroup_name");
            }
            //验证这个组是不是归管理员管
            int row2 = verifyEventGroupOfAdmin(userId, eventGroupName);
            //row==1表示是该管理员管理的
            if(row2==1){
                //进行删除
            }

        }
        String sql1 ="DELETE FROM `event` WHERE `event_name`=?";
        return row;
    }
}
