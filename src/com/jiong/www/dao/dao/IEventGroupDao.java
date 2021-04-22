package com.jiong.www.dao.dao;

import com.jiong.www.po.Event;
import com.jiong.www.po.EventGroup;

import java.sql.Connection;
import java.util.List;

/**
 * @author Mono
 */
public interface IEventGroupDao {
    /**
     * 创建瓜圈,添加瓜圈信息到瓜圈表
     * @param eventGroup 瓜圈信息
     * @param conn 连接
     * @return 判断成功
     */
    int doCreate(Connection conn,EventGroup eventGroup);

    /**
     * 把瓜圈和管理员联系起来
     * @param userId 管理员id
     * @param eventGroup 瓜圈信息
     * @param conn 连接
     */
    void groupOfAdmin(Connection conn,int userId, EventGroup eventGroup);

    /**
     * 验证瓜圈名是否存在,避免发生重复
     * @param eventGroupName 瓜名
     * @return 判断是否存在
     */
    int verifyExist(String eventGroupName);

    /**
     * 验证是否是该管理员管理的瓜圈
     * @param userId 管理员id
     * @param eventGroupId 瓜圈id
     * @return 判断成功
     */
    int verifyOfAdmin(int userId, int eventGroupId);

    /**
     * 删除瓜圈
     * @param deleteEventGroupName 删除瓜圈名
     * @param conn 连接
     * @return 判断成功
     */
    int doDelete(Connection conn,String deleteEventGroupName);

    /**
     * 删除在管理员表与瓜圈的数据
     * @param eventGroupId 瓜圈id
     * @param userId 管理员id
     * @param conn 连接
     */
    void doDeleteOfAdmin(Connection conn,int eventGroupId, int userId);

    /**
     * 用瓜圈名查该瓜圈信息
     * @param eventGroupName 瓜圈名
     * @return 瓜圈信息
     */
    EventGroup viewEventGroup(String eventGroupName);

    /**
     * 用瓜圈id查瓜圈名
     * @param eventGroupId 瓜圈id
     * @return 瓜圈名
     */
    String viewEventGroup(int eventGroupId);

    /**
     * 查看管理员管理的所有瓜圈的瓜圈id
     * @param userId 管理员id
     * @return 管理的所有瓜圈的id
     */
    List<Integer> viewAdminGroup(int userId);

    /**
     * 用一组瓜圈id查该瓜圈里的所有瓜的瓜信息
     * @param list 一组瓜圈id
     * @return 这组瓜圈id对应的瓜圈里瓜信息
     */
    List<Event> viewEventGroup(List<Integer> list);

    /**
     * 查看系统所有瓜圈
     * @return 所有瓜圈的信息
     */
    List<EventGroup> viewAllEventGroup();

    /**
     * 用瓜圈id查看瓜圈里的所有瓜信息
     * @param eventGroupId 瓜圈id
     * @return 该瓜圈id对应的瓜圈
     */
    List<Event> viewEventOfEventGroup(int eventGroupId);
    /**查看瓜圈所属管理员id
     * @param eventGroupId 瓜圈id
     * @return 管理员id
     * */
    int queryAdmin(int eventGroupId);
}
