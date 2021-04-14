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
public class AccuseDao {
    /**用户举报瓜*/
    public int accuseEvent(Accuse accuse) throws SQLException {
        int row=0;
        Connection conn = JdbcUtils.getConnection();
        String sql ="INSERT INTO `accusation` (`accused_event_id`,`accuse_user_id`,`accused_content`) VALUES(?,?,?)";
        //联表查询
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1,accuse.getEventId());
        ps.setInt(2,accuse.getAccusedUserId());
        ps.setString(3,accuse.getAccusedContent());
        row = ps.executeUpdate();
        JdbcUtils.release(conn,ps,null);
        return row;
    }
    /**管理员查看自己管理瓜圈的举报情况*/
    public List<Accuse> viewAccusation(int userId) throws SQLException {
        List<Accuse> accuses = new ArrayList<Accuse>();
        Connection conn = JdbcUtils.getConnection();
        String sql="SELECT `login_name`,`accused_content`,s.`create_time`,`event_name`\n" +
                "FROM `accusation` s\n" +
                "INNER JOIN `user`\n" +
                "ON `accuse_user_id` = `user_id`\n" +
                "INNER JOIN `event`\n" +
                "ON `accused_event_id` = `event_id`\n" +
                "INNER JOIN `administrator`\n" +
                "ON `eventGroup_id` = `administrator_groupid`\n" +
                "WHERE `administrator_id` = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
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
        JdbcUtils.release(conn,ps,null);
        //把查询的结果集返回到service层
        return accuses;
    }
    /**删除举报*/
    public void deleteAccuse(Accuse accuse) throws SQLException {
        Connection conn = JdbcUtils.getConnection();
        String sql ="DELETE FROM `accusation` WHERE `accused_event_id`= ? AND`accused_content`=? ";
        //联表查询
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1,accuse.getEventId());
        ps.setString(2,accuse.getAccusedContent());
        ps.executeUpdate();
        JdbcUtils.release(conn,ps,null);
    }
}
