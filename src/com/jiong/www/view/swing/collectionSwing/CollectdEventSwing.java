package com.jiong.www.view.swing.collectionSwing;

import com.jiong.www.po.Event;
import com.jiong.www.service.service.IEventService;
import com.jiong.www.service.serviceImpl.CollectionServiceImpl;
import com.jiong.www.service.serviceImpl.EventServiceImpl;
import com.jiong.www.service.service.ICollectionService;
import com.jiong.www.view.swing.eventSwing.EventSwing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * @author Mono
 */
public class CollectdEventSwing {
    int userId;
    String eventGroupName;
    static final int DOUBLE_CLICK = 2;
    public CollectdEventSwing(int userId, String eventGroupName) {
        this.userId = userId;
        this.eventGroupName = eventGroupName;
        IEventService iEventService = new EventServiceImpl();
        ICollectionService iCollectionService = new CollectionServiceImpl();
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
        JLabel jLabel = new JLabel("我的收藏合集");
        jLabel.setFont(font);
        jLabel.setSize(700,100);
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);
        jPanel.add(jLabel);

        //绝对布局
        Font font1 = new Font("黑体",Font.PLAIN,25);
        //创建一个列表框放收藏合集,双击就可以跳转
        JList<String> list = new JList<>();
        list.setFont(font1);
        list.setFixedCellHeight(30);
        //单元格的大小
        list.setSelectionBackground(Color.gray);
        list.addListSelectionListener(e -> {
            //单击是选择(单击会有tips提示内容简介) 双击是进入
            if(!list.getValueIsAdjusting()){
                Event event = iEventService.doView(list.getSelectedValue());
                list.setToolTipText("作者："+event.getPublisherName()+"发布时间："+event.getCreateTime()+"点赞数："+event.getLikesNum()
                        +"收藏数："+event.getCollectionNum()+"评论数："+event.getCommentNum());
            }
        });
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(e.getClickCount()==DOUBLE_CLICK){
                    //进入瓜界面
                    Event event = iEventService.doView(list.getSelectedValue());
                    new EventSwing(userId,list.getSelectedValue(),event.getEventId(), iEventService.queryGroupName(event.getEventId()));
                }
            }
        });
        DefaultListModel<String> listModel = new DefaultListModel<>();
        List<Event> events = iCollectionService.findAll(userId);
        for (int i = 0; i < events.size(); i++) {
            listModel.add(i,events.get(i).getEventName());
        }
        //向列表框中加入该瓜圈的所有瓜名
        list.setModel(listModel);
        jPanel.add(list);
        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setBounds(25,100,650,300);
        jScrollPane.setViewportView(list);
        jPanel.add(jScrollPane);

        //取消收藏按钮实时刷新
        JButton delete = new JButton("取消收藏");
        delete.setBounds(300,430,90,30);
        delete.addActionListener(e -> {
            if(list.getSelectedIndex()>=0){
                int judge = JOptionPane.showConfirmDialog(null, "您确定要取消收藏" + list.getSelectedValue() + "吗？", "确认", JOptionPane.YES_NO_OPTION);
                if(judge==0){
                    //YES
                    Event event = iEventService.doView(list.getSelectedValue());
                    iCollectionService.doCancelCollect(userId,event.getEventId());
                    //刷新
                    DefaultListModel<String> listModel1;
                    listModel1 = iCollectionService.doRefresh(userId);
                    list.setModel(listModel1);
                }
            }else {
                JOptionPane.showMessageDialog(null,"请单击选择要取消收藏的瓜！","错误",JOptionPane.ERROR_MESSAGE);
            }
        });
        jPanel.add(delete);
        jFrame.setVisible(true);
    }
}
