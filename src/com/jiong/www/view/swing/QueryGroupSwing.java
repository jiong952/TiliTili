package com.jiong.www.view.swing;

import com.jiong.www.service.EventGroupService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Mono
 */
public class QueryGroupSwing {
    int userId;
    String eventGroupName;

    public QueryGroupSwing(int userId, String eventGroupName) {
        this.userId = userId;
        this.eventGroupName = eventGroupName;
        EventGroupService eventGroupService = new EventGroupService();
        JFrame jFrame = new JFrame("TiliTili瓜王系统");
        jFrame.setSize(300,300);
        //设置大小
        jFrame.setLocationRelativeTo(null);
        //窗口可见
        jFrame.setResizable(false);
        //不可拉伸
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JPanel jPanel =new JPanel();
        jFrame.add(jPanel);
        jPanel.setLayout(null);
        //绝对布局

        Font font1 = new Font("黑体",Font.PLAIN,30);
        //查询瓜的标签+文本框
        JLabel query = new JLabel("查询瓜圈(输入名字)");
        query.setFont(font1);
        query.setForeground(Color.BLACK);
        query.setBounds(5,50,280,30);
        jPanel.add(query);
        //文本框
        JTextField queryField = new JTextField(70);
        queryField.setBounds(50,150,200,30);
        jPanel.add(queryField);
        //查询按钮
        JButton queryButton = new JButton("查询");
        queryButton.setBounds(200,200,60,30);
        queryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String eventGroupName = queryField.getText();
                if("".equals(eventGroupName)){
                    JOptionPane.showMessageDialog(null,"查询不能为空！","错误",JOptionPane.ERROR_MESSAGE);
                }else {
                    int judge = eventGroupService.verifyEventGroupName(eventGroupName);
                    if(judge==1){
                        jFrame.dispose();
                        new GroupSwing(userId,eventGroupName);
                    }else {
                        JOptionPane.showMessageDialog(null,"查无此瓜圈！","错误",JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        jPanel.add(queryButton);
        //默认关闭
        jFrame.setVisible(true);
    }
}