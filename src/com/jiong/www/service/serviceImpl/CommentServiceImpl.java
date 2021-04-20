package com.jiong.www.service.serviceImpl;

import com.jiong.www.dao.daoImpl.UserDaoImpl;
import com.jiong.www.dao.daoImpl.CommentDaoImpl;
import com.jiong.www.dao.dao.ICommentDao;
import com.jiong.www.po.Comment;
import com.jiong.www.service.service.ICommentService;
import com.jiong.www.util.EventPagingUtils;

import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static com.jiong.www.util.DbcpUtils.getConnection;

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
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            iCommentDao.doComment(conn,userId,eventId,comment);
            iCommentDao.addCommentNum(conn,eventId);
            conn.commit();
        } catch (SQLException e) {
            try {
                assert conn != null;
                conn.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            e.printStackTrace();
        }
    }
    /**删除评论，同时删除用户评论表中的相关数据,用于普通用户的删除*/
    @Override
    public void doCancel(int commentId, int eventId){
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            iCommentDao.doCancel(conn,commentId,eventId);
            iCommentDao.subtractCommentNum(conn,eventId);
            conn.commit();
        } catch (SQLException e) {
            try {
                assert conn != null;
                conn.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            e.printStackTrace();
        }
    }
    /**删除瓜的所有评论,管理员*/
    @Override
    public void doClear(int eventId){
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            iCommentDao.doClear(conn,eventId);
            iCommentDao.clearCommentNum(conn,eventId);
            conn.commit();
        } catch (SQLException e) {
            try {
                assert conn != null;
                conn.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            e.printStackTrace();
        }
    }
    /**查看瓜的评论,也要返回评论人名*/
    @Override
    public List<Comment> findAll(int eventId){
        List<Comment> comments;
        //创建一个容器返回 评论的信息
        comments = iCommentDao.findAll(eventId);
        for(Comment comment:comments){
            comment.setCommenterName(new UserDaoImpl().queryUserInformation(comment.getCommenterId()).getLoginName());
        }
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
    /**进行删除，添加评论后表格刷新*/
    @Override
    public  List<Comment> doRefresh(List<Comment> comments, DefaultTableModel defaultTableModel,int eventId,String[] columnNames,EventPagingUtils eventPagingUtils) {
        comments=findAll(eventId);
        Object[][] rowData1 = new Object[comments.size()][3];
        for (int i = 0; i < comments.size(); i++) {
            rowData1[i][0]=comments.get(i).getCommenterName();
            rowData1[i][1]=comments.get(i).getCommentContent();
            rowData1[i][2]=comments.get(i).getCommentTime().toString();
        }
        //重新设置数据源
        defaultTableModel.setDataVector(rowData1,columnNames);
        eventPagingUtils.setList(comments);
        eventPagingUtils.setDefaultTableModel(defaultTableModel);
        return comments;
    }


}
