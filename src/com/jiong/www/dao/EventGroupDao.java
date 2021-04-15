package com.jiong.www.dao;

import com.jiong.www.po.Event;
import com.jiong.www.po.EventGroup;
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
public class EventGroupDao {
    /**创建瓜圈,添加瓜圈信息到瓜圈表，并且把管理员和瓜联系一起*/
    public int createEventGroup(int userId, EventGroup eventGroup) throws SQLException {
        Connection conn = JdbcUtils.getConnection();
        int row;
        conn.setAutoCommit(false);
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
        conn.commit();
        JdbcUtils.release(conn,ps,null);
        JdbcUtils.release(conn,ps1,null);
        //释放连接
        return row;
        //向上抛出到view层
    }
    /**验证瓜圈名是否存在,避免发生重复*/
    public int verifyEventGroupName(String eventGroupName) throws SQLException {
        Connection conn = JdbcUtils.getConnection();
        int row=0;
        //用来抛出到view层做判断
        String sql="SELECT `eventGroup_id` FROM `eventgroup` WHERE `eventGroup_name`=?";
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
    /**验证是否是该管理员管理的瓜圈*/
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
    /**删除瓜圈，同时在管理员所管理的数据删除关系,删除瓜圈瓜圈里瓜也要删除*/
    public int deleteEventGroup(String deleteEventGroupName,int userId) throws SQLException {
        Connection conn = JdbcUtils.getConnection();
        int row;
        conn.setAutoCommit(false);
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
        conn.commit();
        //sql语句返回结果判断
        //row是返回值，用于判断
        JdbcUtils.release(conn,ps,null);
        JdbcUtils.release(conn,ps1,null);
        //释放连接
        return row;
        //向上抛出到view层
    }
    /**查看瓜圈*/
    public EventGroup viewEventGroup(String eventGroupName) throws SQLException {
        EventGroup eventGroup = new EventGroup();
        Connection conn = JdbcUtils.getConnection();
        String sql ="SELECT `eventGroup_description`,`eventGroup_id`FROM `eventgroup` WHERE `eventGroup_name` = ?";
        //联表查询
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1,eventGroupName);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            eventGroup.setEventGroupDescription(rs.getString("eventGroup_description"));
            eventGroup.setEventGroupId(rs.getInt("eventGroup_id"));
        }
        JdbcUtils.release(conn,ps,rs);
        //把查询的结果集返回到service层
        return eventGroup;
    }
    /**查看所有瓜圈*/
    public List<EventGroup> viewAllEventGroup() throws SQLException {
        List<EventGroup> eventGroups = new ArrayList<>();
        Connection conn = JdbcUtils.getConnection();
        String sql ="SELECT `eventGroup_name`,`eventGroup_description` FROM `eventgroup`";
        //联表查询
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            EventGroup eventGroup = new EventGroup();
            eventGroup.setEventGroupName(rs.getString("eventGroup_name"));
            eventGroup.setEventGroupDescription(rs.getString("eventGroup_description"));
            eventGroups.add(eventGroup);
        }
        JdbcUtils.release(conn,ps,rs);
        //把查询的结果集返回到service层
        return eventGroups;
    }
    /**查看瓜圈里的所有瓜*/
    public List<Event> viewEventOfEventGroup(String eventGroupName) throws SQLException {
        List<Event> events = new ArrayList<>();
        Connection conn = JdbcUtils.getConnection();
        String sql ="SELECT `event_name`,`login_name`,`comment_num`,`likes_num`,`collection_num`,s.`create_time`\n" +
                "FROM `event` s\n" +
                "INNER JOIN `eventgroup` p\n" +
                "ON p.`eventGroup_id`=s.`eventGroup_id`\n" +
                "INNER JOIN `user` o\n" +
                "ON o.`user_id`=s.`publisher_id`\n" +
                "WHERE p.`eventGroup_name`=?";
        //联表查询
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1,eventGroupName);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            Event event = new Event();
            event.setEventName(rs.getString("event_name"));
            event.setPublisherName(rs.getString("login_name"));
            event.setCommentNum(rs.getInt("comment_num"));
            event.setLikesNum(rs.getInt("likes_num"));
            event.setCollectionNum(rs.getInt("collection_num"));
            event.setCreateTime(rs.getDate("create_time"));
            events.add(event);
        }
        JdbcUtils.release(conn,ps,rs);
        //把查询的结果集返回到service层
        return events;
    }
}
