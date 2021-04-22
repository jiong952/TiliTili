package com.jiong.www.dao.daoImpl;

import com.jiong.www.dao.dao.ILikesDao;

import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;



import static com.jiong.www.util.MyDsUtils.*;
import java.sql.Connection;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mono
 */
public class LikesDaoImpl implements ILikesDao {

    /**点赞,同时更新用户点赞表*/
    @Override
    public void doLikes(Connection conn,int userId, int eventId) {
        Object[] params ={eventId,userId};
        String sql="INSERT INTO `like` (`event_id`,`user_id`) VALUES(?,?)";
        try {
            queryRunner.execute(conn,sql,params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**点赞量+1*/
    @Override
    public void addCollectionNum(Connection conn,int eventId) {
        String sql ="UPDATE `event` SET `likes_num` = `likes_num`+1 WHERE `event_id` =?";
        try {
            queryRunner.execute(conn,sql,eventId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**取消点赞,同时删除用户点赞表中的相关数据*/
    @Override
    public void doCancelLikes(Connection conn,int userId, int eventId)  {
        Object[] params={eventId,userId};
        String sql="DELETE FROM `like`  WHERE `event_id`= ? AND `user_id` =?";
        try {
            queryRunner.execute(conn,sql, params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**点赞量-1*/
    @Override
    public void subtractCollectionNum(Connection conn,int eventId) {
        String sql ="UPDATE `event` SET `likes_num` = `likes_num`-1 WHERE `event_id` =?";
        try {
            queryRunner.execute(conn,sql,eventId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**清空瓜的点赞数据*/
    @Override
    public void doClear(Connection conn,int eventId) {
        String sql="DELETE FROM `like` WHERE `event_id`= ? ";
        //清空点赞数据
        try {
            queryRunner.execute(conn,sql,eventId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**查看用户是否点赞*/
    @Override
    public int queryLikes(int userId, int eventId) {
        int judge=0;
        Object[] params={eventId, userId};
        String sql="SELECT `id` FROM `like`  WHERE `event_id`= ? AND `user_id` =?";
        try {
            Object query = queryRunner.query(sql, new ScalarHandler<>(),params);
            if(query!=null){
                //有数据，有点赞
                judge=1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return judge;
    }
    /**查看点赞合集 每个瓜只展示事件标题 作者 发布时间 点赞量 收藏量 评论量*/
    @Override
    public List<Integer> findAll(int userId)  {
        List<Integer> list = new ArrayList<>();
        //创建一个集合返回 收藏瓜的信息
        String sql ="SELECT `event_id` AS eventId FROM `like` WHERE `user_id` =?";
        try {
            list=queryRunner.query(sql, new ColumnListHandler<>(),userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
