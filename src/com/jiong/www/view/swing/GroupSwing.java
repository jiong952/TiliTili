package com.jiong.www.view.swing;

import com.jiong.www.po.Event;
import com.jiong.www.service.EventGroupServiceImpl;
import com.jiong.www.service.service.IEventService;
import com.jiong.www.service.serviceImpl.EventServiceImpl;
import com.jiong.www.service.UserService;
import com.jiong.www.util.GroupPagingUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * @author Mono
 */
public class GroupSwing extends JFrame {
    int userId;
    String eventGroupName;
    static final int DOUBLE_CLICK = 2;
    static final int PAGE_SIZE = 9;
    static final int  ADMIN = 2;
    static final int  SUPER_ADMIN = 4;
    static final int  VISITOR = 3;
    public GroupSwing(int userId, String eventGroupName) {
        this.userId = userId;
        this.eventGroupName=eventGroupName;
        EventGroupServiceImpl eventGroupServiceImpl =new EventGroupServiceImpl();
        IEventService iEventService = new EventServiceImpl();
        JFrame eventOfGroup = new JFrame("TiliTili瓜王系统");
        eventOfGroup.setSize(1200,800);
        //设置大小
        eventOfGroup.setLocationRelativeTo(null);
        //窗口可见
        eventOfGroup.setResizable(false);
        //不可拉伸
        eventOfGroup.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
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

        //瓜圈简介文本框
        JTextArea description = new JTextArea(eventGroupServiceImpl.viewEventGroup(eventGroupName).getEventGroupDescription());
        description.setFont(font2);
        description.setBounds(270,10,900,80);
        description.setEditable(false);
        jPanel.add(description);
        //给文本框加一个滚动面板
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(270,10,900,80);
        scrollPane.setViewportView(description);
        jPanel.add(scrollPane);

        //加入菜单栏
        new MenuSwing(userId,eventOfGroup,eventGroupName);

        Font font1 = new Font("黑体",Font.PLAIN,36);

        //创建一个列表框来放瓜
        JList<String> list = new JList<>();
        list.setFont(font1);
        list.setFixedCellHeight(56);
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
                    eventOfGroup.dispose();
                    new EventSwing(userId,list.getSelectedValue(),event.getEventId(),eventGroupName);
                }
            }
        });
        DefaultListModel<String> listModel = new DefaultListModel<>();

        List<Event> events = eventGroupServiceImpl.viewEventOfEventGroup(eventGroupName);

        //每一页页面的展示瓜数目
        if(events.size()>=PAGE_SIZE){
            for (int i = 0; i < PAGE_SIZE; i++) {
                listModel.add(i,events.get(i).getEventName());
            }
        }else {
            for (int i = 0; i < events.size(); i++) {
                listModel.add(i,events.get(i).getEventName());
            }
        }
        //向列表框中加入该瓜圈的所有瓜名
        list.setModel(listModel);
        jPanel.add(list);
        //给列表框加滚动面板
        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setBounds(10,100,1150,400);
        jScrollPane.setViewportView(list);
        jPanel.add(jScrollPane);
        //分页处理
        new GroupPagingUtils(events,listModel,jPanel,PAGE_SIZE);

        //查询瓜的标签+文本框
        Font font3 = new Font("黑体",Font.PLAIN,25);
        JLabel query = new JLabel("查询瓜");
        query.setFont(font3);
        query.setForeground(Color.BLACK);
        query.setBounds(5,650,120,30);
        jPanel.add(query);

        //输入瓜圈名字的提示
        JLabel tip = new JLabel("请输入要查询瓜的名字");
        tip.setForeground(Color.red);
        tip.setVisible(false);
        tip.setBounds(120,670,150,30);
        jPanel.add(tip);

        //查询瓜的文本框
        JTextField queryField = new JTextField(70);
        queryField.setBounds(120,650,200,30);
        //查询瓜文本框的监听器
        queryField.getDocument().addDocumentListener(new DocumentListener(){
            @Override
            public void insertUpdate(DocumentEvent e) {
                tip.setVisible(!"".equals(queryField.getText()));
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                tip.setVisible(!"".equals(queryField.getText()));
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });
        jPanel.add(queryField);
        //查询按钮
        JButton queryButton = new JButton("查询");
        queryButton.setBounds(380,650,60,30);
        queryButton.addActionListener(e -> {
            String eventName = queryField.getText();
            if("".equals(eventName)){
                JOptionPane.showMessageDialog(null,"查询不能为空！","错误",JOptionPane.ERROR_MESSAGE);
            }else {
                int judge = iEventService.verifyExist(eventName);
                Event event1 = iEventService.doView(eventName);
                if(judge==1){
                    //瓜存在
                    new EventSwing(userId,eventName,event1.getEventId(),eventGroupName);
                }else {
                    JOptionPane.showMessageDialog(null,"查无此瓜！","错误",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        jPanel.add(queryButton);

        //返回上一页按钮
        JButton back = new JButton("返回");
        back.setBounds(1050,700,78,30);
        back.addActionListener(e -> {
            eventOfGroup.dispose();
            new GroupsSwing(userId,eventGroupName);
        });
        back.setFont(font2);
        jPanel.add(back);

        //删除按钮
        JButton delete = new JButton("删除瓜");
        delete.setBounds(500,650,90,30);
        delete.addActionListener(e -> {
            if(list.getSelectedIndex()>0) {
                int judge0 = eventGroupServiceImpl.verifyOfAdmin(userId, eventGroupName);
                //判断是不是该管理员管理的瓜圈
                if (judge0 == 1) {
                    //是
                    int judge = JOptionPane.showConfirmDialog(null, "您确定要删除此瓜吗？", "确认", JOptionPane.YES_NO_OPTION);
                    if (judge == 0) {
                        //选择是
                        judge0 = iEventService.doDelete(iEventService.doView(list.getSelectedValue()).getEventId());
                        if (judge0 == 1) {
                            JOptionPane.showMessageDialog(null, "删除瓜成功！");
                            new GroupsSwing(userId, eventGroupName);
                        }
                    }
                } else {
                    //不是
                    JOptionPane.showMessageDialog(null, "这不是您管理的瓜圈", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }else {
                JOptionPane.showMessageDialog(null,"请单击选择要删除的瓜","错误",JOptionPane.ERROR_MESSAGE);
            }

        });
        delete.setVisible(false);
        jPanel.add(delete);

        //创建瓜的按钮
        JButton create = new JButton("创建本瓜圈瓜");
        create.setBounds(700,650,120,30);
        create.addActionListener(e -> new CreateEventSwing(userId,eventGroupName));
        jPanel.add(create);
        //刷新按钮，解决创建瓜之后页面无法立刻更新的问题
        JButton refresh = new JButton("刷新");
        refresh.setBounds(820,650,90,30);
        refresh.addActionListener(e -> {
            DefaultListModel<String> listModel1 = new DefaultListModel<>();
            List<Event> events1 = eventGroupServiceImpl.viewEventOfEventGroup(eventGroupName);
            for (int i = 0; i < events1.size(); i++) {
                listModel1.add(i,events1.get(i).getEventName());
            }
            //向列表框中加入该瓜圈的所有瓜名
            list.setModel(listModel1);
        });
        jPanel.add(refresh);

        //直接用roleId来区分不同的身份，使不同角色看到不同的界面
        int roleId = new UserService().verifyRole(userId);

        //删除瓜圈 创建瓜圈
        if(roleId==ADMIN||roleId==SUPER_ADMIN){
            //管理员或者是超级管理员
            delete.setVisible(true);
        }

        if(roleId==VISITOR){
            //游客不能创建瓜 也不需要更新
            create.setVisible(false);
            refresh.setVisible(false);
        }
        eventOfGroup.setVisible(true);
    }
}
