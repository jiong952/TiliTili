package com.jiong.www.view.swing;

import com.jiong.www.po.EventGroup;
import com.jiong.www.po.User;
import com.jiong.www.service.EventGroupService;
import com.jiong.www.service.UserService;
import com.jiong.www.util.MenuSwingUtils;
import com.jiong.www.util.GroupsPagingUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class GroupsSwing extends JFrame {
    int userId;
    String eventGroupName;
    //判断是否记住密码
    public static void main(String[] args) {
        new GroupsSwing(2,"范冰冰");
    }
    public GroupsSwing(int userId, String eventGroupName) {
        this.userId = userId;
        this.eventGroupName = eventGroupName;

        EventGroupService eventGroupService = new EventGroupService();
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

        new MenuSwingUtils(userId,eventGroup,eventGroupName);

        Font font1 = new Font("黑体",Font.PLAIN,36);

        JList<String> list = new JList<>();
        list.setFont(font1);
        list.setFixedCellHeight(56);
        list.setSelectionBackground(Color.gray);
        //单元格的大小
        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                //单击是选择(单击会有tips提示内容简介) 双击是进入
                if(!list.getValueIsAdjusting()){
                    list.setToolTipText("内容简介:"+eventGroupService.viewEventGroup(list.getSelectedValue()).getEventGroupDescription());
                }
            }
        });
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(e.getClickCount()==2){
                    eventGroup.dispose();
                    new GroupSwing(userId,list.getSelectedValue());
                }
            }
        });
        DefaultListModel<String> listModel = new DefaultListModel<String>();
        List<EventGroup> eventGroups = eventGroupService.viewAllEventGroup();
        int pageSize = 9;
        //每一页页面的展示瓜圈数目
        if(eventGroups.size()>=pageSize){
            for (int i = 0; i < pageSize; i++) {
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

        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setBounds(10,100,1150,400);
        jScrollPane.setViewportView(list);
        jPanel.add(jScrollPane);

        new GroupsPagingUtils(eventGroups,listModel,jPanel,pageSize);

        //查询瓜圈的标签+文本框
        Font font2 = new Font("黑体",Font.PLAIN,25);
        JLabel query = new JLabel("查询瓜圈");
        query.setFont(font2);
        query.setForeground(Color.BLACK);
        query.setBounds(5,650,120,30);
        jPanel.add(query);
        //输入瓜圈名字的提示
        JLabel tip = new JLabel("请输入要查询瓜圈的名字");
        tip.setForeground(Color.red);
        tip.setVisible(false);
        tip.setBounds(120,670,150,30);
        jPanel.add(tip);
        //文本框
        JTextField queryField = new JTextField(30);
        queryField.setBounds(120,650,120,30);
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
        queryButton.setBounds(245,650,60,30);
        queryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String eventGroupName = queryField.getText();
                if("".equals(eventGroupName)){
                    JOptionPane.showMessageDialog(null,"查询不能为空！","错误",JOptionPane.ERROR_MESSAGE);
                }else {
                int judge = eventGroupService.verifyEventGroupName(eventGroupName);
                if(judge==1){
                new GroupSwing(userId,eventGroupName);
                }else {
                    JOptionPane.showMessageDialog(null,"查无此瓜圈！","错误",JOptionPane.ERROR_MESSAGE);
                }
                }
            }
        });
        jPanel.add(queryButton);

        //管理员或者是超级管理员
        JButton delete = new JButton("删除瓜圈");
        delete.setBounds(350,650,90,30);
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(list.getSelectedIndex()>0){
                    int judge0 = eventGroupService.verifyEventGroupOfAdmin(userId, list.getSelectedValue());
                    //判断是不是该管理员管理的瓜圈
                    if(judge0==1){
                        //是
                        int judge = JOptionPane.showConfirmDialog(null, "您确定要删除" + list.getSelectedValue() + "瓜圈吗？", "确认", JOptionPane.YES_NO_OPTION);
                        if(judge==0){
                            //选择是
                            judge0 = eventGroupService.deleteEventGroup(list.getSelectedValue(), userId);
                            if(judge0==1){
                                JOptionPane.showMessageDialog(null,"删除瓜圈成功！");
                                //刷新
                                DefaultListModel<String> listModel1 = new DefaultListModel<String>();
                                List<EventGroup> eventGroups1 = eventGroupService.viewAllEventGroup();
                                for (int i = 0; i < eventGroups1.size(); i++) {
                                    listModel1.add(i,eventGroups1.get(i).getEventGroupName());
                                }
                                //向列表框中加入所有的瓜圈名
                                list.setModel(listModel1);
                            }
                        }
                    }else {
                        //不是
                        JOptionPane.showMessageDialog(null,"这不是您管理的瓜圈","错误",JOptionPane.ERROR_MESSAGE);
                    }
                }else {
                    JOptionPane.showMessageDialog(null,"请单击选择要删除的瓜圈","错误",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        delete.setVisible(false);
        jPanel.add(delete);

        JButton create = new JButton("创建瓜圈");
        create.setBounds(550,650,90,30);
        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //创建瓜圈
                new CreateGroupSwing(userId,eventGroupName);
            }
        });
        create.setVisible(false);
        jPanel.add(create);
        //刷新
        JButton refresh = new JButton("刷新");
        refresh.setBounds(720,650,90,30);
        refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultListModel<String> listModel1 = new DefaultListModel<String>();
                List<EventGroup> eventGroups1 = eventGroupService.viewAllEventGroup();
                for (int i = 0; i < eventGroups1.size(); i++) {
                    listModel1.add(i,eventGroups1.get(i).getEventGroupName());
                }
                //向列表框中加入所有的瓜圈名
                list.setModel(listModel1);
            }
        });
        jPanel.add(refresh);
        //直接用roleId来区分不同的身份，使不同角色看到不同的界面
        int roleId = new UserService().verifyRole(userId);

        //窗口可见
        eventGroup.setVisible(true);
        //删除瓜圈 创建瓜圈
        if(roleId==2||roleId==4){
            delete.setVisible(true);
            create.setVisible(true);
        }
        if(roleId==3){
            refresh.setVisible(false);
        }



    }

}
