package com.jiong.www.dao;

import com.jiong.www.po.Event;
import com.jiong.www.util.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mono
 */
public class CollectionDao {
    /**收藏,同时更新收藏表*/
    public void collection(int userId,int eventId) throws SQLException {
        Connection conn = JdbcUtils.getConnection();
        conn.setAutoCommit(false);
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
        conn.commit();
        JdbcUtils.release(conn,ps,null);
        //释放连接
    }
    /**取消收藏,同时删除用户收藏表中的相关数据*/
    public void cancelCollection(int userId,int eventId) throws SQLException {
        Connection conn = JdbcUtils.getConnection();
        conn.setAutoCommit(false);
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
        conn.commit();
        JdbcUtils.release(conn,ps,null);
        //释放连接
    }
    /**查看用户是否点赞*/
    public int collectionIfOrNot(int userId,int eventId) throws SQLException {
        int judge=0;
        Connection conn = JdbcUtils.getConnection();
        String sql1="SELECT `id` FROM `collection`  WHERE `event_id`= ? AND `user_id` =?";
        PreparedStatement ps1 = conn.prepareStatement(sql1);
        ps1.setInt(1,eventId);
        ps1.setInt(2,userId);
        ResultSet rs = ps1.executeQuery();
        if(rs.isBeforeFirst()){
            judge=1;
            //表里有数据,是用户有收藏,judge=1
        }
        //sql语句返回结果判断
        //row是返回值，用于判断 0表示执行失败,1表示执行成功
        JdbcUtils.release(conn,ps1,null);
        //释放连接
        return judge;
    }
    /**查看收藏合集 每个瓜只展示事件标题 作者 发布时间 点赞量 收藏量 评论量*/
    public List<Event> viewEventOfCollection(int userId) throws SQLException {
        List<Event> events = new ArrayList<>();
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

}
