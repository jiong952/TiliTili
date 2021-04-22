package com.jiong.www.service.serviceImpl;

import com.jiong.www.dao.daoImpl.EventGroupDaoImpl;
import com.jiong.www.dao.dao.IEventGroupDao;
import com.jiong.www.dao.daoImpl.UserDaoImpl;
import com.jiong.www.po.Event;
import com.jiong.www.po.EventGroup;
import com.jiong.www.service.service.IEventGroupService;
import com.jiong.www.service.service.IEventService;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static com.jiong.www.util.MyDsUtils.*;

/**
 * @author Mono
 */
public class EventGroupServiceImpl implements IEventGroupService {

    IEventGroupDao iEventGroupDao = new EventGroupDaoImpl();
    IEventService iEventService = new EventServiceImpl();
    /**创建瓜圈*/
    @Override
    public int doCreate(int userId, String eventGroupName, String eventGroupDescription){
        int row = 0;
        // 用于接收dao层的返回值
        //封装eventGroup对象
        EventGroup eventGroup = new EventGroup();
        eventGroup.setEventGroupName(eventGroupName);
        eventGroup.setEventGroupDescription(eventGroupDescription);
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            row= iEventGroupDao.doCreate(conn,eventGroup);
            iEventGroupDao.groupOfAdmin(conn,userId,eventGroup);
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            e.printStackTrace();
        }
        return row;
    }
    /**验证瓜圈名是否存在*/
    @Override
    public int verifyExist(String eventGroupName){
        int row;
        //默认0不存在
        row= iEventGroupDao.verifyExist(eventGroupName);
        //0则无数据，1则有数据
        return row;

    }
    /**验证是否是该管理员管理的瓜圈*/
    @Override
    public int verifyOfAdmin(int userId, String eventGroupName){
        int row;
        //默认0不是管理员管理的瓜圈
        EventGroup eventGroup;
        eventGroup = iEventGroupDao.viewEventGroup(eventGroupName);
        row= iEventGroupDao.verifyOfAdmin(userId,eventGroup.getEventGroupId());
        return row;
    }
    /**删除瓜圈，同时在管理员所管理的数据删除关系，删除瓜圈里的瓜*/
    @Override
    public int doDelete(String deleteEventGroupName, int userId){
        int row = 0;
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            //删除瓜圈里的瓜
            int eventGroupId = iEventGroupDao.viewEventGroup(deleteEventGroupName).getEventGroupId();
            List<Event> events = iEventGroupDao.viewEventOfEventGroup(eventGroupId);
            for(Event event:events){
                iEventService.doDelete(event.getEventId());
            }
            //清除管理员表瓜圈的数据
            iEventGroupDao.doDeleteOfAdmin(conn, eventGroupId,userId);
            //删除瓜圈
            row= iEventGroupDao.doDelete(conn,deleteEventGroupName);
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            e.printStackTrace();
        }
        return row;
    }
    /**查看所有瓜圈*/
    @Override
    public List<EventGroup> findAll(){
        List<EventGroup> eventGroups;
        eventGroups= iEventGroupDao.viewAllEventGroup();
        return eventGroups;
    }
    /**用瓜圈名查该瓜圈信息*/
    @Override
    public EventGroup viewEventGroup(String eventGroupName){
        EventGroup eventGroup;
        eventGroup= iEventGroupDao.viewEventGroup(eventGroupName);
        return eventGroup;
    }
    /**用瓜圈名查看瓜圈里的所有瓜*/
    @Override
    public List<Event> viewEventOfEventGroup(String eventGroupName){
        List<Event> events ;
        events = iEventGroupDao.viewEventOfEventGroup(iEventGroupDao.viewEventGroup(eventGroupName).getEventGroupId());
        for(Event event:events){
            event.setPublisherName(new UserDaoImpl().queryUserInformation(event.getPublisherId()).getLoginName());
        }
        return events;
    }
    /**瓜网的第一页的数据处理*/
    @Override
    public void  doDataProcess(int pageSize, DefaultListModel<String> listModel, List<EventGroup> eventGroups) {
        //每一页页面的展示瓜圈数目
        if(eventGroups.size()>= pageSize){
            for (int i = 0; i < pageSize; i++) {
                listModel.add(i,eventGroups.get(i).getEventGroupName());
            }
        }else {
            for (int i = 0; i < eventGroups.size(); i++) {
                listModel.add(i,eventGroups.get(i).getEventGroupName());
            }
        }
    }
    /**删除增加之后刷新数据*/
    @Override
    public void   doRefresh(List<EventGroup> eventGroups,DefaultListModel<String> defaultListModel) {
        //刷新
        List<EventGroup> eventGroups1 = findAll();
        defaultListModel.clear();
        for (int i = 0; i < eventGroups1.size(); i++) {
            defaultListModel.add(i,eventGroups1.get(i).getEventGroupName());
        }
        //向列表框中加入所有的瓜圈名
        eventGroups.clear();
        eventGroups.addAll(eventGroups1);

    }

    /**
     * 瓜圈的第一页的数据处理
     */
    @Override
    public void dataProcessGroup(int pageSize, DefaultListModel<String> listModel, List<Event> events) {
        //每一页页面的展示瓜数目
        if(events.size()>=pageSize){
            for (int i = 0; i < pageSize; i++) {
                listModel.add(i,events.get(i).getEventName());
            }
        }else {
            for (int i = 0; i < events.size(); i++) {
                listModel.add(i,events.get(i).getEventName());
            }
        }
    }

    /**
     * 刷新数据源
     */
    @Override
    public void refreshGroup(List<Event> events, DefaultListModel<String> defaultListModel, String eventGroupName) {
        //刷新
        List<Event> events1 = viewEventOfEventGroup(eventGroupName);
        defaultListModel.clear();
        for (int i = 0; i < events1.size(); i++) {
            defaultListModel.add(i,events1.get(i).getEventName());
        }
        //向列表框中加入所有的瓜圈名
        events.clear();
        events.addAll(events1);
    }

    /**
     * 查询瓜圈管理员id
     *
     * @param eventGroupName 瓜圈名
     * @return 管理员id
     */
    @Override
    public int queryAdmin(String eventGroupName) {
        int adminId;
        EventGroup eventGroup = iEventGroupDao.viewEventGroup(eventGroupName);
        adminId=iEventGroupDao.queryAdmin(eventGroup.getEventGroupId());
        return adminId;
    }
}
