package com.jiong.www.view.swing;

import com.jiong.www.service.UserService;
import com.jiong.www.util.Md5Utils;
import com.jiong.www.util.StringUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterSwing extends JFrame implements ActionListener , DocumentListener {
    JFrame register;
    JPanel jPanel;
    //2个标签
    JLabel username;
    JLabel password;
    JLabel confirmPassword;
    JLabel jLabel1;
    JLabel jLabel2;
    JLabel jLabel3;
    //2个文本框
    JTextField usernameField;
    JPasswordField passwordField;
    JPasswordField confirmPasswordField;
    //3个按钮
    JButton registerButton;
    JButton reset;
    JButton cancel;
    UserService userService = new UserService();

    public static void main(String[] args) {
        new RegisterSwing();
    }
    public RegisterSwing(){
        register = new JFrame("TiliTili瓜王系统");
        register.setSize(500,500);
        //设置大小
        register.setLocationRelativeTo(null);
        //窗口可见
        register.setResizable(false);
        //不可拉伸
        register.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        //默认关闭

        jPanel = new JPanel();
        register.add(jPanel);
        jPanel.setLayout(null);
        //绝对布局

        Font font = new Font("宋体", Font.BOLD, 50);
        JLabel jLabel = new JLabel("修改密码");
        jLabel.setFont(font);
        jLabel.setBounds(0,0,250,100);
        jPanel.add(jLabel);

        Font font1 = new Font("黑体",Font.PLAIN,18);
        username = new JLabel("用户名");
        username.setBounds(125,150,60,20);
        username.setFont(font1);

        password = new JLabel("密码");
        password.setBounds(125,200,60,20);
        password.setFont(font1);

        confirmPassword = new JLabel("确认密码");
        confirmPassword.setFont(font1);
        confirmPassword.setBounds(125,250,100,20);

        jLabel1 = new JLabel("用户名已存在！");
        jLabel1.setBounds(400,150,100,20);
        jLabel1.setVisible(false);
        jLabel1.setForeground(Color.red);

        jLabel2 =new JLabel();
        jLabel2.setBounds(350,250,150,20);
        jLabel2.setVisible(false);
        jLabel2.setForeground(Color.red);


        //密码格式的提示
        jLabel3 = new JLabel("密码8位以上,一定有且只有字母数字");
        jLabel3.setBounds(125,280,250,20);
        jLabel3.setVisible(false);
        jLabel3.setForeground(Color.red);
        jPanel.add(jLabel3);

        jPanel.add(username);
        jPanel.add(password);
        jPanel.add(confirmPassword);
        jPanel.add(jLabel1);
        jPanel.add(jLabel2);
        usernameField = new JTextField(10);
        usernameField.setBounds(250,150,100,20);
        usernameField.getDocument().addDocumentListener(this);
        passwordField = new JPasswordField(10);
        passwordField.setBounds(250,200,100,20);
        passwordField.getDocument().addDocumentListener(this);
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(250,250,100,20);
        confirmPasswordField.getDocument().addDocumentListener(this);
        jPanel.add(usernameField);
        jPanel.add(passwordField);
        jPanel.add(confirmPasswordField);

        registerButton = new JButton("注册");
        registerButton.setBounds(100,350,80,20);
        registerButton.addActionListener(this);
        reset = new JButton("重置");
        reset.setBounds(200,350,80,20);
        reset.addActionListener(this);
        cancel = new JButton("取消");
        cancel.setBounds(300,350,80,20);
        cancel.addActionListener(this);
        jPanel.add(registerButton);
        jPanel.add(reset);
        jPanel.add(cancel);

        register.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==registerButton){
            String password = new String(passwordField.getPassword());
            String comfirmPassword = new String(confirmPasswordField.getPassword());
            if("".equals(usernameField.getText()) || "".equals(password) ||"".equals(comfirmPassword)){
                JOptionPane.showMessageDialog(null,"请填写完所有信息！","错误",JOptionPane.ERROR_MESSAGE);
                //让用户填写所有
            }else if(userService.verifyUsername(usernameField.getText())==1){
                JOptionPane.showMessageDialog(null,"用户名已存在！","错误",JOptionPane.ERROR_MESSAGE);
            }else if(!password.equals(comfirmPassword)){
                JOptionPane.showMessageDialog(null,"两次密码输入不一致！","错误",JOptionPane.ERROR_MESSAGE);
            }
            else {
                String newName = usernameField.getText();
                String securePassword = new Md5Utils().toMD5(comfirmPassword);
                int judge = userService.register(newName, securePassword);
                if(judge>0){
                    JOptionPane.showMessageDialog(null,"注册成功！");
                    register.dispose();
                    new WelcomeSwing();
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
            new WelcomeSwing();
        }

    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        if(e.getDocument()==usernameField.getDocument()){
            String userName = usernameField.getText();
            int judge = userService.verifyUsername(userName);
            //提示用户用户名存在
            jLabel1.setVisible(judge == 1&&!"".equals(usernameField.getText()));
        }
        if(e.getDocument()==confirmPasswordField.getDocument()){
            String password = new String(passwordField.getPassword());
            String comfirmPassword = new String(confirmPasswordField.getPassword());
            if("".equals(password)&&!"".equals(comfirmPassword)){
                //用户未输入第一次直接输入第二次密码
                jLabel2.setText("请先输入初始密码");
                jLabel2.setVisible(true);
                //提示先输入第一次密码
            }else if(!"".equals(password)&&!"".equals(comfirmPassword)){
                //用户输入了两次密码
                jLabel2.setVisible(false);
                if(!password.equals(comfirmPassword)){
                    jLabel2.setText("两次输入密码不一致");
                    jLabel2.setVisible(true);
                    //提示确认密码
                }else {
                    jLabel2.setVisible(false);
                }
            }
            else if("".equals(password)&&"".equals(comfirmPassword)){
                //用户没有输入任何密码
                jLabel2.setVisible(false);
            }


        }
        if(e.getDocument()==passwordField.getDocument()){
            String password = new String(passwordField.getPassword());
            String comfirmPassword = new String(confirmPasswordField.getPassword());
            if(!"".equals(password)){
                boolean judge = new StringUtils().isPassword(password);
                jLabel3.setVisible(!judge);
            }
            if(!"".equals(password)&&"".equals(comfirmPassword)){
                jLabel2.setVisible(false);
            }
            if("".equals(password)&&!"".equals(comfirmPassword)){
                jLabel2.setText("请先输入初始密码");
                jLabel2.setVisible(true);
                //提示先输入第一次密码
            }else if(!"".equals(password)&&!"".equals(comfirmPassword)){
                jLabel2.setVisible(false);
                if(!password.equals(comfirmPassword)){
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
            int judge = userService.verifyUsername(userName);
            //提示用户用户名存在
            jLabel1.setVisible(judge == 1&&!"".equals(usernameField.getText()));
        }
        if(e.getDocument()==confirmPasswordField.getDocument()){
            String password = new String(passwordField.getPassword());
            String comfirmPassword = new String(confirmPasswordField.getPassword());
            if("".equals(password)&&!"".equals(comfirmPassword)){
                //用户未输入第一次直接输入第二次密码
                jLabel2.setText("请先输入初始密码");
                jLabel2.setVisible(true);
                //提示先输入第一次密码
            }else if(!"".equals(password)&&!"".equals(comfirmPassword)){
                //用户输入了两次密码
                jLabel2.setVisible(false);
                if(!password.equals(comfirmPassword)){
                    jLabel2.setText("两次输入密码不一致");
                    jLabel2.setVisible(true);
                    //提示确认密码
                }else {
                    jLabel2.setVisible(false);
                }
            }
            else if("".equals(password)&&"".equals(comfirmPassword)){
                //用户没有输入任何密码
                jLabel2.setVisible(false);
            }


        }
        if(e.getDocument()==passwordField.getDocument()){
            String password = new String(passwordField.getPassword());
            String comfirmPassword = new String(confirmPasswordField.getPassword());
            if(!"".equals(password)){
                boolean judge = new StringUtils().isPassword(password);
                jLabel3.setVisible(!judge);
            }
            if(!"".equals(password)&&"".equals(comfirmPassword)){
                jLabel2.setVisible(false);
            }
            if("".equals(password)&&!"".equals(comfirmPassword)){
                jLabel2.setText("请先输入初始密码");
                jLabel2.setVisible(true);
                //提示先输入第一次密码
            }else if(!"".equals(password)&&!"".equals(comfirmPassword)){
                jLabel2.setVisible(false);
                if(!password.equals(comfirmPassword)){
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
