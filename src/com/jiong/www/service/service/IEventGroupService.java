package com.jiong.www.service.service;

import com.jiong.www.po.Event;
import com.jiong.www.po.EventGroup;

import javax.swing.*;
import java.util.List;

/**
 * @author Mono
 */
public interface IEventGroupService {
    /**
     * 创建瓜圈
     * @param userId 创建瓜圈的管理员id
     * @param eventGroupName 瓜圈名
     * @param eventGroupDescription 瓜圈描述
     * @return 判断成功
     */
    int doCreate(int userId, String eventGroupName, String eventGroupDescription);

    /**
     * 验证瓜圈名是否存在
     * @param eventGroupName 瓜圈名
     * @return 判断成功
     */
    int verifyExist(String eventGroupName);

    /**
     * 验证是否是该管理员管理的瓜圈
     * @param userId 管理员id
     * @param eventGroupName 瓜圈名
     * @return 判断是否是所管理的
     */
    int verifyOfAdmin(int userId, String eventGroupName);

    /**
     * 删除瓜圈，同时在管理员所管理的数据删除关系，删除瓜圈里的瓜
     * @param deleteEventGroupName 删除的瓜圈名
     * @param userId 瓜圈管理员id
     * @return 判断成功
     */
    int doDelete(String deleteEventGroupName, int userId);

    /**
     * 查看所有瓜圈
     * @return 所有瓜圈信息
     */
    List<EventGroup> findAll();

    /**
     * 用瓜圈名查该瓜圈信息
     * @param eventGroupName 瓜圈名
     * @return 瓜圈信息
     */
    EventGroup viewEventGroup(String eventGroupName);

    /**
     * 用瓜圈名查看瓜圈里的所有瓜
     * @param eventGroupName 瓜圈名
     * @return 所有的瓜圈信息
     */
    List<Event> viewEventOfEventGroup(String eventGroupName);
    /**瓜网的第一页的数据处理
     * @param pageSize 每一页的展示数目
     * @param listModel 传入数据源
     * @param eventGroups 获得瓜圈的信息
     */
    void doDataProcess(int pageSize,DefaultListModel<String> listModel,List<EventGroup> eventGroups);
    /**刷新数据源
     * @param eventGroups 瓜圈数据
     * @param defaultListModel 数据源
     * */
    void doRefresh(List<EventGroup> eventGroups,DefaultListModel<String> defaultListModel);
    /**瓜圈的第一页的数据处理
     * @param pageSize 每一页的展示数目
     * @param listModel 传入数据源
     * @param events 获得瓜的信息
     */
    void dataProcessGroup(int pageSize, DefaultListModel<String> listModel, List<Event> events);
    /**刷新数据源
     * @param events 瓜数据
     * @param defaultListModel 数据源
     * @param eventGroupName 瓜圈名
     * */
    void refreshGroup(List<Event> events, DefaultListModel<String> defaultListModel, String eventGroupName);
    /**查询瓜圈管理员id
     * @param eventGroupName 瓜圈名
     * @return 管理员id
     * */
    int queryAdmin(String eventGroupName);
}
