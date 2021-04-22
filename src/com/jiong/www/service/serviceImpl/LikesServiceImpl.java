package com.jiong.www.service.serviceImpl;

import com.jiong.www.dao.dao.ILikesDao;
import com.jiong.www.dao.dao.IEventDao;
import com.jiong.www.dao.daoImpl.LikesDaoImpl;
import com.jiong.www.dao.daoImpl.UserDaoImpl;
import com.jiong.www.dao.daoImpl.EventDaoImpl;
import com.jiong.www.po.Event;
import com.jiong.www.service.ServiceException;
import com.jiong.www.service.service.ILikesService;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static com.jiong.www.util.MyDsUtils.*;

/**
 * @author Mono
 */
public class LikesServiceImpl implements ILikesService {
    ILikesDao iLikesDao = new LikesDaoImpl();
    IEventDao iEventDao = new EventDaoImpl();
    /**点赞,同时更新用户点赞表*/
    @Override
    public void likes(int userId, int eventId){
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            iLikesDao.likes(conn,userId,eventId);
            iLikesDao.addNum(conn,eventId);
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                throw new ServiceException("数据回滚异常",e);
            }
            e.printStackTrace();
            throw new ServiceException("点赞异常",e);
        }
    }
    /**取消点赞,同时删除用户点赞表中的相关数据*/
    @Override
    public void cancelLikes(int userId, int eventId){
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            iLikesDao.cancelLikes(conn,userId,eventId);
            iLikesDao.subtractNum(conn,eventId);
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                throw new ServiceException("数据回滚异常",e);
            }
            e.printStackTrace();
            throw new ServiceException("取消点赞异常",e);
        }
    }
    /**查看用户是否点赞*/
    @Override
    public int isLikes(int userId, int eventId)  {
        int judge;
        judge= iLikesDao.isLikes(userId,eventId);
        return  judge;
    }
    /**查看点赞合集 每个瓜只展示事件标题 作者 发布时间 点赞量 收藏量 评论量*/
    @Override
    public List<Event> findAll(int userId){
        List<Event> events ;
        List<Integer> integers = iLikesDao.findAll(userId);
        events=iEventDao.findSome(integers);
        for(Event event:events){
            event.setPublisherName(new UserDaoImpl().queryInformation(event.getPublisherId()).getLoginName());
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
