package com.jiong.www.view.commandLine;

import com.jiong.www.po.Comment;
import com.jiong.www.service.CommentServiceImpl;
import com.jiong.www.service.EventGroupService;
import com.jiong.www.service.EventService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CommentView {

    Scanner scanner = new Scanner(System.in);
    CommentServiceImpl commentServiceImpl = new CommentServiceImpl();
    EventService eventService = new EventService();
    EventGroupService eventGroupService = new EventGroupService();
    //进行评论，评论数+1，评论表更新
    public void comment(int userId,int eventId){
        System.out.println("请输入评论内容！停止输入请在文末新建一行输入@");
        List<String> list=new ArrayList<>();
        while (!scanner.hasNext("@")){
            list.add(scanner.nextLine());
        }
        //储存多行字符串的数组
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            stringBuilder.append(list.get(i)+"\n");
        }
        //把list--stringBuilder--string
        String commentContent = stringBuilder.toString();
        scanner.nextLine();
        //用于缓冲最后的@
        commentServiceImpl.doComment(userId,eventId,commentContent);

    }
    //删除评论，同时删除用户评论表中的相关数据,用于普通用户的删除
    public void cancelComment(int commentId,int eventId){
        commentServiceImpl.doCancel(commentId,eventId);
    }
    //删除瓜的所有评论,管理员,事件发布者
    public void clearComment(int userId,int roleId,int eventId){
        if(roleId==1){
            //为普通用户
            //验证这个瓜是不是该用户发的
            int row = eventService.verifyEventOfUser(userId,eventId);
            if(row==1){
                //row1==1表示是该用户发的
                //进行删除
                commentServiceImpl.doClear(eventId);
            }else {
                //不是用户发的，没有删除的权限
                System.out.println("这不是您发布的瓜！没有权限");
            }
        }
        else if(roleId==2) {
            //先查这个瓜在哪个组,查出瓜圈名
            String eventGroupName;
            //eventGroupName为查询的瓜圈名
            eventGroupName = eventService.queryEventOfEventGroup(eventId);
            //验证这个组是不是归管理员管
            int row = eventGroupService.verifyEventGroupOfAdmin(userId, eventGroupName);
            //row==1表示是该管理员管理的,0表示不是管理员管
            if (row == 1) {
                //进行删除
                commentServiceImpl.doClear(eventId);
            } else {
                //不是管理员所管理的瓜圈,没有权限
                System.out.println("这不是您管理的瓜圈！没有权限");
            }
        }
    }
    //查看瓜的评论,也要返回评论人名
    public void viewComment(int eventId){
        List<Comment> comments = commentServiceImpl.findAll(eventId);
        System.out.println("评论：");
        for (int i = 0; i < comments.size(); i++) {
            Comment comment ;
            comment = comments.get(i);
            System.out.println(comment.getCommenterName());
            System.out.println(comment.getCommentContent());
            System.out.println(comment.getCommenterId());
        }
    }
}
