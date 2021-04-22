package com.jiong.www.service.serviceImpl;

import com.jiong.www.dao.daoImpl.EventGroupDaoImpl;
import com.jiong.www.dao.daoImpl.LikesDaoImpl;
import com.jiong.www.dao.daoImpl.UserDaoImpl;
import com.jiong.www.dao.daoImpl.AccuseDaoImpl;
import com.jiong.www.dao.daoImpl.CollectionDaoImpl;
import com.jiong.www.dao.daoImpl.CommentDaoImpl;
import com.jiong.www.dao.daoImpl.EventDaoImpl;
import com.jiong.www.dao.dao.IEventDao;
import com.jiong.www.po.Event;
import com.jiong.www.service.ServiceException;
import com.jiong.www.service.service.IEventService;

import java.sql.Connection;
import java.sql.SQLException;

import static com.jiong.www.util.MyDsUtils.*;


/**
 * @author Mono
 */
public class EventServiceImpl implements IEventService {
    IEventDao iEventDao = new EventDaoImpl();
    EventGroupDaoImpl eventGroupDaoImpl =new EventGroupDaoImpl();
    /**创建瓜*/
    @Override
    public int create(int userId, int eventGroupId, String eventName, String eventContent){
        // 用于接收dao层的返回值
        //封装event对象
        Event event = new Event();
        event.setName(eventName);
        event.setEventContent(eventContent);
        return iEventDao.create(userId,eventGroupId,event);
    }
    /**验证瓜名是否存在*/
    @Override
    public int isExist(String eventName){
        return iEventDao.isExist(eventName);
    }
    /**验证这个瓜是不是用户发的*/
    @Override
    public int isCreate(int userId, int eventId){
        return iEventDao.isCreate(userId,eventId);
    }
    /**查询这个瓜所在的瓜圈名*/
    @Override
    public String queryName(int eventId){
        String eventGroupName;
        int eventGroupId = iEventDao.queryId(eventId);
        eventGroupName= eventGroupDaoImpl.queryName(eventGroupId);
        return eventGroupName;
    }
    /**删除瓜以及瓜的点赞收藏评论举报信息*/
    @Override
    public int delete(int eventId){
        Connection conn = null;
        int judge;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            judge= iEventDao.delete(conn,eventId);
            new CollectionDaoImpl().clearAll(conn,eventId);
            new CommentDaoImpl().clearAll(conn,eventId);
            new AccuseDaoImpl().clearAll(conn,eventId);
            new LikesDaoImpl().clearAll(conn,eventId);
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                throw new ServiceException("数据回滚异常",e);
            }
            e.printStackTrace();
            throw new ServiceException("删除瓜异常",e);
        }
        return judge;
    }
    /**查看瓜,返回瓜的所有信息，封装*/
    @Override
    public Event find(String eventName){
        Event event = iEventDao.find(eventName);
        event.setPublisherName(new UserDaoImpl().queryInformation(event.getPublisherId()).getLoginName());
        return event;
    }
}
