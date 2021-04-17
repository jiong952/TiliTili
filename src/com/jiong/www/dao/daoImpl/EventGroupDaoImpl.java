package com.jiong.www.dao.daoImpl;

import com.jiong.www.dao.dao.IEventGroupDao;
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
public class EventGroupDaoImpl implements IEventGroupDao {
    /**创建瓜圈,添加瓜圈信息到瓜圈表*/
    @Override
    public int doCreate(EventGroup eventGroup) {
        int row = 0;
        Connection conn = null;
        PreparedStatement ps =null;
        try {
            conn = JdbcUtils.getConnection();
            String sql ="INSERT INTO `eventgroup`(`eventGroup_name`,`eventGroup_description`) VALUES(?,?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1,eventGroup.getEventGroupName());
            ps.setString(2, eventGroup.getEventGroupDescription());
            row= ps.executeUpdate();
            //row是返回值，用于判断
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                JdbcUtils.release(conn,ps,null);
                //释放连接
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return row;
        //向上抛出到view层
    }
    /**把瓜圈和管理员联系起来*/
    @Override
    public void groupOfAdmin(int userId, EventGroup eventGroup){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JdbcUtils.getConnection();
            String sql ="INSERT INTO `administrator`(`administrator_id`,`administrator_groupid`)VALUES(?,(SELECT `eventGroup_id`FROM `eventgroup` WHERE `eventGroup_name`=?))";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,userId);
            ps.setString(2,eventGroup.getEventGroupName());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                JdbcUtils.release(conn,ps,null);
                //释放连接
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    /**验证瓜圈名是否存在,避免发生重复*/
    @Override
    public int verifyExist(String eventGroupName) {
        int row=0;
        Connection conn = null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            conn = JdbcUtils.getConnection();
            String sql="SELECT `eventGroup_id` FROM `eventgroup` WHERE `eventGroup_name`=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1,eventGroupName);
            rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                row=1;
                //表里有数据则row=1
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                JdbcUtils.release(conn,ps,rs);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return row;
        //抛出到view层判断
    }
    /**验证是否是该管理员管理的瓜圈*/
    @Override
    public int verifyOfAdmin(int userId, int eventGroupId) {
        int row=0;
        //0表示不是
        Connection conn = null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            conn = JdbcUtils.getConnection();
            String sql="SELECT `id` FROM `administrator` WHERE `administrator_id` = ? AND `administrator_groupid` = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,userId);
            ps.setInt(2,eventGroupId);
            rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                row=1;
                //表里有数据则是该管理员管的瓜圈row=1
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                JdbcUtils.release(conn,ps,rs);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return row;
    }
    /**删除瓜圈*/
    @Override
    public int doDelete(String deleteEventGroupName)  {
        int row = 0;
        Connection conn = null;
        PreparedStatement ps=null;
        try {
            conn = JdbcUtils.getConnection();
            //删除瓜圈
            String sql ="DELETE FROM `eventgroup` WHERE `eventGroup_name`=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1,deleteEventGroupName);
            //最后删除瓜圈
            row= ps.executeUpdate();
            //sql语句返回结果判断
            //row是返回值，用于判断
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                JdbcUtils.release(conn,ps,null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //释放连接
        return row;
        //向上抛出到view层
    }
    /**删除在管理员表与瓜圈的数据*/
    @Override
    public void doDeleteOfAdmin(int eventGroupId, int userId){
        Connection conn = null;
        PreparedStatement ps=null;
        try {
            conn = JdbcUtils.getConnection();
            //删除瓜圈与管理员的关系
            String sql ="DELETE FROM `administrator`WHERE `administrator_id`=? AND `administrator_groupid`=?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,userId);
            //userId是管理员id
            ps.setInt(2,eventGroupId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                JdbcUtils.release(conn,ps,null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    /**用瓜圈名查该瓜圈信息*/
    @Override
    public EventGroup viewEventGroup(String eventGroupName){
        EventGroup eventGroup = new EventGroup();
        Connection conn = null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            conn = JdbcUtils.getConnection();
            String sql ="SELECT `eventGroup_description`,`eventGroup_id`FROM `eventgroup` WHERE `eventGroup_name` = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1,eventGroupName);
            rs = ps.executeQuery();
            while (rs.next()){
                eventGroup.setEventGroupDescription(rs.getString("eventGroup_description"));
                eventGroup.setEventGroupId(rs.getInt("eventGroup_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                JdbcUtils.release(conn,ps,rs);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //把查询的结果集返回到service层
        return eventGroup;
    }
    /**用瓜圈id查瓜圈名*/
    @Override
    public String viewEventGroup(int eventGroupId){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs=null;
        String eventGroupName=null;
        try {
            conn = JdbcUtils.getConnection();
            String sql ="SELECT `eventGroup_name` FROM `eventgroup` WHERE `eventGroup_id`=?";
            //联表查询
            ps = conn.prepareStatement(sql);
            ps.setInt(1,eventGroupId);
            rs = ps.executeQuery();
            while (rs.next()){
                eventGroupName = rs.getString("eventGroup_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                JdbcUtils.release(conn,ps,rs);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //把查询的结果集返回到service层
        return eventGroupName;
    }
    /**查看管理员管理的所有瓜圈的瓜圈id*/
    @Override
    public List<Integer> viewAdminGroup(int userId){
        Connection conn = null;
        PreparedStatement ps=null;
        ResultSet rs = null;
        List<Integer> list = new ArrayList<>();
        try {
            conn=JdbcUtils.getConnection();
            String sql = "SELECT `administrator_groupid` FROM `administrator` WHERE `administrator_id` = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,userId);
            rs=ps.executeQuery();
            while (rs.next()){
                list.add(rs.getInt("administrator_groupid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                JdbcUtils.release(conn,ps,rs);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
    /**用一组瓜圈id查该瓜圈里的所有瓜的瓜信息*/
    @Override
    public List<Event> viewEventGroup(List<Integer> list){
        Connection conn = null;
        PreparedStatement ps=null;
        ResultSet rs = null;
        List<Event> eventList = new ArrayList<>();
        try {
            conn=JdbcUtils.getConnection();
            for (Integer integer : list) {
                String sql = "SELECT `event_id`,`event_name` FROM `event` WHERE `eventGroup_id` = ?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, integer);
                rs = ps.executeQuery();
                while (rs.next()) {
                    Event event = new Event();
                    event.setEventId(rs.getInt("event_id"));
                    event.setEventName(rs.getString("event_name"));
                    eventList.add(event);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                JdbcUtils.release(conn,ps,rs);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return eventList;
    }
    /**查看系统所有瓜圈*/
    @Override
    public List<EventGroup> viewAllEventGroup()  {
        List<EventGroup> eventGroups = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps=null;
        ResultSet rs = null;
        try {
            conn = JdbcUtils.getConnection();
            String sql ="SELECT `eventGroup_name`,`eventGroup_description` FROM `eventgroup`";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                EventGroup eventGroup = new EventGroup();
                eventGroup.setEventGroupName(rs.getString("eventGroup_name"));
                eventGroup.setEventGroupDescription(rs.getString("eventGroup_description"));
                eventGroups.add(eventGroup);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                JdbcUtils.release(conn,ps,rs);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //把查询的结果集返回到service层
        return eventGroups;
    }
    /**用瓜圈id查看瓜圈里的所有瓜信息*/
    @Override
    public List<Event> viewEventOfEventGroup(int eventGroupId)  {
        List<Event> events = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            conn = JdbcUtils.getConnection();
            String sql ="SELECT `event_id`,`event_name`,`comment_num`,`likes_num`,`collection_num`,`create_time` \n" +
                    "FROM `event` WHERE `eventGroup_id` = ?";
            //联表查询
            ps = conn.prepareStatement(sql);
            ps.setInt(1,eventGroupId);
            rs = ps.executeQuery();
            while(rs.next()){
                Event event = new Event();
                event.setEventId(rs.getInt("event_id"));
                event.setEventName(rs.getString("event_name"));
                event.setCommentNum(rs.getInt("comment_num"));
                event.setLikesNum(rs.getInt("likes_num"));
                event.setCollectionNum(rs.getInt("collection_num"));
                event.setCreateTime(rs.getDate("create_time"));
                events.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                JdbcUtils.release(conn,ps,rs);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //把查询的结果集返回到service层
        return events;
    }
}
