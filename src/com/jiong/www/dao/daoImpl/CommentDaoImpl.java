package com.jiong.www.dao.daoImpl;

import com.jiong.www.dao.DaoException;
import com.jiong.www.dao.dao.ICommentDao;
import com.jiong.www.po.Comment;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import static com.jiong.www.util.MyDsUtils.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mono
 */
public class CommentDaoImpl implements ICommentDao {
    /**进行评论*/
    @Override
    public void comment(Connection conn, int userId, int eventId, Comment comment)  {
        Object[] params ={eventId,userId,comment.getCommentContent()};
        String sql="INSERT INTO `comment` (`event_id`,`user_id`,`comment_content`) VALUES(?,?,?)";
        try {
            queryRunner.execute(conn,sql,params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("评论异常",e);
        }
    }

    /**评论数+1*/
    @Override
    public void addNum(Connection conn, int eventId) {
        String sql ="UPDATE `event` SET `comment_num` = `comment_num`+1 WHERE `event_id` =?";
        try {
            queryRunner.execute(conn,sql,eventId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("评论数+1异常",e);
        }
    }

    /**删除评论，同时删除用户评论表中的相关数据,用于普通用户的删除*/
    @Override
    public void cancelComment(Connection conn, int commentId, int eventId) {
        Object[] params ={eventId,commentId};
        String sql="DELETE FROM `comment` WHERE `event_id`= ? AND `id` =?";
        try {
            queryRunner.execute(conn,sql, params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("删除评论异常",e);
        }
    }

    /**评论数-1*/
    @Override
    public void subtractNum(Connection conn, int eventId) {
        String sql ="UPDATE `event` SET `comment_num` = `comment_num`-1 WHERE `event_id` =?";
        try {
            queryRunner.execute(conn,sql,eventId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("评论数-1异常",e);
        }
    }

    /**删除瓜的所有评论，管理员*/
    @Override
    public void clearAll(Connection conn, int eventId){
        String sql="DELETE FROM `comment` WHERE `event_id`= ? ";
        //清空所有评论
        try {
            queryRunner.execute(conn,sql,eventId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("删除瓜的所有评论异常",e);
        }
    }
    /**评论数清0*/
    @Override
    public void clearNum(Connection conn, int eventId) {
        String sql="UPDATE `event` SET `comment_num` = 0 WHERE `event_id` =?";
        //评论数清0
        try {
            queryRunner.execute(conn,sql,eventId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("评论数清0异常",e);
        }
    }

    /**查看瓜的评论*/
    @Override
    public List<Comment> findAll(int eventId){
        List<Comment> comments;
        //创建一个集合返回 评论的信息
        String sql ="SELECT `comment_content` AS commentContent,`user_id` AS commenterId,`create_time` AS commentTime," +
                "`id` AS commentId FROM `comment`WHERE`event_id`=?";
        //把查询的结果集返回到service层
        try {
            comments=queryRunner.query(sql,new BeanListHandler<>(Comment.class),eventId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("查看瓜的评论异常",e);
        }
        return comments;

    }

    /**
     * 查看某一评论的信息
     *
     * @param commentId 评论id
     * @return 评论信息
     */
    @Override
    public Comment find(int commentId) {
        Comment query=new Comment();
        String sql ="SELECT `comment_content` AS commentContent,`user_id` AS commenterId,`create_time` AS commentTime FROM `comment`WHERE`id`=?";
        try {
            query = queryRunner.query(sql, new BeanHandler<>(Comment.class), commentId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("查看特定评论异常",e);
        }
        return query;
    }
}
