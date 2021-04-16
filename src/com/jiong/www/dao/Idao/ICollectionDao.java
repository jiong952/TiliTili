package com.jiong.www.dao.Idao;

import com.jiong.www.po.Event;

import java.util.List;

/**
 * @author Mono
 */
public interface ICollectionDao {
    /**收藏,同时更新收藏表
     * @param userId 收藏人id
     * @param eventId 收藏瓜id
     * */
    void doCollect(int userId, int eventId);
    /**取消收藏,同时删除用户收藏表中的相关数据
     * @param userId 取消收藏人id
     * @param eventId 取消收藏瓜id
     * */
    void doCancelCollect(int userId, int eventId);
    /**查看用户是否点赞
     * @param userId 用户id
     * @param eventId 查看的瓜
     * @return 查看判断
     * */
    int queryCollect(int userId, int eventId);
    /**查看收藏合集 每个瓜只展示事件标题 作者 发布时间 点赞量 收藏量 评论量
     * @param userId 用户id
     * @return 返回查询结果*/
    List<Event> findAll(int userId);
}