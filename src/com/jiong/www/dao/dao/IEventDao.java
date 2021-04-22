package com.jiong.www.dao.dao;

import com.jiong.www.po.Event;

import java.sql.Connection;
import java.util.List;

/**
 * @author Mono
 */
public interface IEventDao {
    /**创建瓜，添加瓜信息到瓜表
     * @param userId 创键用户的id
     * @param  eventGroupId 瓜所在瓜圈id
     * @param  event 创键瓜的信息
     * @return 判断是否成功*/
    int create(int userId, int eventGroupId, Event event);
    /**验证瓜名是否存在
     * @param eventName 验证的瓜名
     * @return 判断是否操作成功
     * */
    int isExist(String eventName);
    /**删除瓜时验证是不是用户发的瓜
     * @param userId 用户id
     * @param eventId 瓜id
     * @return 判断是否操作成功
     * */
    int isCreate(int userId, int eventId);
    /**查询这个瓜所在的瓜圈id
     * @param eventId 瓜id
     * @return 瓜圈名
     * */
    int queryId(int eventId);
    /**删除瓜
     * @param eventId 删除的瓜id
     * @param conn 连接
     * @return 判断是否删除成功
     * */
    int delete(Connection conn, int eventId);
    /**查看一个瓜
     * @param eventName 瓜名
     * @return 瓜信息
     * */
    Event find(String eventName);
    /**查看所有瓜
     * @param list 瓜id集合
     * @return 瓜信息
     * */
    List<Event> findSome(List<Integer> list);
    /**查看瓜名
     * @param eventId 瓜id
     * @return 瓜名
     * */
    String queryName(int eventId);
}
