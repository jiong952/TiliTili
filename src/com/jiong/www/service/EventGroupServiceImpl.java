package com.jiong.www.service;

import com.jiong.www.dao.daoImpl.EventGroupDaoImpl;
import com.jiong.www.dao.dao.IEventGroupDao;
import com.jiong.www.dao.UserDao;
import com.jiong.www.po.Event;
import com.jiong.www.po.EventGroup;
import com.jiong.www.service.service.IEventService;
import com.jiong.www.service.serviceImpl.EventServiceImpl;

import javax.swing.*;
import java.util.List;
/**
 * @author Mono
 */
public class EventGroupServiceImpl implements IEventGroupService {

    IEventGroupDao iEventGroupDao = new EventGroupDaoImpl();
    IEventService iEventService = new EventServiceImpl();
    /**创建瓜圈*/
    @Override
    public int doCreate(int userId, String eventGroupName, String eventGroupDescription){
        int row;
        // 用于接收dao层的返回值
        //封装eventGroup对象
        EventGroup eventGroup = new EventGroup();
        eventGroup.setEventGroupName(eventGroupName);
        eventGroup.setEventGroupDescription(eventGroupDescription);
        row= iEventGroupDao.doCreate(eventGroup);
        iEventGroupDao.groupOfAdmin(userId,eventGroup);
        return row;
    }
    /**验证瓜圈名是否存在*/
    @Override
    public int verifyExist(String eventGroupName){
        int row;
        //默认0不存在
        row= iEventGroupDao.verifyExist(eventGroupName);
        //0则无数据，1则有数据
        return row;

    }
    /**验证是否是该管理员管理的瓜圈*/
    @Override
    public int verifyOfAdmin(int userId, String eventGroupName){
        int row;
        //默认0不是管理员管理的瓜圈
            EventGroup eventGroup = iEventGroupDao.viewEventGroup(eventGroupName);
            row= iEventGroupDao.verifyOfAdmin(userId,eventGroup.getEventGroupId());
        return row;
    }
    /**删除瓜圈，同时在管理员所管理的数据删除关系，删除瓜圈里的瓜*/
    @Override
    public int doDelete(String deleteEventGroupName, int userId){
        int row;
        //删除瓜圈里的瓜
        List<Event> events = iEventGroupDao.viewEventOfEventGroup(iEventGroupDao.viewEventGroup(deleteEventGroupName).getEventGroupId());
        for(Event event:events){
            iEventService.doDelete(event.getEventId());
        }
        //清除管理员表瓜圈的数据
        iEventGroupDao.doDeleteOfAdmin(iEventGroupDao.viewEventGroup(deleteEventGroupName).getEventGroupId(),userId);
        //删除瓜圈
        row= iEventGroupDao.doDelete(deleteEventGroupName);
        return row;
    }
    /**查看所有瓜圈*/
    @Override
    public List<EventGroup> findAll(){
        List<EventGroup> eventGroups;
        eventGroups= iEventGroupDao.viewAllEventGroup();
        return eventGroups;
    }
    /**用瓜圈名查该瓜圈信息*/
    @Override
    public EventGroup viewEventGroup(String eventGroupName){
        EventGroup eventGroup;
        eventGroup= iEventGroupDao.viewEventGroup(eventGroupName);
        return eventGroup;
    }
    /**用瓜圈名查看瓜圈里的所有瓜*/
    @Override
    public List<Event> viewEventOfEventGroup(String eventGroupName){
        List<Event> events ;
        events = iEventGroupDao.viewEventOfEventGroup(iEventGroupDao.viewEventGroup(eventGroupName).getEventGroupId());
        for(Event event:events){
            event.setPublisherName(new UserDao().queryUserInformation(event.getPublisherId()).getLoginName());
        }
        return events;
    }
    /**瓜网的第一页的数据处理*/
    @Override
    public void  doDataProcess(int pageSize, DefaultListModel<String> listModel, List<EventGroup> eventGroups) {
        //每一页页面的展示瓜圈数目
        if(eventGroups.size()>= pageSize){
            for (int i = 0; i < pageSize; i++) {
                listModel.add(i,eventGroups.get(i).getEventGroupName());
            }
        }else {
            for (int i = 0; i < eventGroups.size(); i++) {
                listModel.add(i,eventGroups.get(i).getEventGroupName());
            }
        }
    }
    /**删除增加之后刷新数据*/
    @Override
    public void   doRefresh(List<EventGroup> eventGroups,DefaultListModel<String> defaultListModel) {
        //刷新
        List<EventGroup> eventGroups1 = findAll();
        defaultListModel.clear();
        for (int i = 0; i < eventGroups1.size(); i++) {
            defaultListModel.add(i,eventGroups1.get(i).getEventGroupName());
        }
        //向列表框中加入所有的瓜圈名
        eventGroups.clear();
        eventGroups.addAll(eventGroups1);

    }

    /**
     * 瓜圈的第一页的数据处理
     */
    @Override
    public void DataProcessGroup(int pageSize, DefaultListModel<String> listModel, List<Event> events) {
        //每一页页面的展示瓜数目
        if(events.size()>=pageSize){
            for (int i = 0; i < pageSize; i++) {
                listModel.add(i,events.get(i).getEventName());
            }
        }else {
            for (int i = 0; i < events.size(); i++) {
                listModel.add(i,events.get(i).getEventName());
            }
        }
    }

    /**
     * 刷新数据源
     */
    @Override
    public void RefreshGroup(List<Event> events, DefaultListModel<String> defaultListModel,String eventGroupName) {
        //刷新
        List<Event> events1 = viewEventOfEventGroup(eventGroupName);
        defaultListModel.clear();
        for (int i = 0; i < events1.size(); i++) {
            defaultListModel.add(i,events1.get(i).getEventName());
        }
        //向列表框中加入所有的瓜圈名
        events.clear();
        events.addAll(events1);
    }
}
