package com.jiong.www.dao;

import com.jiong.www.po.Comment;
import com.jiong.www.po.Event;
import com.jiong.www.po.EventGroup;
import com.jiong.www.po.User;
import com.jiong.www.util.JdbcUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
            userDeafault.setUserBirthday(resultSet.getDate("user_birthday"));
        }
        //查询并储存该用户的信息的原先值
        String sql ="UPDATE `user` SET `user_e-mail`=?,`user_nickname`=?,`user_gender`=?,`user_description`=?,`user_birthday`=? WHERE `user_id`=?";
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
        if(user.getUserBirthday()==null){
            ps.setDate(5,userDeafault.getUserBirthday());
        }else {
            ps.setDate(5,user.getUserBirthday());
        }
        //如果用户没有修改该栏信息，则保留上次的值,修改则覆盖
        ps.setInt(6,userId);
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
        JdbcUtils.release(conn,ps,rs);
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
        JdbcUtils.release(conn,ps,rs);
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
        JdbcUtils.release(conn,ps,rs);
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
            userQuery.setUserBirthday(rs.getDate("user_birthday"));
        }
        JdbcUtils.release(conn,ps,rs);
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
        Connection conn = JdbcUtils.getConnection();
        int row=0;
        //用来抛出到view层做判断
        String sql="SELECT *FROM `eventgroup` WHERE `eventGroup_name`=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1,eventGroupName);
        ResultSet rs = ps.executeQuery();
        if(rs.isBeforeFirst()){
            row=1;
            //表里有数据则row=1
        }
        JdbcUtils.release(conn,ps,rs);
        return row;
        //抛出到view层判断
    }
    //验证是否是该管理员管理的瓜圈
    public int verifyEventGroupOfAdmin(int userId,String eventGroupName) throws SQLException {
        int row=0;
        //0表示不是
        Connection conn = JdbcUtils.getConnection();
        String sql="SELECT `id`\n" +
                "FROM `administrator`\n" +
                "INNER JOIN `eventgroup`\n" +
                "ON `administrator_groupid`=`eventGroup_id`\n" +
                "WHERE `eventGroup_name`=? AND `administrator_id`=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1,eventGroupName);
        ps.setInt(2,userId);
        ResultSet rs = ps.executeQuery();
        if(rs.isBeforeFirst()){
            row=1;
            //表里有数据则是该管理员管的瓜圈row=1
        }
        JdbcUtils.release(conn,ps,rs);
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
    //创建瓜时验证瓜名是否存在
    public int verifyEventName(String eventName) throws SQLException {
        Connection conn = JdbcUtils.getConnection();
        int row=1;
        //row为1表示不存在
        //用来抛出到view层做判断
        String sql="SELECT *FROM `event`WHERE `event_name`=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1,eventName);
        ResultSet rs = ps.executeQuery();
        if(rs.isBeforeFirst()){
            row=0;
            //row=0则表里有数据
        }
        JdbcUtils.release(conn,ps,rs);
        return row;
        //抛出到view层判断,1则无数据，0则有数据
    }
    //删除瓜时验证是不是用户发的瓜
    public int verifyEventOfUser(int userId,int eventId) throws SQLException {
        Connection conn = JdbcUtils.getConnection();
        int row=0;
        //默认不是用户的瓜
        //用来抛出到view层做判断
        String sql="SELECT `event_name` FROM `event` WHERE `publisher_id` = ? AND `event_id`  = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1,userId);
        ps.setInt(2,eventId);
        ResultSet rs = ps.executeQuery();
        if(rs.isBeforeFirst()){
            row=1;
            //表里有数据,是用户的瓜则row=1
        }
        JdbcUtils.release(conn,ps,rs);
        return row;
        //抛出到view层判断
    }
    //删除瓜时查询这个瓜所在的瓜圈名
    public String queryEventOfEventGroup(int eventId) throws SQLException {
        Connection conn = JdbcUtils.getConnection();
        //进行数据库连接
        String sql ="SELECT `eventGroup_name`\n" +
                "FROM `eventgroup` s\n" +
                "INNER JOIN `event` p\n" +
                "ON s.`eventGroup_id`=p.`eventGroup_id`\n" +
                "WHERE `event_id`  = ?";
        //联表查询
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1,eventId);
        ResultSet rs = ps.executeQuery();
        String eventGroupName = null;
        //eventGroupName为查询的瓜圈名
        while (rs.next()){
            eventGroupName=rs.getString("eventGroup_name");
        }
        JdbcUtils.release(conn,ps,rs);
        return eventGroupName;
    }
    //删除瓜
    public int deleteEvent(int eventId) throws SQLException {
        int row=0;
        Connection conn = JdbcUtils.getConnection();
        //进行数据库连接
        String sql ="DELETE FROM `event` WHERE `event_id` =?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1,eventId);
        row = ps.executeUpdate();
        //row为1删除成功,0删除失败
        JdbcUtils.release(conn,ps,null);
        return row;
    }
    //查看瓜和搜索瓜,返回瓜的所有信息，封装,返回eventId作为参数给其他方法用
    public Event viewEvent(String eventName) throws SQLException {
        Event eventQuery = new Event();
        Connection conn = JdbcUtils.getConnection();
        String sql ="SELECT `login_name`,`event_name`,`event_content`,`comment_num`,`likes_num`,s.`create_time`,`event_id`,`collection_num`\n" +
                "FROM `event` s\n" +
                "INNER JOIN `user` p\n" +
                "ON s.publisher_id= p.user_id\n" +
                "WHERE `event_name`=?";
        //联表查询
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1,eventName);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            eventQuery.setPublisherName(rs.getString("login_name"));
            //发布者的名字
            eventQuery.setEventName(rs.getString("event_name"));
            //瓜名
            eventQuery.setEventContent(rs.getString("event_content"));
            //瓜内容
            eventQuery.setLikesNum(rs.getInt("likes_num"));
            eventQuery.setCollectionNum(rs.getInt("collection_num"));
            eventQuery.setCommentNum(rs.getInt("comment_num"));
            //点赞收藏评论数
            eventQuery.setCreateTime(rs.getDate("create_time"));
            //瓜id
            eventQuery.setEventId(rs.getInt("event_id"));
        }
        JdbcUtils.release(conn,ps,rs);
        //把查询的结果集返回到service层
        return eventQuery;
    }
    //点赞,同时更新用户点赞表
    public void likes(int userId,int eventId) throws SQLException {
        Connection conn = JdbcUtils.getConnection();
        String sql ="UPDATE `event` SET `likes_num` = `likes_num`+1 WHERE `event_id` =?";
        String sql1="INSERT INTO `like` (`event_id`,`user_id`) VALUES(?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        PreparedStatement ps1 = conn.prepareStatement(sql1);
        ps.setInt(1,eventId);
        ps1.setInt(1,eventId);
        ps1.setInt(2,userId);
        ps.executeUpdate();
        ps1.executeUpdate();
        //sql语句返回结果判断
        //row是返回值，用于判断 0表示执行失败,1表示执行成功
        JdbcUtils.release(conn,ps,null);
        //释放连接
    }
    //取消点赞,同时删除用户点赞表中的相关数据
    public void cancelLikes(int userId,int eventId) throws SQLException {
        Connection conn = JdbcUtils.getConnection();
        String sql ="UPDATE `event` SET `likes_num` = `likes_num`-1 WHERE `event_id` =?";
        String sql1="DELETE FROM `like`  WHERE `event_id`= ? AND `user_id` =?";
        PreparedStatement ps = conn.prepareStatement(sql);
        PreparedStatement ps1 = conn.prepareStatement(sql1);
        ps.setInt(1,eventId);
        ps1.setInt(1,eventId);
        ps1.setInt(2,userId);
        ps.executeUpdate();
        ps1.executeUpdate();
        //sql语句返回结果判断
        //row是返回值，用于判断 0表示执行失败,1表示执行成功
        JdbcUtils.release(conn,ps,null);
        //释放连接
    }
    //查看点赞合集 每个瓜只展示事件标题 作者 发布时间 点赞量 收藏量 评论量
    public List<Event> viewEventOfLikes(int userId) throws SQLException {
        List<Event> events = new ArrayList<Event>();
        //创建一个容器返回 点赞瓜的信息
        Connection conn = JdbcUtils.getConnection();
        String sql ="SELECT `login_name`,`event_name`,`event_content`,`comment_num`,`likes_num`,s.`create_time`,s.`event_id`,`collection_num`\n" +
                "FROM `like` p\n" +
                "INNER JOIN `event` s\n" +
                "ON s.`event_id`=p.`event_id`\n" +
                "INNER JOIN `user` k\n" +
                "ON k.`user_id`=s.`publisher_id`\n" +
                "WHERE p.`user_id`=?";
        //联表查询
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1,userId);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            Event eventQuery = new Event();
            //event对象封装瓜的信息
            eventQuery.setPublisherName(rs.getString("login_name"));
            //发布者的名字
            eventQuery.setEventName(rs.getString("event_name"));
            //瓜名
            eventQuery.setEventContent(rs.getString("event_content"));
            //瓜内容
            eventQuery.setLikesNum(rs.getInt("likes_num"));
            eventQuery.setCollectionNum(rs.getInt("collection_num"));
            eventQuery.setCommentNum(rs.getInt("comment_num"));
            //点赞收藏评论数
            eventQuery.setCreateTime(rs.getDate("create_time"));
            //瓜id
            eventQuery.setEventId(rs.getInt("event_id"));
            events.add(eventQuery);
        }
        JdbcUtils.release(conn,ps,rs);
        //把查询的结果集返回到service层
        return events;
    }
    //收藏,同时更新收藏表
    public void collection(int userId,int eventId) throws SQLException {
        Connection conn = JdbcUtils.getConnection();
        String sql ="UPDATE `event` SET `collection_num` = `collection_num`+1 WHERE `event_id` =?";
        String sql1="INSERT INTO `collection` (`event_id`,`user_id`) VALUES(?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        PreparedStatement ps1 = conn.prepareStatement(sql1);
        ps.setInt(1,eventId);
        ps1.setInt(1,eventId);
        ps1.setInt(2,userId);
        ps.executeUpdate();
        ps1.executeUpdate();
        //sql语句返回结果判断
        //row是返回值，用于判断 0表示执行失败,1表示执行成功
        JdbcUtils.release(conn,ps,null);
        //释放连接
    }
    //取消收藏,同时删除用户收藏表中的相关数据
    public void cancelCollection(int userId,int eventId) throws SQLException {
        Connection conn = JdbcUtils.getConnection();
        String sql ="UPDATE `event` SET `collection_num` = `collection_num`-1 WHERE `event_id` =?";
        String sql1="DELETE FROM `collection`  WHERE `event_id`= ? AND `user_id` =?";
        PreparedStatement ps = conn.prepareStatement(sql);
        PreparedStatement ps1 = conn.prepareStatement(sql1);
        ps.setInt(1,eventId);
        ps1.setInt(1,eventId);
        ps1.setInt(2,userId);
        ps.executeUpdate();
        ps1.executeUpdate();
        //sql语句返回结果判断
        //row是返回值，用于判断 0表示执行失败,1表示执行成功
        JdbcUtils.release(conn,ps,null);
        //释放连接
    }
    //查看收藏合集 每个瓜只展示事件标题 作者 发布时间 点赞量 收藏量 评论量
    public List<Event> viewEventOfCollection(int userId) throws SQLException {
        List<Event> events = new ArrayList<Event>();
        //创建一个容器返回 收藏瓜的信息
        Connection conn = JdbcUtils.getConnection();
        String sql ="SELECT `login_name`,`event_name`,`event_content`,`comment_num`,`likes_num`,s.`create_time`,s.`event_id`,`collection_num`\n" +
                "FROM `collection` p\n" +
                "INNER JOIN `event` s\n" +
                "ON s.`event_id`=p.`event_id`\n" +
                "INNER JOIN `user` k\n" +
                "ON k.`user_id`=s.`publisher_id`\n" +
                "WHERE p.`user_id`=?";
        //联表查询
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1,userId);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            Event eventQuery = new Event();
            //event对象封装瓜的信息
            eventQuery.setPublisherName(rs.getString("login_name"));
            //发布者的名字
            eventQuery.setEventName(rs.getString("event_name"));
            //瓜名
            eventQuery.setEventContent(rs.getString("event_content"));
            //瓜内容
            eventQuery.setLikesNum(rs.getInt("likes_num"));
            eventQuery.setCollectionNum(rs.getInt("collection_num"));
            eventQuery.setCommentNum(rs.getInt("comment_num"));
            //点赞收藏评论数
            eventQuery.setCreateTime(rs.getDate("create_time"));
            //瓜id
            eventQuery.setEventId(rs.getInt("event_id"));
            events.add(eventQuery);
        }
        JdbcUtils.release(conn,ps,rs);
        //把查询的结果集返回到service层
        return events;
    }
    //进行评论，评论数+1，评论表更新
    public void comment(int userId,int eventId,Comment comment) throws SQLException {
        Connection conn = JdbcUtils.getConnection();
        String sql ="UPDATE `event` SET `comment_num` = `comment_num`+1 WHERE `event_id` =?";
        String sql1="INSERT INTO `comment` (`event_id`,`user_id`,`comment_content`) VALUES(?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        PreparedStatement ps1 = conn.prepareStatement(sql1);
        ps.setInt(1,eventId);
        ps1.setInt(1,eventId);
        ps1.setInt(2,userId);
        ps1.setString(3,comment.getCommentContent());
        ps.executeUpdate();
        ps1.executeUpdate();
        //sql语句返回结果判断
        //row是返回值，用于判断
        JdbcUtils.release(conn,ps,null);
        JdbcUtils.release(conn,ps1,null);
        //释放连接
        //向上抛出到view层
    }
    //删除评论，同时删除用户评论表中的相关数据,用于普通用户的删除
    public void cancelComment(int userId,int eventId) throws SQLException{
        Connection conn = JdbcUtils.getConnection();
        String sql ="UPDATE `event` SET `comment_num` = `comment_num`-1 WHERE `event_id` =?";
        String sql1="DELETE FROM `comment` WHERE `event_id`= ? AND `user_id` =?";
        PreparedStatement ps = conn.prepareStatement(sql);
        PreparedStatement ps1 = conn.prepareStatement(sql1);
        ps.setInt(1,eventId);
        ps1.setInt(1,eventId);
        ps1.setInt(2,userId);
        ps.executeUpdate();
        ps1.executeUpdate();
        //sql语句返回结果判断
        //row是返回值，用于判断 0表示执行失败,1表示执行成功
        JdbcUtils.release(conn,ps,null);
        //释放连接
    }
    //删除瓜的所有评论，管理员
    public void clearComment(int eventId) throws SQLException {
        Connection conn = JdbcUtils.getConnection();
        String sql="DELETE FROM `comment` WHERE `event_id`= ? ";
        //清空所有评论
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1,eventId);
        ps.executeUpdate();
        //sql语句返回结果判断
        //row是返回值，用于判断 0表示执行失败,1表示执行成功
        JdbcUtils.release(conn,ps,null);
        //释放连接
    }
    //查看瓜的评论,也要返回评论人名
    public List<Comment> viewComment(int eventId) throws SQLException {
        List<Comment> comments = new ArrayList<Comment>();
        //创建一个容器返回 评论的信息
        Connection conn = JdbcUtils.getConnection();
        String sql ="SELECT `login_name`,`comment_content`\n" +
                "FROM `user` s\n" +
                "INNER JOIN `comment` p\n" +
                "ON s.`user_id`=p.`user_id`\n" +
                "WHERE `event_id`=?";
        //联表查询
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1,eventId);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            Comment comment = new Comment();
            comment.setCommentContent(rs.getString("comment_content"));
            comment.setCommenterName(rs.getString("login_name"));
            comments.add(comment);
        }
        JdbcUtils.release(conn,ps,rs);
        //把查询的结果集返回到service层
        return comments;

    }
    //用户举报瓜
    public int accuseEvent(int eventId) throws SQLException {
        int row=0;
        Connection conn = JdbcUtils.getConnection();
        String sql ="UPDATE `event` SET `accuse_status` = 1 WHERE `event_id`=?";
        //联表查询
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1,eventId);
        row = ps.executeUpdate();
        JdbcUtils.release(conn,ps,null);
        //把查询的结果集返回到service层
        return row;
    }
}
