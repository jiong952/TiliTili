package com.jiong.www.dao.daoImpl;

import com.jiong.www.dao.DaoException;
import com.jiong.www.dao.dao.IEventGroupDao;
import com.jiong.www.po.Event;
import com.jiong.www.po.EventGroup;

import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import static com.jiong.www.util.MyDsUtils.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mono
 */
public class EventGroupDaoImpl implements IEventGroupDao {

    /**创建瓜圈,添加瓜圈信息到瓜圈表*/
    @Override
    public int doCreate(Connection conn, EventGroup eventGroup) {
        int row;
        Object[] params ={eventGroup.getEventGroupName(),eventGroup.getEventGroupDescription()};
        String sql ="INSERT INTO `eventgroup`(`eventGroup_name`,`eventGroup_description`) VALUES(?,?)";
        try {
            row=queryRunner.execute(conn,sql, params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("创建瓜圈异常",e);
        }
        //row是返回值，用于判断
        return row;
        //向上抛出到view层
    }
    /**把瓜圈和管理员联系起来*/
    @Override
    public void groupOfAdmin(Connection conn,int userId, EventGroup eventGroup){
        Object[] params={userId,eventGroup.getEventGroupName()};
        String sql ="INSERT INTO `administrator`(`administrator_id`,`administrator_groupid`)VALUES(?,(SELECT `eventGroup_id`FROM `eventgroup` WHERE `eventGroup_name`=?))";
        try {
            queryRunner.execute(conn,sql, params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("更新管理员表异常",e);
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
            throw new DaoException("查看瓜圈名是否存在异常",e);
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
            Object query = queryRunner.query(sql, new ScalarHandler<>(),params);
            if(query!=null){
                row=1;
                //表里有数据则是该管理员管的瓜圈row=1
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("查看是否是管理员所管理瓜圈异常",e);
        }
        return row;
    }
    /**删除瓜圈*/
    @Override
    public int doDelete(Connection conn,String deleteEventGroupName)  {
        int row;
        String sql ="DELETE FROM `eventgroup` WHERE `eventGroup_name`=?";
        try {
            row=queryRunner.execute(conn,sql,deleteEventGroupName);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("删除瓜圈异常",e);
        }
        return row;
        //向上抛出到view层
    }
    /**删除在管理员表与瓜圈的数据*/
    @Override
    public void doDeleteOfAdmin(Connection conn,int eventGroupId, int userId){
        Object[] params={eventGroupId,userId};
        //删除瓜圈与管理员的关系
        String sql ="DELETE FROM `administrator`WHERE `administrator_id`=? AND `administrator_groupid`=?";
        try {
            queryRunner.execute(conn,sql, params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("删除管理员表数据异常",e);
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
            throw new DaoException("用瓜圈名查该瓜圈信息异常",e);
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
            throw new DaoException("用瓜圈id查该瓜名异常",e);
        }
        //把查询的结果集返回到service层
        return eventGroupName;
    }
    /**查看管理员管理的所有瓜圈的瓜圈id*/
    @Override
    public List<Integer> viewAdminGroup(int userId){
        List<Integer> list;
        String sql = "SELECT `administrator_groupid` FROM `administrator` WHERE `administrator_id` = ?";
        try {
            list=queryRunner.query(sql, new ColumnListHandler<>(),userId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("查看管理员管理的所有瓜圈的瓜圈id异常",e);
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
                List<Event> query = queryRunner.query(sql, new BeanListHandler<>(Event.class), integer);
                if(query!=null){
                    eventList.addAll(query);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DaoException("用一组瓜圈id查该瓜圈里的所有瓜的瓜信息异常",e);
            }
        }
        return eventList;
    }
    /**查看系统所有瓜圈*/
    @Override
    public List<EventGroup> viewAllEventGroup()  {
        List<EventGroup> eventGroups;
        String sql ="SELECT `eventGroup_name` AS eventGroupName,`eventGroup_description` AS eventGroupDescription FROM `eventgroup`";
        try {
            eventGroups = queryRunner.query(sql, new BeanListHandler<>(EventGroup.class));
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("查看所有瓜圈异常",e);
        }
        //把查询的结果集返回到service层
        return eventGroups;
    }
    /**用瓜圈id查看瓜圈里的所有瓜信息*/
    @Override
    public List<Event> viewEventOfEventGroup(int eventGroupId)  {
        List<Event> events;
        String sql ="SELECT `event_id` AS eventId,`event_name` AS eventName,`comment_num` AS commentNum,`likes_num` AS likesNum," +
                "`collection_num` AS collectionNum,`publisher_id` AS publisherId,`create_time` AS createTime FROM `event` WHERE `eventGroup_id` = ?";
        try {
            events=queryRunner.query(sql,new BeanListHandler<>(Event.class),eventGroupId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("用瓜圈id查看瓜圈里的所有瓜信息异常",e);
        }
        //把查询的结果集返回到service层
        return events;
    }
    /**查看瓜圈所属管理员id*/
    @Override
    public int queryAdmin(int eventGroupId) {
        int adminId;
        String sql="SELECT `administrator_id` FROM `administrator` WHERE `administrator_groupid`=?";
        try {
            adminId=queryRunner.query(sql,new ScalarHandler<>(),eventGroupId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("查看瓜圈所属管理员id异常",e);
        }
        return adminId;
    }
}
