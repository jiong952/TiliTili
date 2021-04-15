package com.jiong.www.view.swing;

import com.jiong.www.service.AccuseService;

import javax.swing.*;
import java.awt.*;

/**
 * @author Mono
 */
public class AccuseSwing extends JFrame {
    int userId;
    int eventId;
    String eventName;
    public AccuseSwing(int userId,String eventName, int eventId) throws HeadlessException {
        this.userId = userId;
        this.eventId = eventId;
        this.eventName=eventName;
        AccuseService accuseService = new AccuseService();
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
        JLabel jLabel = new JLabel("请输入举报理由");
        jLabel.setFont(font1);
        jLabel.setForeground(Color.BLACK);
        jLabel.setBounds(5,30,280,30);
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);
        jPanel.add(jLabel);

        //文本域
        JTextArea jTextArea = new JTextArea(8,70);
        jTextArea.setLineWrap(true);
        jTextArea.setBounds(50,80,200,100);
        jPanel.add(jTextArea);
        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setBounds(50,80,200,100);
        jScrollPane.setViewportView(jTextArea);
        jPanel.add(jScrollPane);

        //查询按钮
        JButton jButton = new JButton("举报");
        jButton.setBounds(110,200,60,30);
        jButton.addActionListener(e -> {
            if(!"".equals(jTextArea.getText())){
                int judge = JOptionPane.showConfirmDialog(null, "您确定要举报" + eventName + "吗?", "确认", JOptionPane.YES_NO_OPTION);
                if(judge==0){
                    //YES
                    int row = accuseService.accuseEvent(eventId, userId, jTextArea.getText());
                    if(row==1){
                        JOptionPane.showMessageDialog(null,"举报成功");
                        jFrame.dispose();
                    }
                }else {
                    JOptionPane.showMessageDialog(null,"做一个文明吃瓜群众！");
                }
           }else {
                JOptionPane.showMessageDialog(null,"举报理由不可以为空","错误",JOptionPane.ERROR_MESSAGE);
            }
        });
        jPanel.add(jButton);
        //可见
        jFrame.setVisible(true);
    }
}
