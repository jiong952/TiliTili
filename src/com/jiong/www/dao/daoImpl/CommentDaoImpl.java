package com.jiong.www.dao.daoImpl;

import com.jiong.www.dao.dao.ICommentDao;
import com.jiong.www.po.Comment;
import com.jiong.www.util.DbcpUtils;
import com.jiong.www.util.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mono
 */
public class CommentDaoImpl implements ICommentDao {
    /**进行评论*/
    @Override
    public void doComment(int userId, int eventId, Comment comment)  {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DbcpUtils.getConnection();
            String sql="INSERT INTO `comment` (`event_id`,`user_id`,`comment_content`) VALUES(?,?,?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,eventId);
            ps.setInt(2,userId);
            ps.setString(3,comment.getCommentContent());
            ps.executeUpdate();
            //sql语句返回结果判断
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                DbcpUtils.release(conn,ps,null);
                //释放连接
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**评论数+1*/
    @Override
    public void addCommentNum(int eventId) {
        Connection conn = null;
        PreparedStatement ps =null;
        try {
            conn = DbcpUtils.getConnection();
            String sql ="UPDATE `event` SET `comment_num` = `comment_num`+1 WHERE `event_id` =?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,eventId);
            ps.executeUpdate();
            //sql语句返回结果判断
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                DbcpUtils.release(conn,ps,null);
                //释放连接
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**删除评论，同时删除用户评论表中的相关数据,用于普通用户的删除*/
    @Override
    public void doCancel(int commentId, int eventId) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DbcpUtils.getConnection();
            String sql="DELETE FROM `comment` WHERE `event_id`= ? AND `id` =?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,eventId);
            ps.setInt(2,commentId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                DbcpUtils.release(conn,ps,null);
                //释放连接
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**评论数-1*/
    @Override
    public void subtractCommentNum(int eventId) {
        Connection conn = null;
        PreparedStatement ps =null;
        try {
            conn = DbcpUtils.getConnection();
            String sql ="UPDATE `event` SET `comment_num` = `comment_num`-1 WHERE `event_id` =?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,eventId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                DbcpUtils.release(conn,ps,null);
                //释放连接
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**删除瓜的所有评论，管理员*/
    @Override
    public void doClear(int eventId){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DbcpUtils.getConnection();
            String sql="DELETE FROM `comment` WHERE `event_id`= ? ";
            //清空所有评论
            ps = conn.prepareStatement(sql);
            ps.setInt(1,eventId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                DbcpUtils.release(conn,ps,null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //释放连接
        }
    }
    /**评论数清0*/
    @Override
    public void clearCommentNum(int eventId) {
        Connection conn = null;
        PreparedStatement ps =null;
        try {
            conn = DbcpUtils.getConnection();
            String sql="UPDATE `event` SET `comment_num` = 0 WHERE `event_id` =?";
            //清空所有评论
            ps = conn.prepareStatement(sql);
            ps.setInt(1,eventId);
            ps.executeUpdate();
            //sql语句返回结果判断
            //row是返回值，用于判断 0表示执行失败,1表示执行成功
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                DbcpUtils.release(conn,ps,null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //释放连接
        }
    }

    /**查看瓜的评论,也要返回评论人名*/
    @Override
    public List<Comment> findAll(int eventId){
        List<Comment> comments = new ArrayList<>();
        //创建一个容器返回 评论的信息
        Connection conn = null;
        PreparedStatement ps =null;
        ResultSet rs = null;
        try {
            conn = DbcpUtils.getConnection();
            String sql ="SELECT `comment_content`,`user_id`,`create_time`,`id` FROM `comment`WHERE`event_id`=?";
            //联表查询
            ps = conn.prepareStatement(sql);
            ps.setInt(1,eventId);
            rs = ps.executeQuery();
            while(rs.next()){
                Comment comment = new Comment();
                comment.setCommentContent(rs.getString("comment_content"));
                comment.setCommenterId(rs.getInt("user_id"));
                comment.setCommentTime(rs.getDate("create_time"));
                comment.setCommentId(rs.getInt("id"));
                comments.add(comment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                DbcpUtils.release(conn,ps,rs);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //把查询的结果集返回到service层
        return comments;

    }
}
