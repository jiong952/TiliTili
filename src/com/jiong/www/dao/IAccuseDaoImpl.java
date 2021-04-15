package com.jiong.www.dao;

import com.jiong.www.po.Accuse;
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
public class IAccuseDaoImpl implements IAccuseDao {

    /**用户举报瓜*/
    @Override
    public int accuseEvent(Accuse accuse)  {
        int row = 0;
        Connection conn = null;
        PreparedStatement ps =null;
        try {
            conn = JdbcUtils.getConnection();
            String sql ="INSERT INTO `accusation` (`accused_event_id`,`accuse_user_id`,`accused_content`) VALUES(?,?,?)";
            //联表查询
            ps = conn.prepareStatement(sql);
            ps.setInt(1,accuse.getEventId());
            ps.setInt(2,accuse.getAccusedUserId());
            ps.setString(3,accuse.getAccusedContent());
            row = ps.executeUpdate();

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
    /**管理员查看自己管理瓜圈的举报情况*/
    @Override
    public List<Accuse> viewAccusation(int userId){
        List<Accuse> accuses = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JdbcUtils.getConnection();
            String sql="SELECT `login_name`,`accused_content`,s.`create_time`,`event_name`\n" +
                    "FROM `accusation` s\n" +
                    "INNER JOIN `user`\n" +
                    "ON `accuse_user_id` = `user_id`\n" +
                    "INNER JOIN `event`\n" +
                    "ON `accused_event_id` = `event_id`\n" +
                    "INNER JOIN `administrator`\n" +
                    "ON `eventGroup_id` = `administrator_groupid`\n" +
                    "WHERE `administrator_id` = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Accuse accuse = new Accuse();
                accuse.setAccusedUserName(rs.getString("login_name"));
                accuse.setAccusedContent(rs.getString("accused_content"));
                accuse.setAccuseTime(rs.getDate("create_time"));
                accuse.setAccusedEventName(rs.getString("event_name"));
                accuses.add(accuse);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                JdbcUtils.release(conn,ps,null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //把查询的结果集返回到service层
        return accuses;
    }
    /**删除举报*/
    @Override
    public void deleteAccuse(Accuse accuse)  {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JdbcUtils.getConnection();
            String sql ="DELETE FROM `accusation` WHERE `accused_event_id`= ? AND`accused_content`=? ";
            //联表查询
            ps = conn.prepareStatement(sql);
            ps.setInt(1,accuse.getEventId());
            ps.setString(2,accuse.getAccusedContent());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                JdbcUtils.release(conn,ps,null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
