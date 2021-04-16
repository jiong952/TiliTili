package com.jiong.www.service;

import com.jiong.www.dao.EventDaoImpl;
import com.jiong.www.dao.IEventDao;
import com.jiong.www.po.Event;


/**
 * @author Mono
 */
public class EventServiceImpl implements IEventService {
    IEventDao iEventDao = new EventDaoImpl();
    /**创建瓜*/
    @Override
    public int doCreate(int userId, int eventGroupId, String eventName, String eventContent){
        int row;
        // 用于接收dao层的返回值
        //封装event对象
        Event event = new Event();
        event.setEventName(eventName);
        event.setEventContent(eventContent);
        row= iEventDao.doCreate(userId,eventGroupId,event);
        return row;
    }
    /**验证瓜名是否存在*/
    @Override
    public int verifyExist(String eventName){
        int row;
        //默认0不存在
        row= iEventDao.verifyExist(eventName);
        //1则有数据，0无数据
        return row;
    }
    /**验证这个瓜是不是用户发的*/
    @Override
    public int doVerify(int userId, int eventId){
        int row;
        //row=1是用户发的,row=0不是
        row= iEventDao.doVerify(userId,eventId);
        //row=1是用户发的,row=0不是
        return row;
    }
    /**查询这个瓜所在的瓜圈名*/
    @Override
    public String queryGroupName(int eventId){
        String eventGroupName;
        //eventGroupName为查询的瓜圈名
        eventGroupName= iEventDao.queryGroupName(eventId);
        return eventGroupName;
    }
    /**删除瓜*/
    @Override
    public int doDelete(int eventId){
        int row;
        row= iEventDao.doDelete(eventId);
        return row;
    }
    /**查看瓜,返回瓜的所有信息，封装*/
    @Override
    public Event doView(String eventName){
        Event eventQuery = iEventDao.doView(eventName);
        return eventQuery;
    }

}
