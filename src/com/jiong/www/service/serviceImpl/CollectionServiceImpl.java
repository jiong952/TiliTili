package com.jiong.www.service.serviceImpl;

import com.jiong.www.dao.dao.ICollectionDao;
import com.jiong.www.dao.dao.IEventDao;
import com.jiong.www.dao.daoImpl.UserDaoImpl;
import com.jiong.www.dao.daoImpl.CollectionDaoImpl;
import com.jiong.www.dao.daoImpl.EventDaoImpl;
import com.jiong.www.po.Event;
import com.jiong.www.service.ServiceException;
import com.jiong.www.service.service.ICollectionService;
import static com.jiong.www.util.MyDsUtils.*;
import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Mono
 */
public class CollectionServiceImpl implements ICollectionService {
    ICollectionDao iCollectionDao = new CollectionDaoImpl();
    IEventDao iEventDao = new EventDaoImpl();
    /**收藏,同时更新收藏表*/
    @Override
    public void collect(int userId, int eventId){
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            iCollectionDao.collect(conn,userId,eventId);
            iCollectionDao.addNum(conn,eventId);
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                throw new ServiceException("数据回滚异常",e);
            }
            e.printStackTrace();
            throw new ServiceException("收藏异常",e);
        }
    }
    /**取消收藏,同时删除用户收藏表中的相关数据*/
    @Override
    public void cancelCollect(int userId, int eventId){
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            iCollectionDao.cancelCollect(conn,userId,eventId);
            iCollectionDao.subtractNum(conn,eventId);
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                throw new ServiceException("数据回滚异常",e);
            }
            e.printStackTrace();
            throw new ServiceException("取消收藏异常",e);
        }
    }
    /**查看用户是否点赞*/
    @Override
    public int isCollect(int userId, int eventId){
        int judge;
        judge= iCollectionDao.isCollect(userId,eventId);
        return judge;
    }
    /**查看收藏合集 每个瓜只展示事件标题 作者 发布时间 点赞量 收藏量 评论量*/
    @Override
    public List<Event> findAll(int userId){
        List<Event> events;
        List<Integer> integers = iCollectionDao.findAll(userId);
        events = iEventDao.findSome(integers);
        for(Event event:events){
            if(event!=null){
                event.setPublisherName(new UserDaoImpl().queryInformation(event.getPublisherId()).getLoginName());
            }
        }
        return events;
    }
    /**刷新列表信息*/
    @Override
    public DefaultListModel<String> refresh(int userId) {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        List<Event> events1 = findAll(userId);
        for (int i = 0; i < events1.size(); i++) {
            listModel.add(i,events1.get(i).getName());
        }
        return listModel;
    }

}
