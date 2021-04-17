package com.jiong.www.view.swing;

import com.jiong.www.po.EventGroup;
import com.jiong.www.service.EventGroupServiceImpl;
import com.jiong.www.service.IEventGroupService;
import com.jiong.www.service.UserService;
import com.jiong.www.util.GroupsPagingUtils;

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
public class GroupsSwing extends JFrame {
    int userId;
    String eventGroupName;
    static final int DOUBLE_CLICK = 2;
    static final int PAGE_SIZE = 9;

    public GroupsSwing(int userId, String eventGroupName) {
        this.userId = userId;
        this.eventGroupName = eventGroupName;

        IEventGroupService iEventGroupService = new EventGroupServiceImpl();
        JFrame eventGroup = new JFrame("TiliTili瓜王系统");
        eventGroup.setSize(1200,800);
        //设置大小
        eventGroup.setLocationRelativeTo(null);
        //窗口可见
        eventGroup.setResizable(false);
        //不可拉伸
        eventGroup.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
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

        new MenuSwing(userId,eventGroup,eventGroupName);

        Font font1 = new Font("黑体",Font.PLAIN,36);

        JList<String> list = new JList<>();
        list.setFont(font1);
        list.setFixedCellHeight(56);
        //单元格的大小
        list.setSelectionBackground(Color.gray);
        list.addListSelectionListener(e -> {
            //单击是选择(单击会有tips提示内容简介)
            if(!list.getValueIsAdjusting()){
                list.setToolTipText("内容简介:"+ iEventGroupService.viewEventGroup(list.getSelectedValue()).getEventGroupDescription());
            }
        });
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                //双击是进入瓜圈
                if(e.getClickCount()==DOUBLE_CLICK){
                    eventGroup.dispose();
                    new GroupSwing(userId,list.getSelectedValue());
                }
            }
        });
        DefaultListModel<String> listModel = new DefaultListModel<>();
        List<EventGroup> eventGroups = iEventGroupService.viewAllEventGroup();

        //每一页页面的展示瓜圈数目
        if(eventGroups.size()>= PAGE_SIZE){
            for (int i = 0; i < PAGE_SIZE; i++) {
                listModel.add(i,eventGroups.get(i).getEventGroupName());
            }
        }else {
            for (int i = 0; i < eventGroups.size(); i++) {
                listModel.add(i,eventGroups.get(i).getEventGroupName());
            }
        }
        //向列表框中加入所有的瓜圈名
        list.setModel(listModel);
        jPanel.add(list);
        //加滚动面板
        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setBounds(10,100,1150,400);
        jScrollPane.setViewportView(list);
        jPanel.add(jScrollPane);
        //分页处理
        new GroupsPagingUtils(eventGroups,listModel,jPanel, PAGE_SIZE);

        //查询瓜圈的标签+文本框
        Font font2 = new Font("黑体",Font.PLAIN,25);
        JLabel query = new JLabel("查询瓜圈");
        query.setFont(font2);
        query.setForeground(Color.BLACK);
        query.setBounds(5,650,120,30);
        jPanel.add(query);

        //输入瓜圈名字的提示标签
        JLabel tip = new JLabel("请输入要查询瓜圈的名字");
        tip.setForeground(Color.red);
        tip.setVisible(false);
        tip.setBounds(120,670,150,30);
        jPanel.add(tip);

        //查询瓜圈的名字文本框
        JTextField queryField = new JTextField(30);
        queryField.setBounds(120,650,120,30);
        //文本框的增删监听器
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
        //查询瓜圈按钮
        JButton queryButton = new JButton("查询");
        queryButton.setBounds(245,650,60,30);
        queryButton.addActionListener(e -> {
            String eventGroupName1 = queryField.getText();
            if("".equals(eventGroupName1)){
                JOptionPane.showMessageDialog(null,"查询不能为空！","错误",JOptionPane.ERROR_MESSAGE);
            }else {
            int judge = iEventGroupService.verifyExist(eventGroupName1);
            if(judge==1){
            new GroupSwing(userId, eventGroupName1);
            }else {
                JOptionPane.showMessageDialog(null,"查无此瓜圈！","错误",JOptionPane.ERROR_MESSAGE);
            }
            }
        });
        jPanel.add(queryButton);

        //删除瓜圈的按钮
        JButton delete = new JButton("删除瓜圈");
        delete.setBounds(350,650,90,30);
        delete.addActionListener(e -> {
            if(list.getSelectedIndex()>0){
                int judge0 = iEventGroupService.verifyOfAdmin(userId, list.getSelectedValue());
                //判断是不是该管理员管理的瓜圈
                if(judge0==1){
                    //YES
                    int judge = JOptionPane.showConfirmDialog(null, "您确定要删除" + list.getSelectedValue() + "瓜圈吗？", "确认", JOptionPane.YES_NO_OPTION);
                    if(judge==0){
                        //确认是
                        judge0 = iEventGroupService.doDelete(list.getSelectedValue(), userId);
                        if(judge0==1){
                            JOptionPane.showMessageDialog(null,"删除瓜圈成功！");
                            //刷新
                            DefaultListModel<String> listModel1 = new DefaultListModel<>();
                            List<EventGroup> eventGroups1 = iEventGroupService.viewAllEventGroup();
                            for (int i = 0; i < eventGroups1.size(); i++) {
                                listModel1.add(i,eventGroups1.get(i).getEventGroupName());
                            }
                            //向列表框中加入所有的瓜圈名
                            list.setModel(listModel1);
                        }else {
                            JOptionPane.showMessageDialog(null,"删除失败","错误",JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }else {
                    //不是
                    JOptionPane.showMessageDialog(null,"这不是您管理的瓜圈","错误",JOptionPane.ERROR_MESSAGE);
                }
            }else {
                JOptionPane.showMessageDialog(null,"请单击选择要删除的瓜圈","错误",JOptionPane.ERROR_MESSAGE);
            }
        });
        delete.setVisible(false);
        jPanel.add(delete);

        //创建瓜圈按钮
        JButton create = new JButton("创建瓜圈");
        create.setBounds(550,650,90,30);
        create.addActionListener(e -> {
            //创建瓜圈
            new CreateGroupSwing(userId,eventGroupName);
        });
        create.setVisible(false);
        jPanel.add(create);

        //解决创建瓜圈后无法更新的问题 刷新
        JButton refresh = new JButton("刷新");
        refresh.setBounds(720,650,90,30);
        refresh.addActionListener(e -> {
            DefaultListModel<String> listModel1 = new DefaultListModel<>();
            List<EventGroup> eventGroups1 = iEventGroupService.viewAllEventGroup();
            for (int i = 0; i < eventGroups1.size(); i++) {
                listModel1.add(i,eventGroups1.get(i).getEventGroupName());
            }
            //向列表框中加入所有的瓜圈名
            list.setModel(listModel1);
        });
        jPanel.add(refresh);


        //直接用roleId来区分不同的身份，使不同角色看到不同的界面
        int roleId = new UserService().verifyRole(userId);

        //窗口可见
        eventGroup.setVisible(true);
        //删除瓜圈 创建瓜圈 管理员超级管理员
        if(roleId==2||roleId==4){
            delete.setVisible(true);
            create.setVisible(true);
        }
        //游客
        if(roleId==3){
            refresh.setVisible(false);
        }


    }

}
