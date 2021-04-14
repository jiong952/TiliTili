package com.jiong.www.dao;

import com.jiong.www.po.Event;
import com.jiong.www.util.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * @author Mono
 */
public class EventDao {

    /**创建瓜，添加瓜信息到瓜表*/
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
    /**创建瓜时验证瓜名是否存在*/
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
    /**删除瓜时验证是不是用户发的瓜*/
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
    /**删除瓜时查询这个瓜所在的瓜圈名*/
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
    /**删除瓜*/
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
    /**查看瓜和搜索瓜,返回瓜的所有信息，封装,返回eventId作为参数给其他方法用*/
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

}
