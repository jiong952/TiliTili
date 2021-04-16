package com.jiong.www.service.serviceImpl;

import com.jiong.www.dao.Idao.ICollectionDao;
import com.jiong.www.dao.daoImpl.CollectionDaoImpl;
import com.jiong.www.po.Event;
import com.jiong.www.service.Iservice.ICollectionService;

import java.util.List;

/**
 * @author Mono
 */
public class CollectionServiceImpl implements ICollectionService {
    ICollectionDao iCollectionDao = new CollectionDaoImpl();
    /**收藏,同时更新收藏表*/
    @Override
    public void doCollect(int userId, int eventId){
        iCollectionDao.doCollect(userId,eventId);
    }
    /**取消收藏,同时删除用户收藏表中的相关数据*/
    @Override
    public void doCancelCollect(int userId, int eventId){
        iCollectionDao.doCancelCollect(userId,eventId);

    }
    /**查看用户是否点赞*/
    @Override
    public int queryCollect(int commentId, int eventId){
        int judge;
        judge= iCollectionDao.queryCollect(commentId,eventId);
        return judge;
    }
    /**查看收藏合集 每个瓜只展示事件标题 作者 发布时间 点赞量 收藏量 评论量*/
    @Override
    public List<Event> findAll(int userId){
        List<Event> events;
        events= iCollectionDao.findAll(userId);
        return events;
    }

}
