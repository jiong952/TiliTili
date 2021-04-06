package com.jiong.www.view;

import com.jiong.www.service.TilitiliService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Register extends JFrame implements ActionListener , DocumentListener {
    JFrame register;
    JPanel jPanel;
    //2个标签
    JLabel username;
    JLabel password;
    JLabel confirmPassword;
    JLabel jLabel1;
    JLabel jLabel2;
    //2个文本框
    JTextField usernameField;
    JPasswordField passwordField;
    JPasswordField confirmPasswordField;
    //3个按钮
    JButton registerButton;
    JButton reset;
    JButton cancel;
    TilitiliService tilitiliService = new TilitiliService();

    public Register(){
        register = new JFrame("TiliTili瓜王系统");
        register.setSize(1200,800);
        //设置大小
        register.setLocationRelativeTo(null);
        //窗口可见
        register.setResizable(false);
        //不可拉伸
        register.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //默认关闭

        jPanel = new JPanel();
        register.add(jPanel);
        jPanel.setLayout(null);
        //绝对布局

        Font font = new Font("黑体",Font.PLAIN,18);
        username = new JLabel("用户名");
        username.setBounds(425,200,60,20);
        username.setFont(font);

        password = new JLabel("密码");
        password.setBounds(425,250,60,20);
        password.setFont(font);

        confirmPassword = new JLabel("确认密码");
        confirmPassword.setFont(font);
        confirmPassword.setBounds(425,300,100,20);

        jLabel1 = new JLabel("用户名已存在！");
        jLabel1.setBounds(700,200,100,20);
        jLabel1.setVisible(false);
        jLabel1.setForeground(Color.red);

        jLabel2 =new JLabel();
        jLabel2.setBounds(650,300,150,20);
        jLabel2.setVisible(false);
        jLabel2.setForeground(Color.red);

        jPanel.add(username);
        jPanel.add(password);
        jPanel.add(confirmPassword);
        jPanel.add(jLabel1);
        jPanel.add(jLabel2);
        usernameField = new JTextField(10);
        usernameField.setBounds(550,200,100,20);
        usernameField.getDocument().addDocumentListener(this);
        passwordField = new JPasswordField(10);
        passwordField.setBounds(550,250,100,20);
        passwordField.getDocument().addDocumentListener(this);
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(550,300,100,20);
        confirmPasswordField.getDocument().addDocumentListener(this);
        jPanel.add(usernameField);
        jPanel.add(passwordField);
        jPanel.add(confirmPasswordField);


        registerButton = new JButton("注册");
        registerButton.setBounds(400,400,80,20);
        registerButton.addActionListener(this);
        reset = new JButton("重置");
        reset.setBounds(500,400,80,20);
        reset.addActionListener(this);
        cancel = new JButton("取消");
        cancel.setBounds(600,400,80,20);
        cancel.addActionListener(this);
        jPanel.add(registerButton);
        jPanel.add(reset);
        jPanel.add(cancel);

        register.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==registerButton){
            if("".equals(usernameField.getText()) || "".equals(new String(passwordField.getPassword())) ||"".equals(new String(confirmPasswordField.getPassword()))){
                JOptionPane.showMessageDialog(null,"请填写完所有信息！","错误",JOptionPane.ERROR_MESSAGE);
                //让用户填写所有
            }else if(tilitiliService.verifyUsername(usernameField.getText())==1){
                JOptionPane.showMessageDialog(null,"用户名已存在！","错误",JOptionPane.ERROR_MESSAGE);
            }else if(!new String(passwordField.getPassword()).equals(new String(confirmPasswordField.getPassword()))){
                JOptionPane.showMessageDialog(null,"两次密码输入不一致！","错误",JOptionPane.ERROR_MESSAGE);
            }
            else {
                String newName = usernameField.getText();
                String newPassword = new String(confirmPasswordField.getPassword());
                int judge = tilitiliService.register(newName, newPassword);
                if(judge>0){
                    JOptionPane.showMessageDialog(null,"注册成功！");
                    register.dispose();
                    new Welcome();
                }else {
                    JOptionPane.showMessageDialog(null,"注册失败！","错误",JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        if(e.getSource()==reset){
            usernameField.setText("");
            passwordField.setText("");
            confirmPasswordField.setText("");
            //空白重置
        }
        if(e.getSource()==cancel) {
            //取消则返回欢迎界面
            register.dispose();
            //先销毁当前
            new Welcome();
        }

    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        if(e.getDocument()==usernameField.getDocument()){
            String userName = usernameField.getText();
            int judge = tilitiliService.verifyUsername(userName);
            //提示用户用户名存在
            jLabel1.setVisible(judge == 1&&!"".equals(usernameField.getText()));
        }
        if(e.getDocument()==confirmPasswordField.getDocument()){
            if("".equals(new String(passwordField.getPassword()))&&!"".equals(new String(confirmPasswordField.getPassword()))){
                //用户未输入第一次直接输入第二次密码
                jLabel2.setText("请先输入初始密码");
                jLabel2.setVisible(true);
                //提示先输入第一次密码
            }else if(!"".equals(new String(passwordField.getPassword()))&&!"".equals(new String(confirmPasswordField.getPassword()))){
                //用户输入了两次密码
                jLabel2.setVisible(false);
                String password0 = new String(passwordField.getPassword());
                String password1 = new String(confirmPasswordField.getPassword());
                if(!password0.equals(password1)){
                    jLabel2.setText("两次输入密码不一致");
                    jLabel2.setVisible(true);
                    //提示确认密码
                }else {
                    jLabel2.setVisible(false);
                }
            }
            else if("".equals(new String(passwordField.getPassword()))&&"".equals(new String(confirmPasswordField.getPassword()))){
                //用户没有输入任何密码
                jLabel2.setVisible(false);
            }


        }
        if(e.getDocument()==passwordField.getDocument()){
            if(!"".equals(new String(passwordField.getPassword()))&&"".equals(new String(confirmPasswordField.getPassword()))){
                jLabel2.setVisible(false);
            }
            if("".equals(new String(passwordField.getPassword()))&&!"".equals(new String(confirmPasswordField.getPassword()))){
                jLabel2.setText("请先输入初始密码");
                jLabel2.setVisible(true);
                //提示先输入第一次密码
            }else if(!"".equals(new String(passwordField.getPassword()))&&!"".equals(new String(confirmPasswordField.getPassword()))){
                jLabel2.setVisible(false);
                String password0 = new String(passwordField.getPassword());
                String password1 = new String(confirmPasswordField.getPassword());
                if(!password0.equals(password1)){
                    jLabel2.setText("两次输入密码不一致");
                    jLabel2.setVisible(true);
                    //提示确认密码
                }else {
                    jLabel2.setVisible(false);
                }
            }
        }
    }

    @Override
    public void removeUpdate(DocumentEvent e) {

        if(e.getDocument()==usernameField.getDocument()){
            String userName = usernameField.getText();
            int judge = tilitiliService.verifyUsername(userName);
            //提示用户用户名存在
            jLabel1.setVisible(judge == 1&&!"".equals(usernameField.getText()));
        }
        if(e.getDocument()==confirmPasswordField.getDocument()){
            if("".equals(new String(passwordField.getPassword()))&&!"".equals(new String(confirmPasswordField.getPassword()))){
                jLabel2.setText("请先输入初始密码");
                jLabel2.setVisible(true);
                //提示先输入第一次密码
            }else if(!"".equals(new String(passwordField.getPassword()))&&!"".equals(new String(confirmPasswordField.getPassword()))){
                jLabel2.setVisible(false);
                String password0 = new String(passwordField.getPassword());
                String password1 = new String(confirmPasswordField.getPassword());
                if(!password0.equals(password1)){
                    jLabel2.setText("两次输入密码不一致");
                    jLabel2.setVisible(true);
                    //提示确认密码
                }else {
                    jLabel2.setVisible(false);
                }
            }
            else if("".equals(new String(passwordField.getPassword()))&&"".equals(new String(confirmPasswordField.getPassword()))){
                jLabel2.setVisible(false);
            }


        }
        if(e.getDocument()==passwordField.getDocument()){
            if(!"".equals(new String(passwordField.getPassword()))&&"".equals(new String(confirmPasswordField.getPassword()))){
                jLabel2.setVisible(false);
            }
            if("".equals(new String(passwordField.getPassword()))&&!"".equals(new String(confirmPasswordField.getPassword()))){
                jLabel2.setText("请先输入初始密码");
                jLabel2.setVisible(true);
                //提示先输入第一次密码
            }else if(!"".equals(new String(passwordField.getPassword()))&&!"".equals(new String(confirmPasswordField.getPassword()))){
                jLabel2.setVisible(false);
                String password0 = new String(passwordField.getPassword());
                String password1 = new String(confirmPasswordField.getPassword());
                if(!password0.equals(password1)){
                    jLabel2.setText("两次输入密码不一致");
                    jLabel2.setVisible(true);
                    //提示确认密码
                }else {
                    jLabel2.setVisible(false);
                }
            }
        }
    }

    @Override
    public void changedUpdate(DocumentEvent e) {

    }

}
