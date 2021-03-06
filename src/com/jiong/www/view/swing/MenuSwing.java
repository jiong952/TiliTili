package com.jiong.www.view.swing;

import com.jiong.www.service.serviceImpl.UserServiceImpl;
import com.jiong.www.view.swing.accuseSwing.AccuseHandleSwing;
import com.jiong.www.view.swing.collectionSwing.CollectdEventSwing;
import com.jiong.www.view.swing.eventGroupSwing.CreateGroupSwing;
import com.jiong.www.view.swing.eventGroupSwing.QueryGroupSwing;
import com.jiong.www.view.swing.eventSwing.CreateEventSwing;
import com.jiong.www.view.swing.eventSwing.QueryEventSwing;
import com.jiong.www.view.swing.likesSwing.LikesEventSwing;
import com.jiong.www.view.swing.userSwing.InformationSwing;
import com.jiong.www.view.swing.userSwing.LoginSwing;
import com.jiong.www.view.swing.userSwing.PasswordSwing;
import com.jiong.www.view.swing.userSwing.RegisterSwing;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Mono
 */
public class MenuSwing {
    int userId;
    String eventGroupName;
    JFrame jFrame;
    static final int  ADMIN = 2;
    static final int  SUPER_ADMIN = 4;
    static final int  VISITOR = 3;
    public MenuSwing(int userId, JFrame jFrame, String eventGroupName) {
        this.userId = userId;
        this.eventGroupName=eventGroupName;
        this.jFrame=jFrame;
        //菜单栏
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBorderPainted(true);

        //用户状态菜单
        JMenu status = new JMenu("用户状态");
        JMenuItem exitLogin = new JMenuItem("退出登录");
        exitLogin.addActionListener(e -> {
            if(userId==0){
                JOptionPane.showMessageDialog(null,"请先登录","错误",JOptionPane.ERROR_MESSAGE);
            }else {
                jFrame.dispose();
                new WelcomeSwing();
                //回到主界面
            }
        });
        JMenuItem register = new JMenuItem("注册");
        register.addActionListener(e -> {
            if(userId!=0){
                JOptionPane.showMessageDialog(null,"请先退出登录","错误",JOptionPane.ERROR_MESSAGE);
            } else {
                new RegisterSwing(0);
            }
        });
        JMenuItem login = new JMenuItem("登录");
        login.addActionListener(e -> {
            if(userId==0){
                new LoginSwing();
            }else {
                JOptionPane.showMessageDialog(null,"您已登录！","错误",JOptionPane.ERROR_MESSAGE);
            }
        });
        status.add(exitLogin);
        status.add(register);
        status.add(login);

        //个人信息菜单
        JMenu information = new JMenu("个人信息");
        JMenuItem modifyInformation = new JMenuItem("修改信息");
        modifyInformation.addActionListener(e -> new InformationSwing(userId,eventGroupName));
        JMenuItem modifyPassword = new JMenuItem("修改密码");
        modifyPassword.addActionListener(e -> new PasswordSwing(userId,eventGroupName));
        information.add(modifyInformation);
        information.add(modifyPassword);

        //瓜菜单
        JMenu event = new JMenu("瓜");
        JMenuItem queryEventGroup = new JMenuItem("查询瓜圈");
        queryEventGroup.addActionListener(e -> new QueryGroupSwing(userId,eventGroupName));
        JMenuItem queryEvent = new JMenuItem("查询瓜");
        queryEvent.addActionListener(e -> new QueryEventSwing(userId,eventGroupName));
        JMenuItem createEventGroup = new JMenuItem("创建瓜圈");
        createEventGroup.setVisible(false);
        createEventGroup.addActionListener(e -> new CreateGroupSwing(userId,eventGroupName));
        JMenuItem createEvent = new JMenuItem("创建瓜");
        createEvent.addActionListener(e -> new CreateEventSwing(userId,null));
        event.add(queryEventGroup);
        event.add(queryEvent);
        event.add(createEventGroup);
        event.add(createEvent);

        //查看合集菜单
        JMenu viewCollection = new JMenu("查看合集");
        JMenuItem likesCollection = new JMenuItem("点赞合集");
        likesCollection.addActionListener(e -> new LikesEventSwing(userId,eventGroupName));
        JMenuItem collectedCollection = new JMenuItem("收藏合集");
        collectedCollection.addActionListener(e -> new CollectdEventSwing(userId,eventGroupName));
        viewCollection.add(likesCollection);
        viewCollection.add(collectedCollection);


        //待处理菜单
        JMenu handle = new JMenu("待处理");
        handle.setVisible(false);
        JMenuItem accuseHandle = new JMenuItem("举报处理");
        accuseHandle.addActionListener(e -> new AccuseHandleSwing(userId));
        handle.add(accuseHandle);

        //管理员账号管理
        JMenu admin = new JMenu("管理员");
        admin.setVisible(false);
        JMenuItem create = new JMenuItem("添加管理员");
        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegisterSwing(1);
            }
        });
        admin.add(create);

        menuBar.add(status);
        menuBar.add(information);
        menuBar.add(event);
        menuBar.add(viewCollection);
        menuBar.add(handle);
        menuBar.add(admin);

        int roleId = new UserServiceImpl().queryRole(userId);

        if(roleId==ADMIN||roleId==SUPER_ADMIN){
            //管理员 超管
            createEventGroup.setVisible(true);
            handle.setVisible(true);
        }
        if(roleId==VISITOR){
            information.setVisible(false);
            event.setVisible(false);
            viewCollection.setVisible(false);
        }
        if(roleId==SUPER_ADMIN){
            admin.setVisible(true);
        }
        jFrame.setJMenuBar(menuBar);
    }
}
