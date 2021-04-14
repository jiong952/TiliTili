package com.jiong.www.service;

import com.jiong.www.dao.CollectionDao;
import com.jiong.www.po.Event;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mono
 */
public class CollectionService {
    CollectionDao collectionDao = new CollectionDao();
    /**收藏,同时更新收藏表*/
    public void collection(int userId,int eventId){
        try {
            collectionDao.collection(userId,eventId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**取消收藏,同时删除用户收藏表中的相关数据*/
    public void cancelCollection(int userId,int eventId){
        try {
            collectionDao.cancelCollection(userId,eventId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**查看用户是否点赞*/
    public int collectionIfOrNot(int commentId,int eventId){
        int judge=0;
        try {
            judge=collectionDao.collectionIfOrNot(commentId,eventId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return judge;
    }
    /**查看收藏合集 每个瓜只展示事件标题 作者 发布时间 点赞量 收藏量 评论量*/
    public List<Event> viewEventOfCollection(int userId){
        List<Event> events = new ArrayList<>();
        try {
            events=collectionDao.viewEventOfCollection(userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

}
