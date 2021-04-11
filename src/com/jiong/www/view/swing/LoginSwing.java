package com.jiong.www.view.swing;

import com.jiong.www.service.UserService;

import javax.swing.*;
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
    public static void main(String[] args) {
        new LoginSwing();
    }
    public LoginSwing()  {
        login = new JFrame("TiliTili瓜王系统");
        login.setSize(1200,800);
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

        Font font = new Font("黑体",Font.PLAIN,18);
        username = new JLabel("用户名");
        username.setBounds(425,200,60,20);
        username.setFont(font);
        password = new JLabel("密码");
        password.setBounds(425,250,60,20);
        password.setFont(font);
        jPanel.add(username);
        jPanel.add(password);

        usernameField = new JTextField(10);
        usernameField.setBounds(550,200,100,20);
        passwordField = new JPasswordField(10);
        passwordField.setBounds(550,250,100,20);
        jPanel.add(usernameField);
        jPanel.add(passwordField);


        loginButton = new JButton("登录");
        loginButton.setBounds(400,400,80,20);
        loginButton.addActionListener(this);
        reset = new JButton("重置");
        reset.setBounds(500,400,80,20);
        reset.addActionListener(this);
        cancel = new JButton("取消");
        cancel.setBounds(600,400,80,20);
        cancel.addActionListener(this);
        jPanel.add(loginButton);
        jPanel.add(reset);
        jPanel.add(cancel);

        login.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==loginButton){
            String userName = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if("".equals(usernameField.getText())||"".equals(new String(passwordField.getPassword()))){
                JOptionPane.showMessageDialog(null,"请填写完所有信息！","错误",JOptionPane.ERROR_MESSAGE);
            }else {
            userId = userService.login(userName, password);
            if(userId==0){
                JOptionPane.showMessageDialog(null,"登录失败!","错误",JOptionPane.ERROR_MESSAGE);
            }else {
                JOptionPane.showMessageDialog(null,"登录成功！");
                //进入下一个页面
                login.dispose();
                new GroupsSwing(userId,eventGroupName);
            }
            }
        }
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
