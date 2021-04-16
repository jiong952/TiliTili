package com.jiong.www.service.serviceImpl;

import com.jiong.www.dao.daoImpl.CommentDaoImpl;
import com.jiong.www.dao.Idao.ICommentDao;
import com.jiong.www.po.Comment;
import com.jiong.www.service.Iservice.ICommentService;

import java.util.List;

/**
 * @author Mono
 */
public class CommentServiceImpl implements ICommentService {
    ICommentDao iCommentDao = new CommentDaoImpl();
    /**进行评论，评论数+1，评论表更新*/
    @Override
    public void doComment(int userId, int eventId, String commentContent){
        Comment comment = new Comment();
        comment.setCommentContent(commentContent);
        iCommentDao.doComment(userId,eventId,comment);
    }
    /**删除评论，同时删除用户评论表中的相关数据,用于普通用户的删除*/
    @Override
    public void doCancel(int commentId, int eventId){
        iCommentDao.doCancel(commentId,eventId);
    }
    /**删除瓜的所有评论,管理员*/
    @Override
    public void doClear(int eventId){
        iCommentDao.doClear(eventId);

    }
    /**查看瓜的评论,也要返回评论人名*/
    @Override
    public List<Comment> findAll(int eventId){
        List<Comment> comments;
        //创建一个容器返回 评论的信息
         comments = iCommentDao.findAll(eventId);
        return comments;
    }
    /**第一页的数据处理*/
    @Override
    public Object[][] doDataProcess(int pageSize, List<Comment> comments) {
        Object[][] rowData = new Object[Math.min(comments.size(), pageSize)][3];
        if(comments.size()>=pageSize){
            for (int i = 0; i < pageSize; i++) {
                rowData[i][0]=comments.get(i).getCommenterName();
                rowData[i][1]=comments.get(i).getCommentContent();
                rowData[i][2]=comments.get(i).getCommentTime().toString();
            }
        }else {
            for (int i = 0; i < comments.size(); i++) {
                rowData[i][0]=comments.get(i).getCommenterName();
                rowData[i][1]=comments.get(i).getCommentContent();
                rowData[i][2]=comments.get(i).getCommentTime().toString();
            }
        }

        return rowData;
    }
    /**刷新评论列表*/
    @Override
    public Object[][] doRefresh(int eventId) {
        List<Comment> comments = findAll(eventId);
        Object[][] rowData = new Object[comments.size()][3];
        for (int i = 0; i < comments.size(); i++) {
            rowData[i][0]=comments.get(i).getCommenterName();
            rowData[i][1]=comments.get(i).getCommentContent();
            rowData[i][2]=comments.get(i).getCommentTime().toString();
        }
        return rowData;
    }

}