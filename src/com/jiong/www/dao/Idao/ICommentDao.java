package com.jiong.www.dao.Idao;

import com.jiong.www.po.Comment;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Mono
 */
public interface ICommentDao {
    /**进行评论，评论数+1，评论表更新
     * @param userId 评论人
     * @param eventId 评论的瓜
     * @param comment 评论信息*/
    void doComment(int userId, int eventId, Comment comment);
    /**收藏数+1
     * @param  eventId 点赞瓜
     * */
    void addCommentNum(int eventId);
    /**删除评论，同时删除用户评论表中的相关数据,用于普通用户的删除
     * @param commentId 取消评论人id
     * @param eventId 取消评论瓜id*/
    void doCancel(int commentId, int eventId);
    /**收藏数-1
     * @param  eventId 点赞瓜
     * */
    void subtractCommentNum(int eventId);
    /**删除瓜的所有评论，管理员
     * @param  eventId 瓜id*/
    void doClear(int eventId);
    /**收藏数清0
     * @param  eventId 点赞瓜
     * */
    void clearCommentNum(int eventId);
    /**查看瓜的评论,也要返回评论人名
     * @param  eventId 瓜id
     * @return 所有评论信息*/
    List<Comment> findAll(int eventId);
}
