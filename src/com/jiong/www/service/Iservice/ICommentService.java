package com.jiong.www.service.Iservice;

import com.jiong.www.po.Comment;
import com.jiong.www.util.EventPagingUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    List<Comment> doRefresh (List<Comment> comments,DefaultTableModel defaultTableModel,int eventId,String[] columnNames,EventPagingUtils eventPagingUtils);
}
