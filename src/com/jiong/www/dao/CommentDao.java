package com.jiong.www.dao;

import com.jiong.www.po.Comment;
import com.jiong.www.util.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommentDao {
    //进行评论，评论数+1，评论表更新
    public void comment(int userId, int eventId, Comment comment) throws SQLException {
        Connection conn = JdbcUtils.getConnection();
        String sql ="UPDATE `event` SET `comment_num` = `comment_num`+1 WHERE `event_id` =?";
        String sql1="INSERT INTO `comment` (`event_id`,`user_id`,`comment_content`) VALUES(?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        PreparedStatement ps1 = conn.prepareStatement(sql1);
        ps.setInt(1,eventId);
        ps1.setInt(1,eventId);
        ps1.setInt(2,userId);
        ps1.setString(3,comment.getCommentContent());
        ps.executeUpdate();
        ps1.executeUpdate();
        //sql语句返回结果判断
        //row是返回值，用于判断
        JdbcUtils.release(conn,ps,null);
        JdbcUtils.release(conn,ps1,null);
        //释放连接
        //向上抛出到view层
    }
    //删除评论，同时删除用户评论表中的相关数据,用于普通用户的删除
    public void cancelComment(int userId,int eventId) throws SQLException{
        Connection conn = JdbcUtils.getConnection();
        String sql ="UPDATE `event` SET `comment_num` = `comment_num`-1 WHERE `event_id` =?";
        String sql1="DELETE FROM `comment` WHERE `event_id`= ? AND `user_id` =?";
        PreparedStatement ps = conn.prepareStatement(sql);
        PreparedStatement ps1 = conn.prepareStatement(sql1);
        ps.setInt(1,eventId);
        ps1.setInt(1,eventId);
        ps1.setInt(2,userId);
        ps.executeUpdate();
        ps1.executeUpdate();
        //sql语句返回结果判断
        //row是返回值，用于判断 0表示执行失败,1表示执行成功
        JdbcUtils.release(conn,ps,null);
        //释放连接
    }
    //删除瓜的所有评论，管理员
    public void clearComment(int eventId) throws SQLException {
        Connection conn = JdbcUtils.getConnection();
        String sql="DELETE FROM `comment` WHERE `event_id`= ? ";
        //清空所有评论
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1,eventId);
        ps.executeUpdate();
        //sql语句返回结果判断
        //row是返回值，用于判断 0表示执行失败,1表示执行成功
        JdbcUtils.release(conn,ps,null);
        //释放连接
    }
    //查看瓜的评论,也要返回评论人名
    public List<Comment> viewComment(int eventId) throws SQLException {
        List<Comment> comments = new ArrayList<Comment>();
        //创建一个容器返回 评论的信息
        Connection conn = JdbcUtils.getConnection();
        String sql ="SELECT `login_name`,`comment_content`,p.`user_id`,p.`create_time`\n" +
                "FROM `user` s\n" +
                "INNER JOIN `comment` p\n" +
                "ON s.`user_id`=p.`user_id`\n" +
                "WHERE `event_id`=?";
        //联表查询
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1,eventId);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            Comment comment = new Comment();
            comment.setCommentContent(rs.getString("comment_content"));
            comment.setCommenterName(rs.getString("login_name"));
            comment.setCommenterId(rs.getInt("user_id"));
            comment.setCommentTime(rs.getDate("create_time"));
            comments.add(comment);
        }
        JdbcUtils.release(conn,ps,rs);
        //把查询的结果集返回到service层
        return comments;

    }
}
