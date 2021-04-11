package com.jiong.www.util;

import com.jiong.www.service.UserService;
import com.jiong.www.view.swing.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuSwingUtil {
    int userId;
    String eventGroupName;
    JFrame jFrame;
    public MenuSwingUtil(int userId, JFrame jFrame, String eventGroupName) {
        this.userId = userId;
        this.eventGroupName=eventGroupName;
        this.jFrame=jFrame;
        //菜单栏
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBorderPainted(true);
        //菜单
        JMenu status = new JMenu("用户状态");
        JMenuItem exitLogin = new JMenuItem("退出登录");
        exitLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(userId==0){
                    JOptionPane.showMessageDialog(null,"请先登录","错误",JOptionPane.ERROR_MESSAGE);
                }else {
                    jFrame.dispose();
                    new WelcomeSwing();
                    //表示游客模式
                }
            }
        });
        JMenuItem register = new JMenuItem("注册");
        register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(userId!=0){
                    JOptionPane.showMessageDialog(null,"请先退出登录","错误",JOptionPane.ERROR_MESSAGE);
                }else {
                    new RegisterSwing();
                }
            }
        });
        JMenuItem login = new JMenuItem("登录");
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(userId==0){
                    new LoginSwing();
                }else {
                    JOptionPane.showMessageDialog(null,"您已登录！","错误",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        status.add(exitLogin);
        status.add(register);
        status.add(login);

        JMenu information = new JMenu("个人信息");
        JMenuItem modifyInformation = new JMenuItem("修改信息");
        modifyInformation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new InformationSwing(userId,eventGroupName);
            }
        });
        JMenuItem modifyPassword = new JMenuItem("修改密码");
        modifyPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new PasswordSwing(userId,eventGroupName);
            }
        });
        information.add(modifyInformation);
        information.add(modifyPassword);

        JMenu event = new JMenu("瓜");
        JMenuItem queryEventGroup = new JMenuItem("查询瓜圈");
        queryEventGroup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new QueryGroupSwing(userId,eventGroupName);
            }
        });
        JMenuItem queryEvent = new JMenuItem("查询瓜");
        queryEvent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new QueryEventSwing(userId,eventGroupName);
            }
        });
        JMenuItem createEventGroup = new JMenuItem("创建瓜圈");
        createEventGroup.setVisible(false);
        createEventGroup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CreateGroupSwing(userId,eventGroupName);
            }
        });
        int roleId = new UserService().verifyRole(userId);
        if(roleId==2||roleId==3){
            createEventGroup.setVisible(true);
        }
        JMenuItem createEvent = new JMenuItem("创建瓜");
        createEvent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CreateEventSwing(userId,null);
            }
        });
        event.add(queryEventGroup);
        event.add(queryEvent);
        event.add(createEventGroup);
        event.add(createEvent);

        JMenu viewCollection = new JMenu("查看合集");
        JMenuItem likesCollection = new JMenuItem("点赞合集");
        likesCollection.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LikesEventSwing(userId,eventGroupName);
            }
        });
        JMenuItem collectedCollection = new JMenuItem("收藏合集");
        collectedCollection.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CollectdEventSwing(userId,eventGroupName);
            }
        });
        viewCollection.add(likesCollection);
        viewCollection.add(collectedCollection);

        JMenu help = new JMenu("帮助");
        JMenuItem helpItem = new JMenuItem("帮助文档");
        help.add(helpItem);

        menuBar.add(status);
        menuBar.add(information);
        menuBar.add(event);
        menuBar.add(viewCollection);
        menuBar.add(help);
        jFrame.setJMenuBar(menuBar);
    }
}
