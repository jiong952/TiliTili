package com.jiong.www.view.commandLine;

import com.jiong.www.po.Event;
import com.jiong.www.service.CollectionService;

import java.util.List;
import java.util.Scanner;

public class CollectionView {
    Scanner scanner = new Scanner(System.in);
    CollectionService collectionService = new CollectionService();
    //收藏,同时更新收藏表
    public void collection(int userId,int eventId){
        collectionService.collection(userId,eventId);
    }
    //取消收藏,同时删除用户收藏表中的相关数据
    public void cancelCollection(int userId,int eventId){
        collectionService.cancelCollection(userId,eventId);
    }
    //查看用户是否点赞
    public void collectionIfOrNot(int userId,int eventId){
        int judge = collectionService.collectionIfOrNot(userId, eventId);
        if(judge==1){
            System.out.println("收藏");
        }else {
            System.out.println("未收藏");
        }
    }
    //查看收藏合集 每个瓜只展示事件标题 作者 发布时间 点赞量 收藏量 评论量
    public void viewEventOfCollection(int userId){
        List<Event> events = collectionService.viewEventOfCollection(userId);
        System.out.println("我的收藏合集：");
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