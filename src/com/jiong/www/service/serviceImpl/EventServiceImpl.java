package com.jiong.www.service.serviceImpl;

import com.jiong.www.dao.daoImpl.EventDaoImpl;
import com.jiong.www.dao.Idao.IEventDao;
import com.jiong.www.po.Event;
import com.jiong.www.service.Iservice.IEventService;


/**
 * @author Mono
 */
public class EventServiceImpl implements IEventService {
    IEventDao iEventDao = new EventDaoImpl();
    /**创建瓜*/
    @Override
    public int doCreate(int userId, int eventGroupId, String eventName, String eventContent){
        // 用于接收dao层的返回值
        //封装event对象
        Event event = new Event();
        event.setEventName(eventName);
        event.setEventContent(eventContent);
        return iEventDao.doCreate(userId,eventGroupId,event);
    }
    /**验证瓜名是否存在*/
    @Override
    public int verifyExist(String eventName){
        return iEventDao.verifyExist(eventName);
    }
    /**验证这个瓜是不是用户发的*/
    @Override
    public int doVerify(int userId, int eventId){
        return iEventDao.doVerify(userId,eventId);
    }
    /**查询这个瓜所在的瓜圈名*/
    @Override
    public String queryGroupName(int eventId){
        return iEventDao.queryGroupName(eventId);
    }
    /**删除瓜*/
    @Override
    public int doDelete(int eventId){
        return iEventDao.doDelete(eventId);
    }
    /**查看瓜,返回瓜的所有信息，封装*/
    @Override
    public Event doView(String eventName){
        return iEventDao.doView(eventName);
    }

}
