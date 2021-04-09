package com.jiong.www.view.swing;

import com.jiong.www.po.EventGroup;
import com.jiong.www.service.EventGroupService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class EventWebSwing extends JFrame {
    int userId;

    public static void main(String[] args) {
        new EventWebSwing(2);
    }
    public EventWebSwing(int userId) {
        this.userId = userId;
        EventGroupService eventGroupService = new EventGroupService();
        JFrame eventGroup = new JFrame("TiliTili瓜王系统");
        eventGroup.setSize(1200,800);
        //设置大小
        eventGroup.setLocationRelativeTo(null);
        //窗口可见
        eventGroup.setResizable(false);
        //不可拉伸
        eventGroup.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //默认关闭

        JPanel jPanel =new JPanel();
        eventGroup.add(jPanel);
        jPanel.setLayout(null);
        //绝对布局

        Font font = new Font("宋体", Font.BOLD, 50);
        JLabel jLabel = new JLabel("TiliTili瓜网");
        jLabel.setFont(font);
        jLabel.setBounds(400,10,450,100);
        jPanel.add(jLabel);

        new MenuSwing(userId,eventGroup);

        Font font1 = new Font("黑体",Font.PLAIN,25);

        JList<String> list = new JList<>();
        list.setFont(font1);
        list.setFixedCellHeight(40);
        list.setSelectionBackground(Color.gray);
        //单元格的大小
        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                //单击是选择(单击会有tips提示内容简介) 双击是进入
                if(!list.getValueIsAdjusting()){
                    String eventGroupDescription = eventGroupService.viewEventGroupDescription(list.getSelectedValue());
                    list.setToolTipText("内容简介:"+eventGroupDescription);
                }
            }
        });
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(e.getClickCount()==2){
                    new EventOfGroupSwing(userId,list.getSelectedValue());
                }
            }
        });
        DefaultListModel<String> listModel = new DefaultListModel<String>();
        List<EventGroup> eventGroups = eventGroupService.viewEventGroup();
        for (int i = 0; i < eventGroups.size(); i++) {
            listModel.add(i,eventGroups.get(i).getEventGroupName());
        }
        //向列表框中加入所有的瓜圈名
        list.setModel(listModel);
        jPanel.add(list);

        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setBounds(10,100,1150,600);
        jScrollPane.setViewportView(list);
        jPanel.add(jScrollPane);
        //窗口可见
        eventGroup.setVisible(true);

    }
}
