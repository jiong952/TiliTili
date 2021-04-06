package com.jiong.www.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Option extends JFrame implements ActionListener {
    JFrame option;
    JPanel jPanel;
    JLabel jLabel;
    JButton event;
    JButton userInformation;
    int userId;
    public static void main(String[] args)  {
    }
    public Option(int userId)  {
        this.userId = userId;
        option = new JFrame("TiliTili瓜王系统");
        option.setSize(1200,800);
        //设置大小
        option.setLocationRelativeTo(null);
        //窗口可见
        option.setResizable(false);
        //不可拉伸
        option.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //默认关闭
        option.setVisible(true);

        jPanel = new JPanel();
        jPanel.setLayout(null);
        option.add(jPanel);

        Font font = new Font("宋体", Font.BOLD, 50);

        jLabel = new JLabel("TiliTili");
        jLabel.setFont(font);
        jLabel.setBounds(0,0,250,100);
        jLabel.setForeground(Color.green);
        jPanel.add(jLabel);

        event = new JButton("进入瓜网");
        event.setFont(new Font("黑体",Font.BOLD,15));
        event.setBounds(400,400,120,30);
        event.addActionListener(this);
        jPanel.add(event);

        userInformation = new JButton("用户信息");
        userInformation.setFont(new Font("黑体",Font.BOLD,15));
        userInformation.setBounds(550,400,120,30);
        userInformation.addActionListener(this);
        jPanel.add(userInformation);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==event){
            //进入瓜网
        }
        if(e.getSource()==userInformation){
            //进入信息界面
            option.dispose();
            new UserInformation(userId);
        }
    }
}
