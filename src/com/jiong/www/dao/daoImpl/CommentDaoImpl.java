package com.jiong.www.dao.daoImpl;

import com.jiong.www.dao.Idao.ICommentDao;
import com.jiong.www.po.Comment;
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
    /**进行评论，评论数+1，评论表更新*/
    @Override
    public void doComment(int userId, int eventId, Comment comment)  {
        Connection conn = null;
        PreparedStatement ps =null;
        PreparedStatement ps1 = null;
        try {
            conn = JdbcUtils.getConnection();
            //事务
            conn.setAutoCommit(false);
            String sql ="UPDATE `event` SET `comment_num` = `comment_num`+1 WHERE `event_id` =?";
            String sql1="INSERT INTO `comment` (`event_id`,`user_id`,`comment_content`) VALUES(?,?,?)";
            ps = conn.prepareStatement(sql);
            ps1 = conn.prepareStatement(sql1);
            ps.setInt(1,eventId);
            ps1.setInt(1,eventId);
            ps1.setInt(2,userId);
            ps1.setString(3,comment.getCommentContent());
            ps.executeUpdate();
            ps1.executeUpdate();
            conn.commit();
            //sql语句返回结果判断
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                JdbcUtils.release(conn,ps,null);
                JdbcUtils.release(conn,ps1,null);
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
        PreparedStatement ps =null;
        PreparedStatement ps1 = null;
        try {
            conn = JdbcUtils.getConnection();
            conn.setAutoCommit(false);
            String sql ="UPDATE `event` SET `comment_num` = `comment_num`-1 WHERE `event_id` =?";
            String sql1="DELETE FROM `comment` WHERE `event_id`= ? AND `id` =?";
            ps = conn.prepareStatement(sql);
            ps1 = conn.prepareStatement(sql1);
            ps.setInt(1,eventId);
            ps1.setInt(1,eventId);
            ps1.setInt(2,commentId);
            ps.executeUpdate();
            ps1.executeUpdate();
            //sql语句返回结果判断
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                JdbcUtils.release(conn,ps,null);
                JdbcUtils.release(conn,ps1,null);
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
        PreparedStatement ps2 =null;
        try {
            conn = JdbcUtils.getConnection();
            String sql="DELETE FROM `comment` WHERE `event_id`= ? ";
            String sql2="UPDATE `event` SET `comment_num` = 0 WHERE `event_id` =?";
            //清空所有评论
            ps = conn.prepareStatement(sql);
            ps2 = conn.prepareStatement(sql2);
            ps.setInt(1,eventId);
            ps2.setInt(1,eventId);
            ps.executeUpdate();
            ps2.executeUpdate();
            //sql语句返回结果判断
            //row是返回值，用于判断 0表示执行失败,1表示执行成功
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                JdbcUtils.release(conn,ps,null);
                JdbcUtils.release(conn,ps2,null);
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
            conn = JdbcUtils.getConnection();
            String sql ="SELECT `login_name`,`comment_content`,p.`user_id`,p.`create_time`,`id`\n" +
                    "FROM `user` s\n" +
                    "INNER JOIN `comment` p\n" +
                    "ON s.`user_id`=p.`user_id`\n" +
                    "WHERE `event_id`=?";
            //联表查询
            ps = conn.prepareStatement(sql);
            ps.setInt(1,eventId);
            rs = ps.executeQuery();
            while(rs.next()){
                Comment comment = new Comment();
                comment.setCommentContent(rs.getString("comment_content"));
                comment.setCommenterName(rs.getString("login_name"));
                comment.setCommenterId(rs.getInt("user_id"));
                comment.setCommentTime(rs.getDate("create_time"));
                comment.setCommentId(rs.getInt("id"));
                comments.add(comment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                JdbcUtils.release(conn,ps,rs);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //把查询的结果集返回到service层
        return comments;

    }
}
