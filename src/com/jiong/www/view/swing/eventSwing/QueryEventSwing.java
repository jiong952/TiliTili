package com.jiong.www.view.swing.eventSwing;

import com.jiong.www.po.Event;
import com.jiong.www.service.service.IEventService;
import com.jiong.www.service.serviceImpl.EventServiceImpl;

import javax.swing.*;
import java.awt.*;

/**
 * @author Mono
 */
public class QueryEventSwing extends JFrame{
    int userId;
    String eventGroupName;

    public QueryEventSwing(int userId, String eventGroupName)  {
        this.userId = userId;
        this.eventGroupName = eventGroupName;
        IEventService iEventService = new EventServiceImpl();
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
        JLabel query = new JLabel("查询瓜(输入名字)");
        query.setFont(font1);
        query.setForeground(Color.BLACK);
        query.setBounds(5,50,280,30);
        jPanel.add(query);
        //查询瓜文本框
        JTextField queryField = new JTextField(70);
        queryField.setBounds(50,150,200,30);
        jPanel.add(queryField);
        //查询瓜按钮
        JButton queryButton = new JButton("查询");
        queryButton.setBounds(200,200,60,30);
        queryButton.addActionListener(e -> {
            String eventName = queryField.getText();
            if("".equals(eventName)){
                JOptionPane.showMessageDialog(null,"查询不能为空！","错误",JOptionPane.ERROR_MESSAGE);
            }else {
                int judge = iEventService.isExist(eventName);
                //验证瓜名是否存在
                Event event1 = iEventService.find(eventName);
                if(judge==1){
                    //存在
                    jFrame.dispose();
                    new EventSwing(userId,eventName,event1.getEventId(),eventGroupName);
                }else {
                    JOptionPane.showMessageDialog(null,"查无此瓜！","错误",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        jPanel.add(queryButton);
        jFrame.setVisible(true);
        //窗口可见
    }
}
