package com.jiong.www.view.swing;

import com.jiong.www.po.Event;
import com.jiong.www.service.EventGroupService;
import com.jiong.www.service.EventService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class EventOfGroupSwing extends JFrame {
    int userId;
    String eventGroupName;

    public static void main(String[] args) {
        new EventOfGroupSwing(2,"范冰冰");
    }
    public EventOfGroupSwing(int userId, String eventGroupName) throws HeadlessException {
        this.userId = userId;
        this.eventGroupName=eventGroupName;
        EventGroupService eventGroupService =new EventGroupService();
        EventService eventService = new EventService();
        JFrame eventOfGroup = new JFrame("TiliTili瓜王系统");
        eventOfGroup.setSize(1200,800);
        //设置大小
        eventOfGroup.setLocationRelativeTo(null);
        //窗口可见
        eventOfGroup.setResizable(false);
        //不可拉伸
        eventOfGroup.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //默认关闭

        JPanel jPanel =new JPanel();
        eventOfGroup.add(jPanel);
        jPanel.setLayout(null);
        //绝对布局
        Font font = new Font("宋体", Font.BOLD, 40);
        JLabel jLabel = new JLabel(eventGroupName);
        jLabel.setFont(font);
        jLabel.setBounds(5,5,1000,100);
        jPanel.add(jLabel);

        Font font2 = new Font("黑体", Font.PLAIN, 18);

        JLabel jLabel2 = new JLabel("瓜圈名:");
        jLabel2.setBounds(1,1,90,30);
        jLabel2.setForeground(Color.PINK);
        jLabel2.setFont(font2);
        jPanel.add(jLabel2);

        JLabel jLabel1 = new JLabel("瓜圈简介:");
        jLabel1.setBounds(160,5,90,30);
        jLabel1.setFont(font2);
        jPanel.add(jLabel1);
        JTextArea description = new JTextArea(eventGroupService.viewEventGroupDescription(eventGroupName));

        description.setFont(font2);
        description.setBounds(270,10,900,80);
        description.setEditable(false);
        jPanel.add(description);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(270,10,900,80);
        scrollPane.setViewportView(description);
        jPanel.add(scrollPane);
        //加入菜单栏
        new MenuSwing(userId,eventOfGroup);

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
                    Event event = eventService.viewEvent(list.getSelectedValue());
                    list.setToolTipText("作者："+event.getPublisherName()+"发布时间："+event.getCreateTime()+"点赞数："+event.getLikesNum()
                    +"收藏数："+event.getCollectionNum()+"评论数："+event.getCommentNum());
                }
            }
        });
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(e.getClickCount()==2){
                    //进入瓜界面
                    Event event = eventService.viewEvent(list.getSelectedValue());
                    new SelectedEventSwing(userId,list.getSelectedValue(),event.getEventId(),eventGroupName);
                }
            }
        });
        DefaultListModel<String> listModel = new DefaultListModel<String>();
        List<Event> events = eventGroupService.viewEventOfEventGroup(eventGroupName);
        for (int i = 0; i < events.size(); i++) {
            listModel.add(i,events.get(i).getEventName());
        }
        //向列表框中加入该瓜圈的所有瓜名
        list.setModel(listModel);
        jPanel.add(list);
        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setBounds(10,100,1150,600);
        jScrollPane.setViewportView(list);
        jPanel.add(jScrollPane);
        eventOfGroup.setVisible(true);
    }
}
