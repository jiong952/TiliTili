package com.jiong.www.dao.daoImpl;

import com.jiong.www.dao.DaoException;
import com.jiong.www.dao.dao.IAccuseDao;
import com.jiong.www.po.Accuse;
import com.jiong.www.po.Event;

import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import static com.jiong.www.util.MyDsUtils.*;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mono
 */
public class AccuseDaoImpl implements IAccuseDao {

    /**用户举报瓜*/
    @Override
    public int accuse(Accuse accuse)  {
        int row;
        Object[] params={accuse.getEventId(),accuse.getAccusedUserId(),accuse.getAccusedContent()};
        String sql ="INSERT INTO `accusation` (`accused_event_id`,`accuse_user_id`,`accused_content`) VALUES(?,?,?)";
        try {
            row=queryRunner.execute(sql,params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("用户举报瓜异常",e);
        }
        return row;
    }
    /**管理员查看自己管理瓜圈的举报情况*/
    @Override
    public List<Accuse> findAll(List<Event> eventList){
        List<Accuse> accuses = new ArrayList<>();
        for(Event event:eventList){
            String sql="SELECT `accused_content` AS accusedContent,`create_time` AS accuseTime,`accuse_user_id` AS accusedUserId," +
                    "`accused_event_id` AS eventId FROM `accusation`  WHERE `accused_event_id`= ?";
            try {
                List<Accuse> query = queryRunner.query(sql, new BeanListHandler<>(Accuse.class), event.getEventId());
                if(query!=null){
                    accuses.addAll(query);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DaoException("管理员查看自己管理瓜圈的举报情况异常",e);
            }
        }
        //把查询的结果集返回到service层
        return accuses;
    }
    /**清空所有举报*/
    @Override
    public void clearAll(Connection conn, int eventId) {
        String sql="DELETE FROM `accusation` WHERE `accused_event_id`= ? ";
        //清空所有举报
        try {
            queryRunner.execute(conn,sql,eventId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("清空所有举报异常",e);
        }
    }
    /**删除举报*/
    @Override
    public void delete(Accuse accuse)  {
        Object[] params={accuse.getEventId(),accuse.getAccusedContent()};
        String sql ="DELETE FROM `accusation` WHERE `accused_event_id`= ? AND`accused_content`=? ";
        try {
            queryRunner.execute(sql,params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("删除举报异常",e);
        }
    }
    /**查看用户是否已经举报*/
    @Override
    public int isAccuse(Accuse accuse) {
        int judge=0;
        Object[] params={accuse.getEventId(),accuse.getAccusedUserId()};
        String sql="SELECT `id` FROM `accusation` WHERE `accused_event_id`= ? AND `accuse_user_id` = ?";
        try {
            Object query = queryRunner.query(sql, new ScalarHandler<>(), params);
            if(query!=null){
                judge=1;
                //已经举报过
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("查看用户举报情况异常",e);
        }
        return judge;
    }


}
