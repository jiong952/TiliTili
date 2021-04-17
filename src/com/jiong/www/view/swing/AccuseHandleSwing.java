package com.jiong.www.view.swing;

import com.jiong.www.po.Event;
import com.jiong.www.service.serviceImpl.AccuseServiceImpl;
import com.jiong.www.service.serviceImpl.EventServiceImpl;
import com.jiong.www.service.service.IAccuseService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * @author Mono
 */
public class AccuseHandleSwing {
    int userId;
    IAccuseService iAccuseService = new AccuseServiceImpl();
    public static void main(String[] args) {
        new AccuseHandleSwing(8);
    }
    public AccuseHandleSwing(int userId) {
        this.userId = userId;
        EventServiceImpl eventServiceImpl = new EventServiceImpl();
        JFrame jFrame = new JFrame("TiliTili瓜王系统");
        jFrame.setSize(700,600);
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
        Font font = new Font("宋体", Font.BOLD, 40);
        JLabel jLabel = new JLabel("举报待处理");
        jLabel.setFont(font);
        jLabel.setSize(700,100);
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);
        jPanel.add(jLabel);
        //创建一个表格来放举报信息
        String[] columnNames = {"举报人","举报的瓜","举报理由","举报时间"};
        //查询所有的举报信息
        Object[][] rowData = iAccuseService.doRefresh(userId);
        Font font1 = new Font("黑体",Font.PLAIN,15);
        JTable table = new JTable();
        DefaultTableModel defaultTableModel = new DefaultTableModel(rowData, columnNames){
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };
        table.setModel(defaultTableModel);
        table.setFont(font1);
        table.setRowHeight(25);
        table.setBounds(25,100,650,300);
        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setBounds(25,100,650,300);
        jScrollPane.setViewportView(table);
        jPanel.add(jScrollPane);

        //删除瓜
        JButton delete = new JButton("删除瓜");
        delete.setBounds(200,430,90,30);
        delete.addActionListener(e -> {
            if(table.getSelectedRow()<0){
                JOptionPane.showMessageDialog(null,"请先单击选择要删除的瓜!","错误",JOptionPane.ERROR_MESSAGE);
            }else {
                String deleteEventName = (String)rowData[table.getSelectedRow()][1];
                int judge = JOptionPane.showConfirmDialog(null, "您确定要删除" + deleteEventName + "吗？", "确认", JOptionPane.YES_NO_OPTION);
                if(judge==0){
                    //YES
                    Event event = eventServiceImpl.doView(deleteEventName);
                    eventServiceImpl.doDelete(event.getEventId());
                    //刷新待处理列表
                    Object[][] rowData1 = iAccuseService.doRefresh(userId);
                    //重新设置数据源
                    defaultTableModel.setDataVector(rowData1,columnNames);
                }
            }
        });
        jPanel.add(delete);
        //举报静默处理按钮
        JButton notDelete = new JButton("静默处理");
        notDelete.setBounds(350,430,90,30);
        notDelete.addActionListener(e -> {
            String accusedEventName = (String)rowData[table.getSelectedRow()][1];
            Event event = eventServiceImpl.doView(accusedEventName);
            String accusedContent = (String)rowData[table.getSelectedRow()][2];
            iAccuseService.doDelete(event.getEventId(),accusedContent);
            //刷新待处理列表
            Object[][] rowData1 = iAccuseService.doRefresh(userId);
            //重新设置数据源
            defaultTableModel.setDataVector(rowData1,columnNames);
        });
        jPanel.add(notDelete);
        //可见
        jFrame.setVisible(true);
    }
}
