package com.jiong.www.view.swing;


import com.jiong.www.service.CommentService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CommentingSwing extends JFrame {
    int userId;
    int eventId;
    String eventName;
    String eventGroupName;
    public static void main(String[] args) {
        new CommentingSwing(2,7,"赵英俊留给世界最后的话：不要把我忘了，永别了","范冰冰");
    }
    public CommentingSwing(int userId, int eventId, String eventName, String eventGroupName) {
        this.userId = userId;
        this.eventId = eventId;
        this.eventName=eventName;
        this.eventGroupName=eventGroupName;

        JFrame commenting = new JFrame("TiliTili瓜王系统");
        commenting.setSize(800,500);
        //设置大小
        commenting.setLocationRelativeTo(null);
        //窗口可见
        commenting.setResizable(false);
        //不可拉伸
        commenting.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        //默认关闭

        JPanel jPanel = new JPanel();
        commenting.add(jPanel);
        jPanel.setLayout(null);
        //绝对布局

        Font font = new Font("宋体", Font.BOLD, 40);
        JLabel jLabel = new JLabel("评论");
        jLabel.setFont(font);
        jLabel.setBounds(5,5,1200,100);
        jLabel.setForeground(Color.PINK);
        jPanel.add(jLabel);

        JTextArea jTextArea = new JTextArea(30,15);
        jTextArea.setLineWrap(true);
        //自动换行
        jTextArea.setBounds(80,80,500,220);
        jPanel.add(jTextArea);
        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setBounds(80,80,500,220);
        jScrollPane.setViewportView(jTextArea);
        jPanel.add(jScrollPane);

        JButton save = new JButton("保存");
        save.setBounds(100,400,80,20);
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CommentService().comment(userId,eventId,jTextArea.getText());
                JOptionPane.showMessageDialog(null,"评论成功！");
                commenting.dispose();
                new SelectedEventSwing(userId,eventName,eventId,eventGroupName);
            }
        });
        JButton reset = new JButton("重置");
        reset.setBounds(300,400,80,20);
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jTextArea.setText("");
            }
        });
        JButton cancel = new JButton("返回");
        cancel.setBounds(500,400,80,20);
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                commenting.dispose();
                new SelectedEventSwing(userId,eventName,eventId,eventGroupName);
            }
        });
        jPanel.add(save);
        jPanel.add(reset);
        jPanel.add(cancel);

        commenting.setVisible(true);
        //可见
    }
}
