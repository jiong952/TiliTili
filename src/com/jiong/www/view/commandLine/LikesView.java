package com.jiong.www.view.commandLine;

import com.jiong.www.po.Event;
import com.jiong.www.service.LikesService;

import java.util.List;
import java.util.Scanner;

public class LikesView {
    Scanner scanner = new Scanner(System.in);
    LikesService likesService = new LikesService();
    //点赞,同时更新用户点赞表
    public void likes(int userId,int eventId){
        likesService.likes(userId, eventId);
    }
    //取消点赞,同时删除用户点赞表中的相关数据
    public void cancelLikes(int userId,int eventId){
        likesService.cancelLikes(userId,eventId);
    }
    //查看用户是否点赞
    public void likesIfOrNot(int userId,int eventId){
        int judge = likesService.likesIfOrNot(userId, eventId);
        if(judge==1){
            System.out.println("点赞");
        }else {
            System.out.println("未点赞");
        }
    }
    //查看点赞合集 每个瓜只展示事件标题 作者 发布时间 点赞量 收藏量 评论量
    public void viewEventOfLikes(int userId){
        List<Event> events = likesService.viewEventOfLikes(userId);
        System.out.println("我的点赞合集：");
        for (int i = 0; i < events.size(); i++) {
            Event eventQuery ;
            eventQuery=events.get(i);
            System.out.println(eventQuery.getEventName());
            System.out.println("作者："+eventQuery.getPublisherName()+"\t发布时间："+eventQuery.getCreateTime());
            System.out.println("点赞量："+eventQuery.getLikesNum()+"\t评论量"+eventQuery.getCommentNum()+"\t收藏量"+eventQuery.getCollectionNum());
            System.out.println("--------------------------------------------------------------------------");
        }
    }
}
