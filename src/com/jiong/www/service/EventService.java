package com.jiong.www.service;

import com.jiong.www.dao.EventDao;
import com.jiong.www.po.Event;

import java.sql.SQLException;

public class EventService {
    EventDao eventDao = new EventDao();
    //创建瓜
    public int createEvent(int userId,int eventGroupId,String eventName,String eventContent){
        int row=0;
        // 用于接收dao层的返回值
        //封装event对象
        Event event = new Event();
        event.setEventName(eventName);
        event.setEventContent(eventContent);
        try {
            row=eventDao.createEvent(userId,eventGroupId,event);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row;
    }
    //验证瓜名是否存在
    public int verifyEventName(String eventGroupName){
        int row=0;
        //默认0不存在
        try {
            row=eventDao.verifyEventName(eventGroupName);
            //1则无数据，0则有数据
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row;
    }
    //验证这个瓜是不是用户发的
    public int verifyEventOfUser(int userId,int eventId){
        int row = 0;
        //row=1是用户发的,row=0不是
        try {
            row=eventDao.verifyEventOfUser(userId,eventId);
            //row=1是用户发的,row=0不是
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row;
    }
    //查询这个瓜所在的瓜圈名
    public String queryEventOfEventGroup(int eventId){
        String eventGroupName = null;
        //eventGroupName为查询的瓜圈名
        try {
            eventGroupName=eventDao.queryEventOfEventGroup(eventId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventGroupName;
    }

    //删除瓜
    public int deleteEvent(int eventId){
        int row=0;
        try {
            row=eventDao.deleteEvent(eventId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row;
    }
    //查看瓜,返回瓜的所有信息，封装
    public Event viewEvent(String eventName){
        Event eventQuery = new Event();
        try {
            eventQuery=eventDao.viewEvent(eventName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventQuery;
    }
    //用户可以举报瓜，给管理员处理
    public int accuseEvent(int eventId){
        int row=0;
        try {
            eventDao.accuseEvent(eventId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row;
    }


}
