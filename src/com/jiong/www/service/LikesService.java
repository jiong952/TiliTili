package com.jiong.www.service;

import com.jiong.www.dao.LikesDao;
import com.jiong.www.po.Event;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LikesService {
    LikesDao likesDao = new LikesDao();
    //点赞,同时更新用户点赞表
    public void likes(int userId,int eventId){
        try {
            likesDao.likes(userId,eventId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //取消点赞,同时删除用户点赞表中的相关数据
    public void cancelLikes(int userId,int eventId){
        try {
            likesDao.cancelLikes(userId,eventId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //查看用户是否点赞
    public int likesIfOrNot(int userId,int eventId)  {
        int judge=0;
        try {
            judge=likesDao.likesIfOrNot(userId,eventId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  judge;
    }
    //查看点赞合集 每个瓜只展示事件标题 作者 发布时间 点赞量 收藏量 评论量
    public List<Event> viewEventOfLikes(int userId){
        List<Event> events = new ArrayList<Event>();
        try {
            events = likesDao.viewEventOfLikes(userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

}
