package com.jiong.www.dao.daoImpl;

import com.jiong.www.dao.Idao.ICollectionDao;
import com.jiong.www.po.Event;
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
public class CollectionDaoImpl implements ICollectionDao {
    /**收藏,同时更新收藏表*/
    @Override
    public void doCollect(int userId, int eventId)  {
        Connection conn = null;
        PreparedStatement ps=null;
        PreparedStatement ps1=null;
        try {
            conn = JdbcUtils.getConnection();
            conn.setAutoCommit(false);
            String sql ="UPDATE `event` SET `collection_num` = `collection_num`+1 WHERE `event_id` =?";
            String sql1="INSERT INTO `collection` (`event_id`,`user_id`) VALUES(?,?)";
            ps = conn.prepareStatement(sql);
            ps1 = conn.prepareStatement(sql1);
            ps.setInt(1,eventId);
            ps1.setInt(1,eventId);
            ps1.setInt(2,userId);
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
    /**取消收藏,同时删除用户收藏表中的相关数据*/
    @Override
    public void doCancelCollect(int userId, int eventId) {
        Connection conn = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        try {
            conn = JdbcUtils.getConnection();
            conn.setAutoCommit(false);
            String sql ="UPDATE `event` SET `collection_num` = `collection_num`-1 WHERE `event_id` =?";
            String sql1="DELETE FROM `collection`  WHERE `event_id`= ? AND `user_id` =?";
            ps = conn.prepareStatement(sql);
            ps1 = conn.prepareStatement(sql1);
            ps.setInt(1,eventId);
            ps1.setInt(1,eventId);
            ps1.setInt(2,userId);
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
    /**查看用户是否点赞*/
    @Override
    public int queryCollect(int userId, int eventId)  {
        int judge=0;
        Connection conn = null;
        PreparedStatement ps= null;
        try {
            conn = JdbcUtils.getConnection();
            String sql1="SELECT `id` FROM `collection`  WHERE `event_id`= ? AND `user_id` =?";
            ps = conn.prepareStatement(sql1);
            ps.setInt(1,eventId);
            ps.setInt(2,userId);
            ResultSet rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                judge=1;
                //表里有数据,是用户有收藏,judge=1
            }
            //sql语句返回结果判断
            //row是返回值，用于判断 0表示执行失败,1表示执行成功
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                JdbcUtils.release(conn,ps,null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //释放连接
        }
        return judge;
    }
    /**查看收藏合集 每个瓜只展示事件标题 作者 发布时间 点赞量 收藏量 评论量*/
    @Override
    public List<Event> findAll(int userId)  {
        List<Event> events = new ArrayList<>();
        //创建一个容器返回 收藏瓜的信息
        Connection conn = null;
        PreparedStatement ps =null;
        ResultSet rs = null;
        try {
            conn = JdbcUtils.getConnection();
            String sql ="SELECT `login_name`,`event_name`,`event_content`,`comment_num`,`likes_num`,s.`create_time`,s.`event_id`,`collection_num`\n" +
                    "FROM `collection` p\n" +
                    "INNER JOIN `event` s\n" +
                    "ON s.`event_id`=p.`event_id`\n" +
                    "INNER JOIN `user` k\n" +
                    "ON k.`user_id`=s.`publisher_id`\n" +
                    "WHERE p.`user_id`=?";
            //联表查询
            ps = conn.prepareStatement(sql);
            ps.setInt(1,userId);
            rs = ps.executeQuery();
            while(rs.next()){
                Event eventQuery = new Event();
                //event对象封装瓜的信息
                eventQuery.setPublisherName(rs.getString("login_name"));
                //发布者的名字
                eventQuery.setEventName(rs.getString("event_name"));
                //瓜名
                eventQuery.setEventContent(rs.getString("event_content"));
                //瓜内容
                eventQuery.setLikesNum(rs.getInt("likes_num"));
                eventQuery.setCollectionNum(rs.getInt("collection_num"));
                eventQuery.setCommentNum(rs.getInt("comment_num"));
                //点赞收藏评论数
                eventQuery.setCreateTime(rs.getDate("create_time"));
                //瓜id
                eventQuery.setEventId(rs.getInt("event_id"));
                events.add(eventQuery);
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
        return events;
    }

}
