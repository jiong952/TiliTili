package com.jiong.www.view.swing.userSwing;

import com.jiong.www.po.User;
import com.jiong.www.service.service.IUserService;
import com.jiong.www.service.serviceImpl.UserServiceImpl;
import com.jiong.www.view.swing.WelcomeSwing;
import com.jiong.www.view.swing.eventGroupSwing.GroupsSwing;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Mono
 */
public class LoginSwing extends JFrame implements ActionListener {
    JFrame login;
    JPanel jPanel;
    JLabel username;
    JLabel password;
    JTextField usernameField;
    JPasswordField passwordField;
    JButton loginButton;
    JButton reset;
    JButton cancel;

    int userId=0;
    String eventGroupName=null;
    IUserService iUserService = new UserServiceImpl();
    int isRemeberPassword = 0;

    public LoginSwing()  {
        login = new JFrame("TiliTili瓜王系统");
        login.setSize(500,500);
        //设置大小
        login.setLocationRelativeTo(null);
        //窗口可见
        login.setResizable(false);
        //不可拉伸
        login.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        //默认关闭

        jPanel = new JPanel();
        login.add(jPanel);
        jPanel.setLayout(null);
        //绝对布局

        Font font = new Font("宋体", Font.BOLD, 50);
        JLabel jLabel = new JLabel("登录");
        jLabel.setFont(font);
        jLabel.setBounds(0,0,250,100);
        jPanel.add(jLabel);

        //用户名+输入文本框
        Font font1 = new Font("黑体",Font.PLAIN,18);
        username = new JLabel("用户名");
        username.setBounds(100,200,60,20);
        username.setFont(font1);
        usernameField = new JTextField(10);
        usernameField.setBounds(225,200,100,20);
        jPanel.add(usernameField);
        jPanel.add(username);

        //密码加输入文本框
        password = new JLabel("密码");
        password.setBounds(100,250,60,20);
        password.setFont(font1);
        jPanel.add(password);
        passwordField = new JPasswordField(10);
        passwordField.setBounds(225,250,100,20);
        jPanel.add(passwordField);

        //记住密码的复选框
        JCheckBox jcheckbox = new JCheckBox("记住密码");
        jcheckbox.setBounds(180,300,100,20);
        jPanel.add(jcheckbox);

        //用户名文本框增删监听器 判断用户名存在 存在判断上一次有没有记住密码
        usernameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                String loginName = usernameField.getText();
                User user = iUserService.isRememberPassword(loginName);
                if(user.getLoginPassword()==null){
                    //用户不存在
                    jcheckbox.setSelected(false);
                    passwordField.setText("");
                }else {
                    //用户存在,判断上次登录有没有选择记住密码
                    int isRememberPassword = user.getIsRememberPassword();
                    if(isRememberPassword==0){
                        //没有
                        jcheckbox.setSelected(false);
                        passwordField.setText("");
                    }else {
                        //有
                        jcheckbox.setSelected(true);
                        passwordField.setText(user.getLoginPassword());
                        isRemeberPassword =1;
                        //judge用来判断是否记住密码
                    }
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String loginName = usernameField.getText();
                User user = iUserService.isRememberPassword(loginName);
                if(user.getLoginPassword()==null){
                    //用户不存在
                    jcheckbox.setSelected(false);
                    passwordField.setText("");
                }else {
                    //用户存在,判断上次登录有没有选择记住密码
                    int isRememberPassword = user.getIsRememberPassword();
                    if(isRememberPassword==0){
                        //没有
                        jcheckbox.setSelected(false);
                        passwordField.setText("");
                    }else {
                        //有
                        jcheckbox.setSelected(true);
                        passwordField.setText(user.getLoginPassword());
                        isRemeberPassword =1;
                        //judge用来判断是否记住密码
                    }
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });

        //登录按钮
        loginButton = new JButton("登录");
        loginButton.setBounds(75,400,80,20);
        loginButton.addActionListener(e -> {
            String userName = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if("".equals(usernameField.getText())||"".equals(new String(passwordField.getPassword()))){
                JOptionPane.showMessageDialog(null,"请填写完所有信息！","错误",JOptionPane.ERROR_MESSAGE);
            }else {
                int judge = iUserService.isExist(userName);
                if(judge==1){
                    //用户名存在
                    userId = iUserService.login(userName, password,isRemeberPassword);
                    if(userId==0){
                        JOptionPane.showMessageDialog(null,"登录失败!","错误",JOptionPane.ERROR_MESSAGE);
                    }else {
                        JOptionPane.showMessageDialog(null,"登录成功！");
                        int roleId = iUserService.queryRole(userId);
                        User user = iUserService.queryInformation(userId);
                        switch (roleId){
                            case 1:
                                JOptionPane.showMessageDialog(null,"您好！用户"+user.getLoginName());
                                break;
                            case 2:
                                JOptionPane.showMessageDialog(null,"您好！管理员"+user.getLoginName());
                                break;
                            case 4:
                                JOptionPane.showMessageDialog(null,"您好！超级管理员"+user.getLoginName());
                                break;
                            default:
                                break;
                        }
                        //先在这里记录是否记住密码
                        if(jcheckbox.isSelected()){
                            iUserService.isRememberPassword(1,userId);
                            //更新表中的记住密码项
                        }else {
                            iUserService.isRememberPassword(0,userId);
                        }
                        //进入下一个页面
                        login.dispose();
                        new GroupsSwing(userId,eventGroupName);
                    }
                }
                else {
                    JOptionPane.showMessageDialog(null,"用户名不存在！","错误",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        reset = new JButton("重置");
        reset.setBounds(175,400,80,20);
        reset.addActionListener(this);
        cancel = new JButton("取消");
        cancel.setBounds(275,400,80,20);
        cancel.addActionListener(this);
        jPanel.add(loginButton);
        jPanel.add(reset);
        jPanel.add(cancel);

        login.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==reset){
            usernameField.setText("");
            passwordField.setText("");
            //空白重置
        }
        if(e.getSource()==cancel) {
            //取消则返回欢迎界面
            login.dispose();
            //先销毁当前
            new WelcomeSwing();
        }
    }

}
