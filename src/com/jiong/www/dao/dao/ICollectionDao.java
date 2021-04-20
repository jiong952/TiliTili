package com.jiong.www.dao.dao;

import java.sql.Connection;
import java.util.List;

/**
 * @author Mono
 */
public interface ICollectionDao {
    /**收藏,同时更新收藏表
     * @param userId 收藏人id
     * @param eventId 收藏瓜id
     * @param conn 连接
     * */
    void doCollect(Connection conn,int userId, int eventId);
    /**收藏数+1
     * @param  eventId 点赞瓜
     * @param conn 连接
     * */
    void addCollectionNum(Connection conn,int eventId);
    /**取消收藏,同时删除用户收藏表中的相关数据
     * @param userId 取消收藏人id
     * @param eventId 取消收藏瓜id
     * @param conn 连接
     * */
    void doCancelCollect(Connection conn,int userId, int eventId);
    /**收藏数-1
     * @param  eventId 收藏瓜
     * @param conn 连接
     * */
    void subtractCollectionNum(Connection conn,int eventId);
    /**清空瓜的收藏
     * @param eventId 瓜id
     * @param conn 连接
     * */
    void doClear(Connection conn,int eventId);
    /**查看用户是否收藏
     * @param userId 用户id
     * @param eventId 查看的瓜
     * @return 查看判断
     * */
    int queryCollect(int userId, int eventId);
    /**查看收藏合集 每个瓜只展示事件标题 作者 发布时间 点赞量 收藏量 评论量
     * @param userId 用户id
     * @return 返回查询结果*/
    List<Integer> findAll(int userId);
}
