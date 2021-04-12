package com.jiong.www.view.swing;

import com.jiong.www.po.User;
import com.jiong.www.service.UserService;
import com.jiong.www.util.Md5Utils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginSwing extends JFrame implements ActionListener {
    JFrame login;
    JPanel jPanel;
    //2个标签
    JLabel username;
    JLabel password;
    //2个文本框
    JTextField usernameField;
    JPasswordField passwordField;
    //3个按钮
    JButton loginButton;
    JButton reset;
    JButton cancel;
    int userId=0;
    String eventGroupName=null;
    UserService userService = new UserService();
    int judge = 0;
    public static void main(String[] args) {
        new LoginSwing();
    }
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

        Font font1 = new Font("黑体",Font.PLAIN,18);
        username = new JLabel("用户名");
        username.setBounds(100,200,60,20);
        username.setFont(font1);
        password = new JLabel("密码");
        password.setBounds(100,250,60,20);
        password.setFont(font1);
        jPanel.add(username);
        jPanel.add(password);

        usernameField = new JTextField(10);
        usernameField.setBounds(225,200,100,20);

        passwordField = new JPasswordField(10);
        passwordField.setBounds(225,250,100,20);
        jPanel.add(usernameField);
        jPanel.add(passwordField);


        JCheckBox jcheckbox = new JCheckBox("记住密码");
        jcheckbox.setBounds(180,300,100,20);
        jPanel.add(jcheckbox);
        usernameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                String loginName = usernameField.getText();
                User user = new UserService().isRememberPassword(loginName);
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
                        judge=1;
                    }
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {

                String loginName = usernameField.getText();
                User user = new UserService().isRememberPassword(loginName);
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
                        judge=1;
                    }
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });

        loginButton = new JButton("登录");
        loginButton.setBounds(75,400,80,20);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String userName = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if("".equals(usernameField.getText())||"".equals(new String(passwordField.getPassword()))){
                    JOptionPane.showMessageDialog(null,"请填写完所有信息！","错误",JOptionPane.ERROR_MESSAGE);
                }else {
                    String securePassword=null;
                    if(judge==0){
                        //没有选择记住密码
                         securePassword = new Md5Utils().toMD5(password);
                    }else {
                        securePassword = password;
                    }
                    userId = userService.login(userName, securePassword);
                    if(userId==0){
                        JOptionPane.showMessageDialog(null,"登录失败!","错误",JOptionPane.ERROR_MESSAGE);
                    }else {
                        JOptionPane.showMessageDialog(null,"登录成功！");
                        //先在这里判断是否记住密码
                        if(jcheckbox.isSelected()){
                            new UserService().perfectInformation(null,null,2,null,userId,null,1);
                            //更新表中的记住密码
                        }else {
                            new UserService().perfectInformation(null,null,2,null,userId,null,0);
                        }
                        //进入下一个页面
                        login.dispose();
                        new GroupsSwing(userId,eventGroupName);
                    }
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
