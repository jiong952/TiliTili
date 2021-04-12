package com.jiong.www.view.swing;

import com.jiong.www.service.UserService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Mono
 */
public class PasswordSwing extends JFrame {
    int userId;
    String eventGroupName;

    public PasswordSwing(int userId, String eventGroupName) {
        this.userId = userId;
        this.eventGroupName=eventGroupName;
        UserService userService = new UserService();

        JFrame password = new JFrame();
        password.setSize(500,500);
        //设置大小
        password.setLocationRelativeTo(null);
        //窗口可见
        password.setResizable(false);
        //不可拉伸
        password.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        //默认关闭

        JPanel jPanel = new JPanel();
        jPanel.setLayout(null);
        password.add(jPanel);
        //绝对布局

        Font font = new Font("宋体", Font.BOLD, 35);
        JLabel jLabel = new JLabel("修改密码");
        jLabel.setFont(font);
        jLabel.setBounds(5,5,250,100);
        jPanel.add(jLabel);

        Font font1 = new Font("黑体",Font.PLAIN,14);

        //旧密码部分：标签+输入文本框+报错提示
        JLabel oldPassword = new JLabel("旧密码:");
        oldPassword.setFont(font1);
        oldPassword.setBounds(100,80,70,30);
        jPanel.add(oldPassword);
        JLabel errorPassword = new JLabel("密码错误！");
        errorPassword.setVisible(false);
        errorPassword.setForeground(Color.red);
        errorPassword.setBounds(280,80,100,20);
        jPanel.add(errorPassword);
        JPasswordField oldPasswordField = new JPasswordField(15);
        oldPasswordField.setBounds(180,85,100,20);
        oldPasswordField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                int judge = userService.verifyPassword(new String(oldPasswordField.getPassword()), userId);
                if(judge==0){
                    //密码错误
                    errorPassword.setVisible(true);
                }else {
                    errorPassword.setVisible(false);
                }

            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                int judge = userService.verifyPassword(oldPassword.getText(), userId);
                if(judge==0){
                    //密码错误
                    errorPassword.setVisible(true);
                }else {
                    errorPassword.setVisible(false);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });
        jPanel.add(oldPasswordField);

        //新密码部分：标签+输入文本框
        JLabel newPassword = new JLabel("新密码:");
        newPassword.setFont(font1);
        newPassword.setBounds(100,120,80,30);
        jPanel.add(newPassword);
        JPasswordField newPasswordField = new JPasswordField(15);
        newPasswordField.setBounds(180,125,100,20);
        jPanel.add(newPasswordField);

        //确认密码部分：标签+输入文本框+报错提示
        JLabel confirmPassword = new JLabel("确认密码:");
        confirmPassword.setFont(font1);
        confirmPassword.setBounds(100,170,80,30);
        jPanel.add(confirmPassword);
        JLabel errorPassword2 = new JLabel("两次密码不一致！");
        errorPassword2.setVisible(false);
        errorPassword2.setForeground(Color.red);
        errorPassword2.setBounds(280,170,150,30);
        jPanel.add(errorPassword2);
        JPasswordField confirmPasswordField = new JPasswordField(15);
        confirmPasswordField.setBounds(180,175,100,20);
        jPanel.add(confirmPasswordField);

        //对新密码和确认密码输入框加实时输入删除更新的监听器，便于报错
        newPasswordField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if(!"".equals(new String(newPasswordField.getPassword()))&&"".equals(new String(confirmPasswordField.getPassword()))){
                    errorPassword2.setVisible(false);
                }
                if("".equals(new String(newPasswordField.getPassword()))&&!"".equals(new String(confirmPasswordField.getPassword()))){
                    errorPassword2.setText("请先输入新密码");
                    errorPassword2.setVisible(true);
                    //提示先输入第一次密码
                }else if(!"".equals(new String(newPasswordField.getPassword()))&&!"".equals(new String(confirmPasswordField.getPassword()))){
                    errorPassword2.setVisible(false);
                    String password0 = new String(newPasswordField.getPassword());
                    String password1 = new String(confirmPasswordField.getPassword());
                    if(!password0.equals(password1)){
                        errorPassword2.setText("两次输入密码不一致");
                        errorPassword2.setVisible(true);
                        //提示确认密码
                    }else {
                        errorPassword2.setVisible(false);
                    }
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {

                if(!"".equals(new String(newPasswordField.getPassword()))&&"".equals(new String(confirmPasswordField.getPassword()))){
                    errorPassword2.setVisible(false);
                }
                if("".equals(new String(newPasswordField.getPassword()))&&!"".equals(new String(confirmPasswordField.getPassword()))){
                    errorPassword2.setText("请先输入新密码");
                    errorPassword2.setVisible(true);
                    //提示先输入第一次密码
                }else if(!"".equals(new String(newPasswordField.getPassword()))&&!"".equals(new String(confirmPasswordField.getPassword()))){
                    errorPassword2.setVisible(false);
                    String password0 = new String(newPasswordField.getPassword());
                    String password1 = new String(confirmPasswordField.getPassword());
                    if(!password0.equals(password1)){
                        errorPassword2.setText("两次输入密码不一致");
                        errorPassword2.setVisible(true);
                        //提示确认密码
                    }else {
                        errorPassword2.setVisible(false);
                    }
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });
        confirmPasswordField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {

                if("".equals(new String(newPasswordField.getPassword()))&&!"".equals(new String(confirmPasswordField.getPassword()))){
                    //用户未输入新密码直接输入第二次密码
                    errorPassword2.setText("请先输入新密码");
                    errorPassword2.setVisible(true);
                    //提示先输入第一次密码
                }else if(!"".equals(new String(newPasswordField.getPassword()))&&!"".equals(new String(confirmPasswordField.getPassword()))){
                    //用户输入了两次密码
                    errorPassword2.setVisible(false);
                    String password0 = new String(newPasswordField.getPassword());
                    String password1 = new String(confirmPasswordField.getPassword());
                    if(!password0.equals(password1)){
                        errorPassword2.setText("两次输入密码不一致");
                        errorPassword2.setVisible(true);
                        //提示确认密码
                    }else {
                        errorPassword2.setVisible(false);
                    }
                }
                else if("".equals(new String(newPasswordField.getPassword()))&&"".equals(new String(confirmPasswordField.getPassword()))){
                    //用户没有输入任何密码
                    errorPassword2.setVisible(false);
                }

            }

            @Override
            public void removeUpdate(DocumentEvent e) {


                if("".equals(new String(newPasswordField.getPassword()))&&!"".equals(new String(confirmPasswordField.getPassword()))){
                    //用户未输入新密码直接输入第二次密码
                    errorPassword2.setText("请先输入新密码");
                    errorPassword2.setVisible(true);
                    //提示先输入第一次密码
                }else if(!"".equals(new String(newPasswordField.getPassword()))&&!"".equals(new String(confirmPasswordField.getPassword()))){
                    //用户输入了两次密码
                    errorPassword2.setVisible(false);
                    String password0 = new String(newPasswordField.getPassword());
                    String password1 = new String(confirmPasswordField.getPassword());
                    if(!password0.equals(password1)){
                        errorPassword2.setText("两次输入密码不一致");
                        errorPassword2.setVisible(true);
                        //提示确认密码
                    }else {
                        errorPassword2.setVisible(false);
                    }
                }
                else if("".equals(new String(newPasswordField.getPassword()))&&"".equals(new String(confirmPasswordField.getPassword()))){
                    //用户没有输入任何密码
                    errorPassword2.setVisible(false);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });

        //确认修改的按钮，按钮加监听器。对各种错误输入进行判断并报错
        JButton confirm = new JButton("确认修改");
        confirm.setBounds(100,350,100,20);
        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if("".equals(new String(oldPasswordField.getPassword())) || "".equals(new String(newPasswordField.getPassword())) ||"".equals(new String(confirmPasswordField.getPassword()))){
                    JOptionPane.showMessageDialog(null,"请填写完所有信息！","错误",JOptionPane.ERROR_MESSAGE);
                    //让用户填写所有
                }else if(userService.verifyPassword(new String(oldPasswordField.getPassword()), userId)==0){
                    JOptionPane.showMessageDialog(null,"密码错误！","错误",JOptionPane.ERROR_MESSAGE);
                }
                    else if(!new String(newPasswordField.getPassword()).equals(new String(confirmPasswordField.getPassword()))){
                    JOptionPane.showMessageDialog(null,"两次密码输入不一致！","错误",JOptionPane.ERROR_MESSAGE);
                }
                else {
                    String userNewPassword=new String(newPasswordField.getPassword());
                    int judge = userService.changePassword(userNewPassword, userId);
                    if(judge>0){
                        JOptionPane.showMessageDialog(null,"修改成功！");
                        password.dispose();
                        new InformationSwing(userId,eventGroupName);
                    }else {
                        JOptionPane.showMessageDialog(null,"修改失败！","错误",JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        jPanel.add(confirm);

        //返回的按钮，加监听器
        JButton cancel = new JButton("返回");
        cancel.setBounds(300,350,60,20);
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                password.dispose();
                //销毁当前界面
                new InformationSwing(userId,eventGroupName);
            }
        });
        jPanel.add(cancel);

        password.setVisible(true);
        //可见

    }
}
