package com.jiong.www.view.commandLine;

import com.jiong.www.po.Event;
import com.jiong.www.service.EventGroupService;
import com.jiong.www.service.EventServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EventView {

    Scanner scanner = new Scanner(System.in);
    EventServiceImpl eventServiceImpl = new EventServiceImpl();
    EventGroupService eventGroupService = new EventGroupService();
    public void createEvent(int userId,int eventGroupId){
        boolean flag=false;
        while (!flag){
            System.out.println("请输入新创建的瓜的名称");
            String eventName=scanner.nextLine();
            //验证瓜名
            int judge= eventServiceImpl.verifyExist(eventName);
            if(judge==0)
            //judge==0表示瓜圈名不存在
            {
                System.out.println("请输入瓜的内容！停止输入请在文末新建一行输入@");
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
                String eventContent = stringBuilder.toString();
                int row=0;
                //创建瓜
                row= eventServiceImpl.doCreate(userId,eventGroupId,eventName,eventContent);
                if(row>0){
                    System.out.println("创建成功！");
                    flag=true;
                }else {
                    System.out.println("创建失败！");
                }
            }
            else {
                System.out.println("此瓜已存在!请重新输入瓜名！退出创建请输入0");
                int nextRow=scanner.nextInt();
                if(nextRow==0){
                    break;
                }
            }
        }
    }
    //删除瓜 管理员删除自己瓜圈里的瓜，用户删除自己瓜
    public void deleteEvent(int userId,int roleId,int eventId){
        //删除要在查看的基础上，即在查看的界面(瓜一定存在，不需要验证)才能删除,此时默认知道瓜名，用户的身份
        if(roleId==1){
            //为普通用户
            //验证这个瓜是不是该用户发的
            int row = eventServiceImpl.doVerify(userId,eventId);
            if(row==1){
                //row1==1表示是该用户发的
                //进行删除
                int judge= eventServiceImpl.doDelete(eventId);
                if(judge==1){
                    System.out.println("删除瓜成功！");
                }else {
                    System.out.println("删除瓜失败！");
                }
            }else {
                //不是用户发的，没有删除的权限
                System.out.println("这不是您发布的瓜！没有权限");
            }
        }
        else if(roleId==2){
            //管理员
            //先查这个瓜在哪个组,查出瓜圈名
            String eventGroupName ;
            //eventGroupName为查询的瓜圈名
            eventGroupName= eventServiceImpl.queryGroupName(eventId);
            //验证这个组是不是归管理员管
            int row = eventGroupService.verifyEventGroupOfAdmin(userId,eventGroupName);
            //row==1表示是该管理员管理的,0表示不是管理员管
            if(row==1){
                //进行删除
                int judge= eventServiceImpl.doDelete(eventId);
                if(judge==1){
                    System.out.println("删除瓜成功！");
                }else {
                    System.out.println("删除瓜失败！");
                }
            }else {
                //不是管理员所管理的瓜圈,没有权限
                System.out.println("这不是您管理的瓜圈！没有权限");
            }

        }
    }
    //查看瓜,返回瓜的所有信息，封装
    public int viewEvent(String eventName){
        Event eventQuery;
        eventQuery= eventServiceImpl.doView(eventName);
        System.out.println(eventQuery.getEventName());
        System.out.println("发布者:"+eventQuery.getPublisherName());
        System.out.println("发布时间:"+eventQuery.getCreateTime());
        System.out.println("点赞数："+eventQuery.getLikesNum()+"\t评论数："+eventQuery.getCommentNum()+"\t收藏数："+eventQuery.getCollectionNum());
        System.out.println();
        System.out.println(eventQuery.getEventContent());
        System.out.println("");
        int eventId;
        eventId=eventQuery.getEventId();
        return eventId;
    }



}
