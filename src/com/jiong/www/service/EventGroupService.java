package com.jiong.www.service;

import com.jiong.www.dao.EventGroupDao;
import com.jiong.www.po.Event;
import com.jiong.www.po.EventGroup;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EventGroupService {
    private EventGroupDao eventGroupDao = new EventGroupDao();
    //创建瓜圈
    public int createEventGroup(int userId,String eventGroupName,String eventGroupDescription){
        int row=0;
        // 用于接收dao层的返回值
        //封装eventGroup对象
        EventGroup eventGroup = new EventGroup();
        eventGroup.setEventGroupName(eventGroupName);
        eventGroup.setEventGroupDescription(eventGroupDescription);
        try {
            row=eventGroupDao.createEventGroup(userId,eventGroup);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row;
    }
    //验证瓜圈名是否存在
    public int verifyEventGroupName(String eventGroupName){
        int row=0;
        //默认0不存在
        try {
            row=eventGroupDao.verifyEventGroupName(eventGroupName);
            //0则无数据，1则有数据
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row;

    }
    //验证是否是该管理员管理的瓜圈
    public int verifyEventGroupOfAdmin(int userId,String eventGroupName){
        int row=0;
        //默认0不是管理员管理的瓜圈
        try {
            row=eventGroupDao.verifyEventGroupOfAdmin(userId,eventGroupName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row;
    }
    //删除瓜圈，同时在管理员所管理的数据删除关系
    public int deleteEventGroup(String deleteEventGroupName,int userId){
        int row=0;
        // 用于接收dao层的返回值
        try {
            row=eventGroupDao.deleteEventGroup(deleteEventGroupName,userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row;
    }
    //查看所有瓜圈
    public List<EventGroup> viewAllEventGroup(){
        List<EventGroup> eventGroups = new ArrayList<EventGroup>();
        try {
            eventGroups=eventGroupDao.viewAllEventGroup();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventGroups;
    }
    //查看瓜圈
    public EventGroup viewEventGroup(String eventGroupName){
        EventGroup eventGroup = new EventGroup();
        try {
            eventGroup=eventGroupDao.viewEventGroup(eventGroupName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventGroup;
    }
    //查看瓜圈里的所有瓜
    public List<Event> viewEventOfEventGroup(String eventGroupName){
        List<Event> events = new ArrayList<Event>();
        try {
            events = eventGroupDao.viewEventOfEventGroup(eventGroupName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }
}
