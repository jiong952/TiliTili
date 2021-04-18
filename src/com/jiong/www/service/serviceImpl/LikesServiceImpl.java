package com.jiong.www.service.serviceImpl;

import com.jiong.www.dao.dao.ILikesDao;
import com.jiong.www.dao.dao.IEventDao;
import com.jiong.www.dao.daoImpl.LikesDaoImpl;
import com.jiong.www.dao.daoImpl.UserDaoImpl;
import com.jiong.www.dao.daoImpl.EventDaoImpl;
import com.jiong.www.po.Event;
import com.jiong.www.service.service.ILikesService;

import javax.swing.*;
import java.util.List;

/**
 * @author Mono
 */
public class LikesServiceImpl implements ILikesService {
    ILikesDao iLikesDao = new LikesDaoImpl();
    IEventDao iEventDao = new EventDaoImpl();
    /**点赞,同时更新用户点赞表*/
    @Override
    public void doLikes(int userId, int eventId){
        iLikesDao.doLikes(userId,eventId);
        iLikesDao.addCollectionNum(eventId);
    }
    /**取消点赞,同时删除用户点赞表中的相关数据*/
    @Override
    public void doCancelLikes(int userId, int eventId){
        iLikesDao.doCancelLikes(userId,eventId);
        iLikesDao.subtractCollectionNum(eventId);
    }
    /**查看用户是否点赞*/
    @Override
    public int queryLikes(int userId, int eventId)  {
        int judge;
        judge= iLikesDao.queryLikes(userId,eventId);
        return  judge;
    }
    /**查看点赞合集 每个瓜只展示事件标题 作者 发布时间 点赞量 收藏量 评论量*/
    @Override
    public List<Event> findAll(int userId){
        List<Event> events ;
        List<Integer> integers = iLikesDao.findAll(userId);
        events=iEventDao.doView(integers);
        for(Event event:events){
            event.setPublisherName(new UserDaoImpl().queryUserInformation(event.getPublisherId()).getLoginName());
        }
        return events;
    }
    /**刷新列表信息*/
    @Override
    public DefaultListModel<String> doRefresh(int userId) {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        List<Event> events1 = findAll(userId);
        for (int i = 0; i < events1.size(); i++) {
            listModel.add(i,events1.get(i).getEventName());
        }
        return listModel;
    }
}
