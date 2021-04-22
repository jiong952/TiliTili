package com.jiong.www.service.service;

import com.jiong.www.po.Comment;
import com.jiong.www.util.CommentPagingUtils;

import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 * @author Mono
 */
public interface ICommentService {
    /**进行评论，评论数+1，评论表更新
     * @param userId 评论人
     * @param eventId 评论的瓜
     * @param commentContent 评论内容*/
    void doComment(int userId, int eventId, String commentContent);
    /**删除评论，同时删除用户评论表中的相关数据,用于普通用户的删除
     * @param commentId 取消评论人id
     * @param eventId 取消评论瓜id*/
    void doCancel(int commentId, int eventId);
    /**删除瓜的所有评论，管理员
     * @param  eventId 瓜id*/
    void doClear(int eventId);
    /**查看瓜的评论,也要返回评论人名
     * @param  eventId 瓜id
     * @return 所有评论信息*/
    List<Comment> findAll(int eventId);
    /**处理第一页评论的数据
     * @param pageSize 每一页的展示数目
     * @param comments 获得的瓜的评论信息
     * @return 返回第一页的评论信息
     * */
    Object[][] doDataProcess(int pageSize,List<Comment> comments);
    /**进行删除，添加评论后表格刷新
     * @param comments 评论数据
     * @param defaultTableModel 传入数据源
     * @param eventId 瓜id
     * @param columnNames 表头
     * @param commentPagingUtils 分页
     * @return 返回处理完的评论信息*/
    List<Comment> doRefresh (List<Comment> comments, DefaultTableModel defaultTableModel, int eventId, String[] columnNames, CommentPagingUtils commentPagingUtils);
    /**查看一条评论的信息
     * @param commentId 评论id
     * @return 评论信息
     * */
    Comment doView(int commentId);

}
