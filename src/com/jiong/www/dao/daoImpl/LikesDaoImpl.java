package com.jiong.www.dao.daoImpl;

import com.jiong.www.dao.dao.ILikesDao;
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
public class LikesDaoImpl implements ILikesDao {

    /**点赞,同时更新用户点赞表*/
    @Override
    public void doLikes(int userId, int eventId) {
        Connection conn = null;
        PreparedStatement ps=null;
        try {
            conn = JdbcUtils.getConnection();
            String sql="INSERT INTO `like` (`event_id`,`user_id`) VALUES(?,?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,eventId);
            ps.setInt(2,userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                JdbcUtils.release(conn,ps,null);
                //释放连接
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    /**点赞量+1*/
    @Override
    public void addCollectionNum(int eventId) {
        Connection conn = null;
        PreparedStatement ps=null;
        try {
            conn = JdbcUtils.getConnection();
            String sql ="UPDATE `event` SET `likes_num` = `likes_num`+1 WHERE `event_id` =?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,eventId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                JdbcUtils.release(conn,ps,null);
                //释放连接
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    /**取消点赞,同时删除用户点赞表中的相关数据*/
    @Override
    public void doCancelLikes(int userId, int eventId)  {
        Connection conn = null;
        PreparedStatement ps=null;
        try {
            conn = JdbcUtils.getConnection();
            String sql="DELETE FROM `like`  WHERE `event_id`= ? AND `user_id` =?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,eventId);
            ps.setInt(2,userId);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                JdbcUtils.release(conn,ps,null);
                //释放连接
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    /**点赞量-1*/
    @Override
    public void subtractCollectionNum(int eventId) {
        Connection conn = null;
        PreparedStatement ps=null;
        try {
            conn = JdbcUtils.getConnection();
            String sql ="UPDATE `event` SET `likes_num` = `likes_num`-1 WHERE `event_id` =?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,eventId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                JdbcUtils.release(conn,ps,null);
                //释放连接
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    /**清空瓜的点赞数据*/
    @Override
    public void doClear(int eventId) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JdbcUtils.getConnection();
            String sql="DELETE FROM `like` WHERE `event_id`= ? ";
            //清空所有评论
            ps = conn.prepareStatement(sql);
            ps.setInt(1,eventId);
            ps.executeUpdate();
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
    }
    /**查看用户是否点赞*/
    @Override
    public int queryLikes(int userId, int eventId) {
        int judge=0;
        Connection conn = null;
        PreparedStatement ps= null;
        try {
            conn = JdbcUtils.getConnection();
            String sql="SELECT `id` FROM `like`  WHERE `event_id`= ? AND `user_id` =?";
            ps = conn.prepareStatement(sql);
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
    /**查看点赞合集 每个瓜只展示事件标题 作者 发布时间 点赞量 收藏量 评论量*/
    @Override
    public List<Integer> findAll(int userId)  {
        List<Integer> list = new ArrayList<>();
        //创建一个容器返回 收藏瓜的信息
        Connection conn = null;
        PreparedStatement ps =null;
        ResultSet rs = null;
        try {
            conn = JdbcUtils.getConnection();
            String sql ="SELECT `event_id` FROM `like` WHERE `user_id` =?";
            //联表查询
            ps = conn.prepareStatement(sql);
            ps.setInt(1,userId);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(rs.getInt("event_id"));
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
        return list;
    }
}
