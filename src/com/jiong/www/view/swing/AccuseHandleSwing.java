package com.jiong.www.view.swing;

import com.jiong.www.po.Accuse;
import com.jiong.www.po.Event;
import com.jiong.www.service.AccuseService;
import com.jiong.www.service.EventService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * @author Mono
 */
public class AccuseHandleSwing {
    int userId;

    public static void main(String[] args) {
        new AccuseHandleSwing(8);
    }
    public AccuseHandleSwing(int userId) {
        this.userId = userId;
        EventService eventService = new EventService();
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
        List<Accuse> accuses = new AccuseService().viewAccusation(userId);
        Object[][] rowData = new Object[accuses.size()][4];
        for (int i = 0; i < accuses.size(); i++) {
            rowData[i][0]=accuses.get(i).getAccusedUserName();
            rowData[i][1]=accuses.get(i).getAccusedEventName();
            rowData[i][2]=accuses.get(i).getAccusedContent();
            rowData[i][3]=accuses.get(i).getAccuseTime();
        }
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
                    Event event = eventService.viewEvent(deleteEventName);
                    eventService.deleteEvent(event.getEventId());
                    //刷新待处理列表
                    List<Accuse> accuses1 = new AccuseService().viewAccusation(userId);
                    Object[][] rowData1 = new Object[accuses1.size()][4];
                    for (int i = 0; i < accuses1.size(); i++) {
                        rowData1[i][0]=accuses1.get(i).getAccusedUserName();
                        rowData1[i][1]=accuses1.get(i).getAccusedEventName();
                        rowData1[i][2]=accuses1.get(i).getAccusedContent();
                        rowData1[i][3]=accuses1.get(i).getAccuseTime();
                    }
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
            Event event = eventService.viewEvent(accusedEventName);
            String accusedContent = (String)rowData[table.getSelectedRow()][2];
            new AccuseService().deleteAccuse(event.getEventId(),accusedContent);
            //刷新待处理列表
            List<Accuse> accuses1 = new AccuseService().viewAccusation(userId);
            Object[][] rowData1 = new Object[accuses1.size()][4];
            for (int i = 0; i < accuses1.size(); i++) {
                rowData1[i][0]=accuses1.get(i).getAccusedUserName();
                rowData1[i][1]=accuses1.get(i).getAccusedEventName();
                rowData1[i][2]=accuses1.get(i).getAccusedContent();
                rowData1[i][3]=accuses1.get(i).getAccuseTime();
            }
            //重新设置数据源
            defaultTableModel.setDataVector(rowData1,columnNames);
        });
        jPanel.add(notDelete);
        //可见
        jFrame.setVisible(true);
    }
}
