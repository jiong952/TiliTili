package com.jiong.www.view.swing;

import com.jiong.www.po.EventGroup;
import com.jiong.www.service.serviceImpl.EventGroupServiceImpl;
import com.jiong.www.service.serviceImpl.EventServiceImpl;

import javax.swing.*;
import java.awt.*;
/**
 * @author Mono
 */
public class CreateEventSwing {
    int userId;
    String eventGroupName;
    public CreateEventSwing(int userId, String eventGroupName) {
        this.userId = userId;
        this.eventGroupName = eventGroupName;
        EventGroupServiceImpl eventGroupServiceImpl = new EventGroupServiceImpl();
        JFrame jFrame = new JFrame("TiliTili瓜王系统");
        jFrame.setSize(500,560);
        //设置大小
        jFrame.setLocationRelativeTo(null);
        //窗口可见
        jFrame.setResizable(false);
        //不可拉伸
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        //默认关闭

        JPanel jPanel =new JPanel();
        jFrame.add(jPanel);
        jPanel.setLayout(null);
        //绝对布局
        Font font = new Font("黑体",Font.BOLD,14);
        //瓜圈名标签+文本框
        JLabel groupNameLabel = new JLabel("瓜的瓜圈名:");
        groupNameLabel.setBounds(80,50,100,30);
        groupNameLabel.setFont(font);
        groupNameLabel.setVisible(false);
        jPanel.add(groupNameLabel);
        JTextField groupField = new JTextField(25);
        groupField.setBounds(180,50,100,20);
        groupField.setVisible(false);
        jPanel.add(groupField);
        //不是从瓜圈页面跳转的而是通过菜单栏跳转的
        if(eventGroupName ==null){
            groupNameLabel.setVisible(true);
            groupField.setVisible(true);
        }
        //瓜名标签+文本框
        JLabel eventNameLabel = new JLabel("新瓜名:");
        eventNameLabel.setBounds(100,80,70,30);
        eventNameLabel.setFont(font);
        jPanel.add(eventNameLabel);
        JTextField jTextField = new JTextField(25);
        jTextField.setBounds(180,85,100,20);
        jPanel.add(jTextField);
        //新瓜内容标签+文本框+滚动面板
        JLabel eventContentLabel = new JLabel("新瓜内容:");
        eventContentLabel.setBounds(100,120,80,30);
        eventContentLabel.setFont(font);
        jPanel.add(eventContentLabel);
        JTextArea jTextArea = new JTextArea();
        jTextArea.setBounds(100,150,300,150);
        jPanel.add(jTextArea);
        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setBounds(100,150,300,150);
        jScrollPane.setViewportView(jTextArea);
        jPanel.add(jScrollPane);



        JButton create = new JButton("创建");
        create.setBounds(50,350,60,20);
        create.addActionListener(e -> {
            if(eventGroupName ==null){
                String groupName = groupField.getText();
                EventGroup eventGroup = eventGroupServiceImpl.viewEventGroup(groupName);
                int judge = new EventServiceImpl().doCreate(userId, eventGroup.getEventGroupId(), jTextField.getText(), jTextArea.getText());
                if(judge==1){
                    JOptionPane.showMessageDialog(null,"创建成功！");
                }
            }
            else {
                EventGroup eventGroup = eventGroupServiceImpl.viewEventGroup(eventGroupName);
                int judge = new EventServiceImpl().doCreate(userId, eventGroup.getEventGroupId(), jTextField.getText(), jTextArea.getText());
                if(judge==1){
                    JOptionPane.showMessageDialog(null,"创建成功！");
                }
            }
            jFrame.dispose();
        });
        jPanel.add(create);
        JButton reset = new JButton("重置");
        reset.setBounds(150,350,60,20);
        reset.addActionListener(e -> {
            jTextField.setText("");
            jTextArea.setText("");
        });
        jPanel.add(reset);
        JButton back = new JButton("返回");
        back.setBounds(250,350,60,20);
        back.addActionListener(e -> {
            jFrame.dispose();
            new GroupsSwing(userId, eventGroupName);
        });
        jPanel.add(back);

        jFrame.setVisible(true);
    }
}
