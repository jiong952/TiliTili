package com.jiong.www.view.swing;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuSwing {
    int userId;
    JFrame jFrame;
    public MenuSwing(int userId, JFrame jFrame) {
        this.userId = userId;
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
                new UserInformationSwing(userId);
            }
        });
        JMenuItem modifyPassword = new JMenuItem("修改密码");
        modifyPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ChangePasswordSwing(userId);
            }
        });
        information.add(modifyInformation);
        information.add(modifyPassword);

        JMenu event = new JMenu("瓜");
        JMenuItem queryEventGroup = new JMenuItem("查询瓜圈");
        JMenuItem queryEvent = new JMenuItem("查询瓜");
        JMenuItem createEventGroup = new JMenuItem("创建瓜圈");
        JMenuItem createEvent = new JMenuItem("创建瓜");
        event.add(queryEventGroup);
        event.add(queryEvent);
        event.add(createEventGroup);
        event.add(createEvent);

        JMenu viewCollection = new JMenu("查看合集");
        JMenuItem likesCollection = new JMenuItem("点赞合集");
        JMenuItem Collection = new JMenuItem("收藏合集");
        viewCollection.add(likesCollection);
        viewCollection.add(Collection);

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
