package com.jiong.www.dao.daoImpl;

import com.jiong.www.dao.dao.IEventGroupDao;
import com.jiong.www.po.Event;
import com.jiong.www.po.EventGroup;
import com.jiong.www.util.DbcpUtils;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import static com.jiong.www.util.DbcpUtils.*;
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
        Object[] params ={eventGroup.getEventGroupName(),eventGroup.getEventGroupDescription()};
        String sql ="INSERT INTO `eventgroup`(`eventGroup_name`,`eventGroup_description`) VALUES(?,?)";
        try {
            row=queryRunner.execute(sql, params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //row是返回值，用于判断
        return row;
        //向上抛出到view层
    }
    /**把瓜圈和管理员联系起来*/
    @Override
    public void groupOfAdmin(int userId, EventGroup eventGroup){
        Object[] params={userId,eventGroup.getEventGroupName()};
        String sql ="INSERT INTO `administrator`(`administrator_id`,`administrator_groupid`)VALUES(?,(SELECT `eventGroup_id`FROM `eventgroup` WHERE `eventGroup_name`=?))";
        try {
            queryRunner.execute(sql, params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**验证瓜圈名是否存在,避免发生重复*/
    @Override
    public int verifyExist(String eventGroupName) {
        int row=0;
        String sql="SELECT `eventGroup_id` AS eventGroupId FROM `eventgroup` WHERE `eventGroup_name`=?";
        try {
            EventGroup query = queryRunner.query(sql, new BeanHandler<>(EventGroup.class), eventGroupName);
            if(query!=null){
                row=1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row;
        //抛出到view层判断
    }
    /**验证是否是该管理员管理的瓜圈*/
    @Override
    public int verifyOfAdmin(int userId, int eventGroupId) {
        int row=0;
        //0表示不是
        Object[] params ={userId,eventGroupId};
        String sql="SELECT `id` FROM `administrator` WHERE `administrator_id` = ? AND `administrator_groupid` = ?";
        try {
            Object query = queryRunner.query(sql, new ScalarHandler<>());
            if(query!=null){
                row=1;
                //表里有数据则是该管理员管的瓜圈row=1
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row;
    }
    /**删除瓜圈*/
    @Override
    public int doDelete(String deleteEventGroupName)  {
        int row = 0;
        String sql ="DELETE FROM `eventgroup` WHERE `eventGroup_name`=?";
        try {
            row=queryRunner.execute(sql,deleteEventGroupName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row;
        //向上抛出到view层
    }
    /**删除在管理员表与瓜圈的数据*/
    @Override
    public void doDeleteOfAdmin(int eventGroupId, int userId){
        Object[] params={eventGroupId,userId};
        //删除瓜圈与管理员的关系
        String sql ="DELETE FROM `administrator`WHERE `administrator_id`=? AND `administrator_groupid`=?";
        try {
            queryRunner.execute(sql, params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**用瓜圈名查该瓜圈信息*/
    @Override
    public EventGroup viewEventGroup(String eventGroupName){
        EventGroup eventGroup = new EventGroup();
        String sql ="SELECT `eventGroup_description` AS eventGroupDescription,`eventGroup_id` AS eventGroupId FROM `eventgroup` WHERE `eventGroup_name` = ?";
        try {
            EventGroup query = queryRunner.query(sql, new BeanHandler<>(EventGroup.class), eventGroupName);
            if(query!=null){
                eventGroup=query;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventGroup;
    }
    /**用瓜圈id查瓜圈名*/
    @Override
    public String viewEventGroup(int eventGroupId){
        String eventGroupName=null;
        String sql ="SELECT `eventGroup_name`  AS eventGroupName FROM `eventgroup` WHERE `eventGroup_id`=?";
        try {
            EventGroup query = queryRunner.query(sql, new BeanHandler<>(EventGroup.class), eventGroupId);
            if(query!=null){
                eventGroupName=query.getEventGroupName();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //把查询的结果集返回到service层
        return eventGroupName;
    }
    /**查看管理员管理的所有瓜圈的瓜圈id*/
    @Override
    public List<Integer> viewAdminGroup(int userId){
        List<Integer> list = new ArrayList<>();
        String sql = "SELECT `administrator_groupid` FROM `administrator` WHERE `administrator_id` = ?";
        try {
            list=queryRunner.query(sql,new ColumnListHandler<Integer>(),userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    /**用一组瓜圈id查该瓜圈里的所有瓜的瓜信息*/
    @Override
    public List<Event> viewEventGroup(List<Integer> list){
        List<Event> eventList = new ArrayList<>();
        for (Integer integer : list) {
            String sql = "SELECT `event_id` AS eventId,`event_name` AS eventName FROM `event` WHERE `eventGroup_id` = ?";
            try {
                Event query = queryRunner.query(sql, new BeanHandler<>(Event.class), integer);
                if(query!=null){
                    eventList.add(query);
                }
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
        String sql ="SELECT `eventGroup_name` AS eventGroupName,`eventGroup_description` AS eventGroupDescription FROM `eventgroup`";
        try {
            eventGroups = queryRunner.query(sql, new BeanListHandler<>(EventGroup.class));
        } catch (SQLException e) {
            e.printStackTrace();
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
            conn = DbcpUtils.getConnection();
            String sql ="SELECT `event_id`,`event_name`,`comment_num`,`likes_num`,`collection_num`,`publisher_id`,`create_time` \n" +
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
                event.setPublisherId(rs.getInt("publisher_id"));
                events.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                DbcpUtils.release(conn,ps,rs);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //把查询的结果集返回到service层
        return events;
    }
}
