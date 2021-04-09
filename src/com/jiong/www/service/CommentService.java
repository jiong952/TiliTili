package com.jiong.www.service;

import com.jiong.www.dao.CommentDao;
import com.jiong.www.po.Comment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommentService {
    CommentDao commentDao = new CommentDao();
    //进行评论，评论数+1，评论表更新
    public void comment(int userId,int eventId,String commentContent){
        Comment comment = new Comment();
        comment.setCommentContent(commentContent);
        try {
            commentDao.comment(userId,eventId,comment);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //删除评论，同时删除用户评论表中的相关数据,用于普通用户的删除
    public void cancelComment(int userId,int eventId){
        try {
            commentDao.cancelComment(userId,eventId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //删除瓜的所有评论,管理员
    public void clearComment(int eventId){
        try {
            commentDao.clearComment(eventId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //查看瓜的评论,也要返回评论人名
    public List<Comment> viewComment(int eventId){
        List<Comment> comments = new ArrayList<Comment>();
        //创建一个容器返回 评论的信息
        try {
            comments = commentDao.viewComment(eventId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }

}
