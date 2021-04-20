package com.jiong.www.dao.daoImpl;

import com.jiong.www.dao.dao.IEventDao;
import com.jiong.www.po.Event;


import org.apache.commons.dbutils.handlers.BeanHandler;
import static com.jiong.www.util.DbcpUtils.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mono
 */
public class EventDaoImpl implements IEventDao {
    /**创建瓜，添加瓜信息到瓜表*/
    @Override
    public int doCreate(int userId, int eventGroupId, Event event) {
        int row = 0;
        Object[] params={eventGroupId,userId,event.getEventName(),event.getEventContent()};
        String sql ="INSERT INTO `event`(`eventGroup_id`,`publisher_id`,`event_name`,`event_content`) VALUES(?,?,?,?)";
        try {
            row=queryRunner.execute(sql, params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //sql语句返回结果判断
        //row是返回值，用于判断
        return row;
    }
    /**验证瓜名是否存在*/
    @Override
    public int verifyExist(String eventName)  {
        int row=0;
        String sql="SELECT `event_id` AS eventId FROM `event`WHERE `event_name`=?";
        try {
            Event query = queryRunner.query(sql, new BeanHandler<>(Event.class), eventName);
            if(query!=null){
                row=1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row;
        //抛出到view层判断,1则无数据，0则有数据
    }
    /**删除瓜时验证是不是用户发的瓜*/
    @Override
    public int doVerify(int userId, int eventId)  {
        int row=0;
        //默认不是用户的瓜
        Object[] params={userId,eventId};
        String sql="SELECT `event_name` AS eventName FROM `event` WHERE `publisher_id` = ? AND `event_id`  = ?";
        try {
            Event query = queryRunner.query(sql, new BeanHandler<>(Event.class), params);
            if(query!=null){
                row=1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row;
        //抛出到view层判断
    }
    /**查询这个瓜所在的瓜圈名*/
    @Override
    public int queryGroupId(int eventId)  {
        int eventGroupId =0;
        String sql ="SELECT `eventGroup_id` AS eventGroupId FROM `event` WHERE `event_id` = ?";
        try {
            Event query = queryRunner.query(sql, new BeanHandler<>(Event.class), eventId);
            if(query!=null){
                eventGroupId=query.getEventGroupId();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventGroupId;
    }
    /**删除瓜*/
    @Override
    public int doDelete(Connection conn,int eventId){
        int row=0;
        String sql ="DELETE FROM `event` WHERE `event_id` =?";
        try {
            row=queryRunner.execute(conn,sql,eventId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row;
    }
    /**查看瓜和搜索瓜,返回瓜的所有信息，封装,返回eventId作为参数给其他方法用*/
    @Override
    public Event doView(String eventName) {
        Event eventQuery = new Event();
        String sql = "SELECT `publisher_id` AS publisherId,`event_content` AS eventContent,`comment_num` AS commentNum,\n" +
                    "`likes_num` AS likesNum,`create_time` AS createTime,`collection_num` AS collectionNum," +
                    "`event_id` AS eventId FROM `event` WHERE `event_name` = ?";
        try {
            Event query = queryRunner.query(sql, new BeanHandler<>(Event.class), eventName);
            if(query!=null){
                eventQuery=query;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventQuery;
    }
    /**用于查看收藏点赞合集时用瓜id查看信息*/
    @Override
    public List<Event> doView(List<Integer> list) {
        List<Event> eventList = new ArrayList<>();
        for (Integer integer : list) {
            String sql = "SELECT `publisher_id` AS publisherId,`event_name` AS eventName,`event_content` AS eventContent,`comment_num` AS commentNum,\n" +
                    "`likes_num` AS likesNum,`create_time` AS createTime,`collection_num` AS collectionNum FROM `event` WHERE `event_id` = ?";
            try {
                Event query = queryRunner.query(sql, new BeanHandler<>(Event.class), integer);
                eventList.add(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return eventList;
    }
    /**查看瓜名*/
    @Override
    public String queryName(int eventId) {
        String eventName=null;
        String sql="SELECT `event_name` AS eventName FROM `event` WHERE `event_id`=?";
        try {
            Event query = queryRunner.query(sql, new BeanHandler<>(Event.class), eventId);
            eventName=query.getEventName();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventName;
    }

}
