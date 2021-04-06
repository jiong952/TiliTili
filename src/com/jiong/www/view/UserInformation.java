package com.jiong.www.view;

import com.jiong.www.po.User;
import com.jiong.www.service.TilitiliService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

public class UserInformation extends JFrame {

    int userId;
    //返回
    public static void main(String[] args) {
        new UserInformation(2);
    }
    public UserInformation(int userId){
        this.userId = userId;

        //获得查询结果
        TilitiliService tilitiliService = new TilitiliService();
        User user = tilitiliService.queryUserInformation(userId);

        JFrame userInformation = new JFrame("TiliTili瓜王系统");
        userInformation.setSize(1200,800);
        //设置大小
        userInformation.setLocationRelativeTo(null);
        //窗口可见
        userInformation.setResizable(false);
        //不可拉伸
        userInformation.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //默认关闭

        JPanel jPanel =new JPanel();
        userInformation.add(jPanel);
        jPanel.setLayout(null);
        //绝对布局

        Font font = new Font("宋体", Font.BOLD, 50);
        JLabel jLabel = new JLabel("个人信息");
        jLabel.setFont(font);
        jLabel.setBounds(0,0,250,100);
        jPanel.add(jLabel);

        Font font1 = new Font("黑体",Font.PLAIN,18);

        //显示用户名标签+名字,用户名不可更改
        JLabel userNameLabel = new JLabel("用户名:");
        userNameLabel.setBounds(425,150,100,20);
        userNameLabel.setFont(font1);
        jPanel.add(userNameLabel);
        JLabel userName = new JLabel(user.getLoginName());
        userName.setBounds(500,150,60,20);
        userName.setFont(font1);
        jPanel.add(userName);

        //用于显示昵称标签+文本框
        JLabel nickNameLabel = new JLabel("昵称:");
        nickNameLabel.setBounds(425,200,60,20);
        nickNameLabel.setFont(font1);
        jPanel.add(nickNameLabel);
        JTextField nickNameTextField = new JTextField(10);
        nickNameTextField.setBounds(500,200,100,20);
        nickNameTextField.setText(user.getUserNickname());
        jPanel.add(nickNameTextField);

        //用于显示性别标签+单选框
        JLabel gender = new JLabel("性别:");
        gender.setBounds(425,250,60,20);
        gender.setFont(font1);
        jPanel.add(gender);
        JRadioButton girl = new JRadioButton("女");
        girl.setBounds(500,245,80,30);
        JRadioButton boy = new JRadioButton("男");
        boy.setBounds(580,245,80,30);
        int userGender = user.getUserGender();
        if(userGender==0){
            girl.setSelected(true);
        }
        if(userGender==1) {
            boy.setSelected(true);
        }
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(girl);
        buttonGroup.add(boy);
        jPanel.add(girl);
        jPanel.add(boy);

        //用于显示邮箱标签+文本框
        // 邮箱要加一个监听器，监听后缀的格式
        JLabel email = new JLabel("邮箱:");
        email.setBounds(425,300,60,20);
        email.setFont(font1);
        jPanel.add(email);
        JTextField emailTextField = new JTextField(15);
        emailTextField.setBounds(500,300,100,20);
        emailTextField.setText(user.getUserEmail());
        jPanel.add(emailTextField);

        //生日+下拉列表+年月日标签
        JLabel birth = new JLabel("生日:");
        birth.setBounds(425,350,60,20);
        birth.setFont(font1);
        jPanel.add(birth);
        //用集合先存放string,转为数组，数组直接放进列表框
        //产生年份
        ArrayList<String> year1 = new ArrayList<String>();
        year1.add("---请选择---");
        for (int i = 2021;i>=1950;i--){
            year1.add(String.valueOf(i));
        }
        String[] year = year1.toArray(new String[year1.size()]);
        //产生月份
        ArrayList<String> month1 = new ArrayList();
        month1.add("---请选择---");
        for (int i = 0;i<12;i++){
            month1.add(String.valueOf(i+1));
        }
        String[] month = month1.toArray(new String[month1.size()]);
        //产生日期
        ArrayList<String> day1 = new ArrayList<>();
        day1.add("---请选择---");
        for (int i = 1; i< 30;i++){
            day1.add(String.valueOf(i));
        }
        String[] day = day1.toArray(new String[month1.size()]);
        //把年份数组放进下拉列表中
        JComboBox<String> birthyear = new JComboBox<>(year);
        birthyear.setBounds(500,350,100,20);
        JComboBox<String> birthmonth = new JComboBox<>(month);
        birthmonth.setBounds(640,350,100,20);
        JComboBox<String> birthday = new JComboBox<>(day);
        birthday.setBounds(780,350,100,20);
        jPanel.add(birthyear);
        jPanel.add(birthmonth);
        jPanel.add(birthday);
        //年月日的标签提示
        JLabel yearLabel = new JLabel("年");
        yearLabel.setBounds(610,345,30,30);
        yearLabel.setFont(font1);
        jPanel.add(yearLabel);
        JLabel monthLabel = new JLabel("月");
        monthLabel.setBounds(750,345,30,30);
        monthLabel.setFont(font1);
        jPanel.add(monthLabel);
        JLabel dayLabel = new JLabel("日");
        dayLabel.setBounds(890,345,30,30);
        dayLabel.setFont(font1);
        jPanel.add(dayLabel);

        Date userBirthday = user.getUserBirthday();
        //从数据库里获取生日
        Calendar calendar = Calendar.getInstance();
        if(userBirthday!=null){
            calendar.setTime(userBirthday);
            //获取年月日，同时选中
            String userYear = String.valueOf(calendar.get(Calendar.YEAR));
            birthyear.setSelectedItem(userYear);
            String userMonth = String.valueOf(calendar.get(Calendar.MONTH)+1);
            //calendar默认从0月开始
            birthmonth.setSelectedItem(userMonth);
            String userDay = String.valueOf(calendar.get(Calendar.DATE));
            birthday.setSelectedItem(userDay);
        }else {
            birthyear.setSelectedItem("---请选择---");
            birthmonth.setSelectedItem("---请选择---");
            birthday.setSelectedItem("---请选择---");
        }


        //个人简介提示+文本域
        JLabel description = new JLabel("个人简介：");
        description.setBounds(390,400,100,20);
        description.setFont(font1);
        jPanel.add(description);
        //文本域
        JTextArea descriptionTextArea = new JTextArea(20,10);
        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setBounds(500,400,500,220);
        descriptionTextArea.setText(user.getUserDescription());
        jPanel.add(descriptionTextArea);

        //修改密码的按钮，加监听器，跳转界面
        JButton changePassword = new JButton("修改密码");
        changePassword.setBounds(440,700,90,20);
        changePassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //修改密码界面
                userInformation.dispose();
                new ChangePassword(userId);
            }
        });
        jPanel.add(changePassword);

        //保存按钮,加监听器，获取上方的信息，判断修改个人信息是否完成
        JButton save = new JButton("保存");
        save.setBounds(550,700,80,20);
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //昵称
                String userNickname = nickNameTextField.getText();
                //性别
                int userGender =2;
                if(girl.isSelected()){
                    userGender=0;
                    //女
                } else if(boy.isSelected()){
                    userGender=1;
                    //男
                }
                //邮箱
                String userEmail = emailTextField.getText();
                //生日
                if(!("---请选择---").equals(birthyear.getSelectedItem())&&!("---请选择---").equals(birthmonth.getSelectedItem())&&!("---请选择---").equals(birthday.getSelectedItem())){
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(birthyear.getSelectedItem());
                    //年
                    stringBuilder.append("-");
                    stringBuilder.append(birthmonth.getSelectedItem());
                    //月
                    stringBuilder.append("-");
                    stringBuilder.append(birthday.getSelectedItem());
                    //日
                    String userBirthday1 = stringBuilder.toString();
                    java.sql.Date userBirthday = java.sql.Date.valueOf(userBirthday1);
                }

                //个人简介
                String userDescription = descriptionTextArea.getText();

                if(("---请选择---").equals(birthyear.getSelectedItem())||("---请选择---").equals(birthmonth.getSelectedItem())||("---请选择---").equals(birthday.getSelectedItem())){
                    JOptionPane.showMessageDialog(null,"请填写正确的生日信息","错误",JOptionPane.ERROR_MESSAGE);
                }else {
                    int judge = tilitiliService.perfectInformation(userEmail, userNickname, userGender, userDescription, 2, userBirthday);
                    if (judge == 1) {
                        JOptionPane.showMessageDialog(null, "保存成功！");
                    } else {
                        JOptionPane.showMessageDialog(null, "保存失败!", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        jPanel.add(save);

        //重置按钮：恢复未保存前的原状
        JButton reset = new JButton("重置");
        reset.setBounds(650,700,80,20);
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //昵称
                nickNameTextField.setText(user.getUserNickname());
                //性别
                int userGender = user.getUserGender();
                if(userGender==0){
                    girl.setSelected(true);
                }else {
                    boy.setSelected(true);
                }
                //邮箱
                emailTextField.setText(user.getUserEmail());
                //生日
                Date userBirthday = user.getUserBirthday();
                //从数据库里获取生日
                Calendar calendar = Calendar.getInstance();
                if(userBirthday!=null){
                    calendar.setTime(userBirthday);
                    //获取年月日，同时选中
                    String userYear = String.valueOf(calendar.get(Calendar.YEAR));
                    birthyear.setSelectedItem(userYear);
                    String userMonth = String.valueOf(calendar.get(Calendar.MONTH)+1);
                    //calendar默认从0月开始
                    birthmonth.setSelectedItem(userMonth);
                    String userDay = String.valueOf(calendar.get(Calendar.DATE));
                    birthday.setSelectedItem(userDay);
                }else {
                    birthyear.setSelectedItem("---请选择---");
                    birthmonth.setSelectedItem("---请选择---");
                    birthday.setSelectedItem("---请选择---");
                }
                //个人简介
                descriptionTextArea.setText(user.getUserDescription());
            }
        });
        jPanel.add(reset);

        //返回按钮
        JButton cancel = new JButton("返回");
        cancel.setBounds(750,700,80,20);
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userInformation.dispose();
                //退出当前界面
                new Option(userId);
            }
        });
        jPanel.add(cancel);

        userInformation.setVisible(true);
    }


}
