package com.jiong.www.dao.daoImpl;

import com.jiong.www.dao.Idao.IAccuseDao;
import com.jiong.www.po.Accuse;
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
public class AccuseDaoImpl implements IAccuseDao {

    /**用户举报瓜*/
    @Override
    public int doAccuse(Accuse accuse)  {
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
    public List<Accuse> findAll(List<Event> eventList){
        List<Accuse> accuses = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JdbcUtils.getConnection();
            for(Event event : eventList){
                String sql="SELECT `accused_content`,`create_time` ,`accuse_user_id`\n" +
                        "FROM `accusation` WHERE `accused_event_id`= ?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1,event.getEventId());
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    Accuse accuse = new Accuse();
                    accuse.setAccusedContent(rs.getString("accused_content"));
                    accuse.setAccuseTime(rs.getTimestamp("create_time"));
                    accuse.setAccusedEventName(event.getEventName());
                    accuse.setAccusedUserId(rs.getInt("accuse_user_id"));
                    accuses.add(accuse);
                }
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

    @Override
    public void doClear(int eventId) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JdbcUtils.getConnection();
            String sql="DELETE FROM `accusation` WHERE `accused_event_id`= ? ";
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

    /**删除举报*/
    @Override
    public void doDelete(Accuse accuse)  {
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
