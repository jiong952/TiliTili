package com.jiong.www.view.swing;

import com.jiong.www.view.swing.eventGroupSwing.GroupsSwing;
import com.jiong.www.view.swing.userSwing.LoginSwing;
import com.jiong.www.view.swing.userSwing.RegisterSwing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @author Mono
 */
public class WelcomeSwing extends JFrame implements ActionListener{
        JFrame welcome;
        JPanel jPanel1;
        JPanel jPanel2;
        JLabel jLabel;
        JButton login;
        JButton register;
        JButton visitor;

    public static void main(String[] args) {
        new WelcomeSwing();
    }
    public WelcomeSwing(){
        welcome = new JFrame("TiliTili瓜王系统");
        welcome.setSize(1200,800);
        //设置大小
        welcome.setLocationRelativeTo(null);

        welcome.setResizable(false);
        //不可拉伸
        welcome.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //默认关闭
        welcome.setLayout(new GridLayout(2,1));

        //面板一，放标题
        jPanel1 = new JPanel(null);
        //绝对布局
        welcome.add(jPanel1);
        jLabel = new JLabel("TiliTili瓜王系统");
        jLabel.setFont(new Font("宋体",Font.PLAIN,50));
        jLabel.setBounds(400,200,700,100);
        jPanel1.add(jLabel);

        //面板二，放按钮
        jPanel2 = new JPanel();
        //默认流式布局
        welcome.add(jPanel2);
        login = new JButton("登录");
        register = new JButton("注册");
        visitor = new JButton("游客");
        jPanel2.add(login);
        jPanel2.add(register);
        jPanel2.add(visitor);
        login.addActionListener(this);
        register.addActionListener(this);
        visitor.addActionListener(this);
        //添加监听器

        welcome.setVisible(true);
        //窗口可见

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==login){
            welcome.dispose();
            //销毁当前页面
            new LoginSwing();
            //进入登录界面
        }else if(e.getSource()==register){
            welcome.dispose();
            //销毁当前页面
            new RegisterSwing();
            //进入注册界面
        }else if(e.getSource()== visitor){
            int judge = JOptionPane.showConfirmDialog(null, "游客只能浏览瓜，您确定不登录吗？", "确认", JOptionPane.YES_NO_OPTION);
            if(judge==0){
                //YES
                JOptionPane.showMessageDialog(null,"您好！游客");
                new GroupsSwing(0,null);
            }
        }

    }
}
