package com.jiong.www.dao.daoImpl;

import com.jiong.www.dao.dao.ICollectionDao;
import com.jiong.www.po.Collection;

import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

import static com.jiong.www.util.MyDsUtils.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mono
 */
public class CollectionDaoImpl implements ICollectionDao {
    /**收藏,同时更新收藏表*/
    @Override
    public void doCollect(Connection conn,int userId, int eventId)  {
        Object[] params={eventId,userId};
        String sql="INSERT INTO `collection` (`event_id`,`user_id`) VALUES(?,?)";
        try {
            queryRunner.execute(conn,sql, params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**收藏量+1*/
    @Override
    public void addCollectionNum(Connection conn,int eventId) {
        String sql = "UPDATE `event` SET `collection_num` = `collection_num`+1 WHERE `event_id` =?";
        try {
            queryRunner.execute(conn,sql, eventId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**取消收藏,同时删除用户收藏表中的相关数据*/
    @Override
    public void doCancelCollect(Connection conn,int userId, int eventId) {
        Object[] params={eventId,userId};
        String sql="DELETE FROM `collection`  WHERE `event_id`= ? AND `user_id` =?";
        try {
            queryRunner.execute(conn,sql,params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**收藏量-1*/
    @Override
    public void subtractCollectionNum(Connection conn,int eventId) {
        String sql ="UPDATE `event` SET `collection_num` = `collection_num`-1 WHERE `event_id` =?";
        try {
            queryRunner.execute(conn,sql,eventId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**清除瓜相应收藏表数据*/
    @Override
    public void doClear(Connection conn,int eventId) {
        String sql="DELETE FROM `collection` WHERE `event_id`= ? ";
        //清空所有评论
        try {
            queryRunner.execute(conn,sql,eventId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**查看用户是否收藏*/
    @Override
    public int queryCollect(int userId, int eventId)  {
        int judge=0;
        Object[] params={eventId,userId};
        String sql="SELECT `id` FROM `collection`  WHERE `event_id`= ? AND `user_id` =?";
        Collection query = null;
        try {
            query = queryRunner.query(sql, new BeanHandler<>(Collection.class), params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(query!=null){
            judge=1;
            //表里有数据,是用户有收藏,judge=1
        }
        //sql语句返回结果判断
        return judge;
    }
    /**查看收藏合集 每个瓜只展示事件标题 作者 发布时间 点赞量 收藏量 评论量*/
    @Override
    public List<Integer> findAll(int userId)  {
        List<Integer> list = new ArrayList<>();
        //创建一个集合返回 收藏瓜的id
        String sql ="SELECT `event_id` AS eventId FROM `collection` WHERE `user_id` =?";
        try {
            list=queryRunner.query(sql, new ColumnListHandler<>(),userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}
