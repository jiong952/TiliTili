package com.jiong.www.dao.dao;

import com.jiong.www.po.Accuse;
import com.jiong.www.po.Event;

import java.sql.Connection;
import java.util.List;


/**
 * @author Mono
 */
public interface IAccuseDao {
    /**用户举报瓜
     * @param accuse accuse对象传入举报信息
     * @return row 返回值用于判断举报是否成功
     * */
    int accuse(Accuse accuse);
    /**管理员查看自己管理瓜圈的举报情况
     * @param  eventList 管理员管理的瓜举报情况
     * @return 返回管理员的管理瓜圈的举报信息list
     * */
    List<Accuse> findAll(List<Event> eventList) ;
    /**清空瓜的举报
     * @param eventId 瓜id
     * @param conn 连接
     * */
    void clearAll(Connection conn, int eventId);
    /**删除举报
     * @param accuse 传入要删除举报信息的accuse对象
     * */
    void delete(Accuse accuse) ;
    /**查看用户是否已经举报
     * @param accuse 举报信息
     * @return 判断存在
     * */
    int isAccuse(Accuse accuse);
}
