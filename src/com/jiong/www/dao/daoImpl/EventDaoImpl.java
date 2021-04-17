package com.jiong.www.dao.daoImpl;

import com.jiong.www.dao.Idao.IEventDao;
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
public class EventDaoImpl implements IEventDao {
    /**创建瓜，添加瓜信息到瓜表*/
    @Override
    public int doCreate(int userId, int eventGroupId, Event event) {
        int row = 0;
        Connection conn = null;
        PreparedStatement ps=null;
        try {
            conn = JdbcUtils.getConnection();
            String sql ="INSERT INTO `event`(`eventGroup_id`,`publisher_id`,`event_name`,`event_content`) VALUES(?,?,?,?)";
            // event_name加了唯一约束，在数据库设计上可以防止重名
            ps = conn.prepareStatement(sql);
            ps.setInt(1,eventGroupId);
            ps.setInt(2,userId);
            ps.setString(3,event.getEventName());
            ps.setString(4,event.getEventContent());
            row= ps.executeUpdate();
            //sql语句返回结果判断
            //row是返回值，用于判断
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                JdbcUtils.release(conn,ps,null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //释放连接
        return row;
        //向上抛出到view层
    }
    /**验证瓜名是否存在*/
    @Override
    public int verifyExist(String eventName)  {
        int row=0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs =null;
        try {
            conn = JdbcUtils.getConnection();
            String sql="SELECT `event_id` FROM `event`WHERE `event_name`=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1,eventName);
            rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                row=1;
                //row=0则表里无数据
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
        return row;
        //抛出到view层判断,1则无数据，0则有数据
    }
    /**删除瓜时验证是不是用户发的瓜*/
    @Override
    public int doVerify(int userId, int eventId)  {
        int row=0;
        //默认不是用户的瓜
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs =null;
        try {
            conn = JdbcUtils.getConnection();
            String sql="SELECT `event_name` FROM `event` WHERE `publisher_id` = ? AND `event_id`  = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,userId);
            ps.setInt(2,eventId);
            rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                row=1;
                //表里有数据,是用户的瓜则row=1
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
        return row;
        //抛出到view层判断
    }
    /**查询这个瓜所在的瓜圈名*/
    @Override
    public String queryGroupName(int eventId)  {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs =null;
        String eventGroupName = null;
        try {
            conn = JdbcUtils.getConnection();
            //进行数据库连接
            String sql ="SELECT `eventGroup_name`\n" +
                    "FROM `eventgroup` s\n" +
                    "INNER JOIN `event` p\n" +
                    "ON s.`eventGroup_id`=p.`eventGroup_id`\n" +
                    "WHERE `event_id`  = ?";
            //联表查询
            ps = conn.prepareStatement(sql);
            ps.setInt(1,eventId);
            rs = ps.executeQuery();
            //eventGroupName为查询的瓜圈名
            while (rs.next()){
                eventGroupName=rs.getString("eventGroup_name");
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
        return eventGroupName;
    }
    /**删除瓜*/
    @Override
    public int doDelete(int eventId){
        int row=0;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JdbcUtils.getConnection();
            //进行数据库连接
            String sql ="DELETE FROM `event` WHERE `event_id` =?";
            //还要删除点赞收藏评论举报表
            ps = conn.prepareStatement(sql);
            ps.setInt(1,eventId);
            row = ps.executeUpdate();
            //row为1删除成功,0删除失败
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                JdbcUtils.release(conn,ps,null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return row;
    }
    /**查看瓜和搜索瓜,返回瓜的所有信息，封装,返回eventId作为参数给其他方法用*/
    @Override
    public Event doView(String eventName) {
        Event eventQuery = new Event();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JdbcUtils.getConnection();
            String sql ="SELECT `login_name`,`event_name`,`event_content`,`comment_num`,`likes_num`,s.`create_time`,`event_id`,`collection_num`\n" +
                    "FROM `event` s\n" +
                    "INNER JOIN `user` p\n" +
                    "ON s.publisher_id= p.user_id\n" +
                    "WHERE `event_name`=?";
            //联表查询
            ps = conn.prepareStatement(sql);
            ps.setString(1,eventName);
            rs = ps.executeQuery();
            while(rs.next()){
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
        return eventQuery;
    }

    @Override
    public List<Event> doView(List<Integer> list) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Event> eventList = new ArrayList<>();
        try {
            conn = JdbcUtils.getConnection();
            for (Integer integer : list) {
                String sql = "SELECT `publisher_id`,`event_name`,`event_content`,`comment_num`,\n" +
                        "`likes_num`,`create_time`,`collection_num` FROM `event` WHERE `event_id` = ?";
                //联表查询
                ps = conn.prepareStatement(sql);
                ps.setInt(1, integer);
                rs = ps.executeQuery();
                while (rs.next()) {
                    Event event = new Event();
                    event.setPublisherId(rs.getInt("publisher_id"));
                    //发布者的名字
                    event.setEventName(rs.getString("event_name"));
                    //瓜名
                    event.setEventContent(rs.getString("event_content"));
                    //瓜内容
                    event.setLikesNum(rs.getInt("likes_num"));
                    event.setCollectionNum(rs.getInt("collection_num"));
                    event.setCommentNum(rs.getInt("comment_num"));
                    //点赞收藏评论数
                    event.setCreateTime(rs.getDate("create_time"));
                    //瓜id
                    event.setEventId(integer);
                    eventList.add(event);
                }
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
        return eventList;
    }

}
