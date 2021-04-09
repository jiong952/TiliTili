package com.jiong.www.view.commandLine;

import com.jiong.www.po.Event;
import com.jiong.www.po.EventGroup;
import com.jiong.www.service.EventGroupService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EventGroupView {
    Scanner scanner = new Scanner(System.in);
    private EventGroupService eventGroupService = new EventGroupService();
    //创建瓜圈,在瓜圈表里加数据，同时管理员的管理表里也要加瓜圈的数据在管理员瓜圈表里也要传数据，把管理员id加传入,瓜圈名不能相同,
    public void createEventGroup(int userId){
        boolean flag=false;
        while (!flag){
            System.out.println("请输入新创建的瓜圈名称");
            String eventGroupName=scanner.nextLine();
            //验证瓜圈名
            int judge;
            judge = eventGroupService.verifyEventGroupName(eventGroupName);
            if(judge==0)
            //judge==0表示瓜圈名不存在
            {
                System.out.println("请输入瓜圈简介！停止输入请在文末新建一行输入@");
                List<String> list=new ArrayList<>();
                //储存多行字符串的数组
                while (!scanner.hasNext("@")){
                    list.add(scanner.nextLine());
                }
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < list.size(); i++) {
                    stringBuilder.append(list.get(i)+"\n");
                }
                //把list--stringBuilder--string
                String eventGroupDescription = stringBuilder.toString();
                int row=eventGroupService.createEventGroup(userId,eventGroupName,eventGroupDescription);
                //创建瓜圈
                if(row>0){
                    System.out.println("创建成功！");
                    flag=true;
                }else {
                    System.out.println("创建失败！");
                }
            }
            else {
                System.out.println("瓜圈已存在!请重新输入瓜圈名！退出创建请输入0");
                int nextRow=scanner.nextInt();
                if(nextRow==0){
                    break;
                }
            }
        }
    }
    //删除瓜圈，同时在管理员所管理的数据删除关系,管理员只能删除自己的瓜圈,删除瓜圈，瓜圈里的瓜也要被删除
    public void deleteEventGroup(int userId){
        boolean flag=false;
        while (!flag){
            System.out.println("请输入删除的瓜圈名");
            String deleteEventGroupName = scanner.next();
            //验证瓜圈名
            int judge;
            int judge1;
            judge = eventGroupService.verifyEventGroupName(deleteEventGroupName);
            if(judge==1){
                judge1=eventGroupService.verifyEventGroupOfAdmin(userId,deleteEventGroupName);
                if(judge1==1){
                    //judge1==1表示是该管理员管理的瓜圈，可以删除
                    //删除
                    int row=eventGroupService.deleteEventGroup(deleteEventGroupName,userId);
                    //row用于接收service传来的结果
                    if(row>0){
                        System.out.println("删除成功！");
                        flag=true;
                    }else {
                        System.out.println("删除失败!");
                    }
                }else {
                    System.out.println("这不是您管理的瓜圈，不可以删除！请重新输入瓜圈名！退出删除请输入0");
                    int nextRow=scanner.nextInt();
                    if(nextRow==0){
                        break;
                    }
                }

            }else {
                System.out.println("该瓜圈名不存在！请重新输入瓜圈名！退出删除请输入0");
                int nextRow=scanner.nextInt();
                if(nextRow==0){
                    break;
                }
            }
        }
    }
    //查看瓜圈,查看同时显示瓜圈里的所有瓜
    public void viewEventOfEventGroup(String eventGroupName){
        List<Event> events ;
        events = eventGroupService.viewEventOfEventGroup(eventGroupName);
        System.out.println("所有瓜");
        for (int i = 0; i < events.size(); i++) {
            Event event;
            event=events.get(i);
            System.out.println(event.getEventName());
            System.out.println("作者名:"+event.getPublisherName()+"创建时间:"+event.getCreateTime());
            System.out.println("点赞量:"+event.getLikesNum()+"\t收藏量:"+event.getCollectionNum()+"\t评论量:"+event.getCommentNum());
            System.out.println("--------------------------------------------------------------------------");
        }
    }
    //查看瓜圈的简介
    public void viewEventGroupDescription(String eventGroupName) {
        System.out.println(eventGroupService.viewEventGroupDescription(eventGroupName));
    }
    // 要把瓜的相应评论输出
    public void viewEventGroup(){
        List<EventGroup> eventGroups ;
        eventGroups = eventGroupService.viewEventGroup();
        System.out.println("所有瓜圈：");
        for (int i = 0; i < eventGroups.size(); i++) {
            EventGroup eventGroup;
            eventGroup=eventGroups.get(i);
            System.out.println(eventGroup.getEventGroupName());
            System.out.println(eventGroup.getEventGroupDescription());
            System.out.println("--------------------------------------------------------------------------");
        }

    }




}
