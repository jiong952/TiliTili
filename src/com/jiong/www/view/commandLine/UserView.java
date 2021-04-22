package com.jiong.www.view.commandLine;

import com.jiong.www.po.User;
import com.jiong.www.service.serviceImpl.UserServiceImpl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserView {
    Scanner scanner = new Scanner(System.in);
    //注册界面，将用户信息加到用户角色表,普通用户只能注册为吃瓜群众，用户名存在则不继续输入
    private UserServiceImpl userServiceImpl = new UserServiceImpl();
    public void register(){
        boolean flag=false;
        while (!flag){
            System.out.println("请输入新用户名：");
            String loginName = scanner.next();
            int judge = userServiceImpl.isExist(loginName);
            if(judge==0){
                //judge为0不存在,为1存在
                System.out.println("请输入新密码：");
                String loginPassword = scanner.next();
                int row = userServiceImpl.register(loginName,loginPassword,1);
                //row用于接收service传来的结果
                if(row>0){
                    System.out.println("注册成功！");
                    flag=true;
                }else {
                    System.out.println("注册失败!");
                }
            }
            else {
                System.out.println("用户名已存在！请重新输入用户名!退出注册请输入0");
                int nextRow=scanner.nextInt();
                if(nextRow==0){
                    break;
                }
            }
        }

    }
    //登录界面
    public int login(){
        System.out.println("请输入用户名：");
        String loginName = scanner.nextLine();
        System.out.println("请输入密码：");
        String loginPassword = scanner.nextLine();
        int userId;
        //用户的id
        userId= userServiceImpl.login(loginName, loginPassword,0);
        //row用于接收service传来的结果
        if(userId!=0){
            System.out.println("登录成功！");
        }else {
            System.out.println("登录失败！用户名不存在或密码错误！");
        }
        return userId;
        //返回userId，登录后将userId作为参数传进其他方法，作为身份验证
    }
    //验证身份
    public int verifyRole(int userId){
        int row;
        row= userServiceImpl.queryRole(userId);
        return row;
        //row为1是普通用户,2是管理员,3是游客,4是超管
    }
    //完善信息界面,注意：头像还没处理，生日和邮箱格式也没有判断,改为文本框 修改密码
    public void perfectInformation(int userId){
        String userEmail=null;
        String userNickName =null;
        int userGender =0;
        String userDescription = null;
        Date userBirthday=null;
        int judge =1;
        while(judge!=0){
            System.out.println("------------------------------------------------------------");
            System.out.println("1.填写生日 2.填写邮箱 3.设置头像 4.修改昵称 5.填写性别 6.填写个人简介 7.修改密码 8.保存修改 0.返回");
            System.out.println("------------------------------------------------------------");
            judge=scanner.nextInt();
            System.out.println("sad");
            scanner.nextLine();
            //一次修改一次录入
            switch (judge){
                //judge用于判断是否返回
                case 1:
                    System.out.println("请输入生日");
                    //bug
                    break;
                case 2:
                    System.out.println("请输入邮箱");
                    userEmail=scanner.nextLine();
                    break;
                case 3:
                    System.out.println("请放头像");
                    break;
                case 4:
                    System.out.println("请输入新昵称");
                    userNickName=scanner.nextLine();
                    break;
                case 5:
                    System.out.println("请选择性别:0为女，1为男");
                    //用JRadioButton单选框
                    userGender= scanner.nextInt();
                    break;
                case 6:
                    System.out.println("请输入个人简介！停止输入请在文末新建一行输入@");
                    List<String> list=new ArrayList<>();
                    while (!scanner.hasNext("@")){
                        list.add(scanner.nextLine());
                    }
                    //储存多行字符串的数组
                    StringBuilder stringBuilder = new StringBuilder();
                    for (String s : list) {
                        stringBuilder.append(s + "\n");
                    }
                    //把list--stringBuilder--string
                    userDescription = stringBuilder.toString();
                    scanner.nextLine();
                    //用于缓冲最后的@
                    //用文本框来修改
                    break;
                case 7:
                    System.out.println("请输入旧密码:");
                    String userOldPassword;
                    userOldPassword=scanner.nextLine();
                    int row1= userServiceImpl.queryPwd(userOldPassword,userId);
                    //用于判断密码修改是否正确
                    if(row1>0){
                        boolean flag=false;
                        String userNewPassword = null;
                        while (!flag) {
                            System.out.println("请输入新密码:");
                            userNewPassword = scanner.nextLine();
                            System.out.println("确认密码:");
                            String userConfirmPassword;
                            userConfirmPassword = scanner.nextLine();
                            if (userConfirmPassword.equals(userNewPassword)) {
                                flag=true;
                            }
                            else {
                                System.out.println("两次密码不一致！");
                                System.out.println("取消修改密码请输入0");
                                int choice=scanner.nextInt();
                                if(choice==0) {
                                    break;
                                }
                            }

                        }
                        //确认密码功能，防止错误输入
                        if(flag){
                            int row2;
                            row2= userServiceImpl.changePwd(userNewPassword,userId);
                            if(row2>0){
                                System.out.println("修改密码成功！");
                            }else {
                                System.out.println("修改失败");
                            }
                        }
                        else {
                            break;
                        }
                    }
                    else {
                        System.out.println("密码输入错误");
                    }
                    break;
                case 8:
                    int row = userServiceImpl.updateInformation(userEmail,userNickName,userGender,userDescription,userId, userBirthday);
                    if(row>0){
                        System.out.println("保存成功!");
                    }else {
                        System.out.println("保存失败！");
                    }
                    break;
                case 0:
                    System.out.println("成功返回");
                    break;
                default:
                    System.out.println("请输入【0-7】");
                    break;
            }

        }
    }
    //查询用户的个人信息
    public void queryUserInformation(int userId){
        User userQuery = new User();
        userQuery= userServiceImpl.queryInformation(userId);
        System.out.println("用户名：" + userQuery.getLoginName());
        System.out.println("邮箱：" + userQuery.getUserEmail());
        System.out.println("昵称：" + userQuery.getUserNickname());
        System.out.println("性别：" + (userQuery.getUserGender()== 0 ? "女" : "男"));
        System.out.println("个人简介：" + userQuery.getUserDescription());
    }
}
