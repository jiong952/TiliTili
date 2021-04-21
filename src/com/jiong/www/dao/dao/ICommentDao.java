package com.jiong.www.dao.dao;

import com.jiong.www.po.Comment;

import java.sql.Connection;
import java.util.List;

/**
 * @author Mono
 */
public interface ICommentDao {
    /**进行评论，评论数+1，评论表更新
     * @param userId 评论人
     * @param eventId 评论的瓜
     * @param conn 连接
     * @param comment 评论信息*/
    void doComment(Connection conn,int userId, int eventId, Comment comment);
    /**收藏数+1
     * @param  eventId 点赞瓜
     * @param conn 连接
     * */
    void addCommentNum(Connection conn,int eventId);
    /**删除评论，同时删除用户评论表中的相关数据,用于普通用户的删除
     * @param commentId 取消评论人id
     * @param eventId 取消评论瓜id
     * @param conn 连接*/

    void doCancel(Connection conn,int commentId, int eventId);
    /**收藏数-1
     * @param  eventId 点赞瓜
     * @param conn 连接
     * */
    void subtractCommentNum(Connection conn,int eventId);
    /**删除瓜的所有评论，管理员
     * @param  eventId 瓜id
     * @param conn 连接*/
    void doClear(Connection conn,int eventId);
    /**收藏数清0
     * @param  eventId 点赞瓜
     * @param conn 连接
     * */
    void clearCommentNum(Connection conn,int eventId);
    /**查看瓜的评论,也要返回评论人名
     * @param  eventId 瓜id
     * @return 所有评论信息*/
    List<Comment> findAll(int eventId);
    /**查看某一评论的信息
     * @param commentId 评论id
     * @return 评论信息
     * */
    Comment doView(int commentId);
}
