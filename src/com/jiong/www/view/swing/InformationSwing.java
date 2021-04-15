package com.jiong.www.view.swing;

import com.jiong.www.po.User;
import com.jiong.www.service.UserService;
import com.jiong.www.util.ImageUtils;
import com.jiong.www.util.StringUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

public class InformationSwing extends JFrame {

    int userId;
    String eventGroupName;
    static final int YEAR_MAX =2021;
    static final int YEAR_MIN =1950;
    static final int MONTH_MAX =12;
    static final int MONTH_MIN =0;
    static final int DAY_MAX =31;
    static final int DAY_MIN =1;
    static final String DATE_DEFAULT ="---请选择---";
    public InformationSwing(int userId, String eventGroupName){
        this.userId = userId;
        this.eventGroupName = eventGroupName;
        UserService userService = new UserService();
        User user = userService.queryUserInformation(userId);

        JFrame userInformation = new JFrame("TiliTili瓜王系统");
        userInformation.setSize(900,700);
        //设置大小
        userInformation.setLocationRelativeTo(null);
        //窗口可见
        userInformation.setResizable(false);
        //不可拉伸
        userInformation.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
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
        userNameLabel.setBounds(50,100,100,20);
        userNameLabel.setFont(font1);
        jPanel.add(userNameLabel);
        JLabel userName = new JLabel(user.getLoginName());
        userName.setBounds(125,100,60,20);
        userName.setFont(font1);
        jPanel.add(userName);

        JLabel iconLabel = new JLabel("头像:");
        iconLabel.setBounds(520,100,100,20);
        iconLabel.setFont(font1);
        jPanel.add(iconLabel);
        JLabel icon = new JLabel();
        icon.setBounds(580,100,180,200);
        jPanel.add(icon);

        //先查看有没有存过头像，没有就变为默认头像
        File file = new File("C:\\Users\\Mono\\Desktop\\TiliTili照片\\" + userId + ".jpg");
        if(!file.exists()){
            file = new File("C:\\Users\\Mono\\Desktop\\TiliTili照片\\默认.jpg");
        }
        ImageIcon imageIcon = new ImageIcon(file.getAbsolutePath());
        imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(icon.getWidth(),-1,Image.SCALE_DEFAULT));
        //高度设置为-1可以缩放好图片大小
        icon.setIcon(imageIcon);

        JButton set = new JButton("新头像");
        set.setBounds(550,310,90,30);
        jPanel.add(set);

        JButton saveIcon = new JButton("保存");
        saveIcon.setBounds(680,310,60,30);
        jPanel.add(saveIcon);

        JButton resetIcon = new JButton("还原");
        resetIcon.setBounds(780,310,60,30);
        jPanel.add(resetIcon);

        new ImageUtils(icon,jPanel,set,saveIcon,resetIcon,userId);

        //用于显示昵称标签+文本框
        JLabel nickNameLabel = new JLabel("昵称:");
        nickNameLabel.setBounds(50,150,60,20);
        nickNameLabel.setFont(font1);
        jPanel.add(nickNameLabel);
        JTextField nickNameTextField = new JTextField(10);
        nickNameTextField.setBounds(125,150,100,20);
        nickNameTextField.setText(user.getUserNickname());
        jPanel.add(nickNameTextField);

        //用于显示性别标签+单选框
        JLabel gender = new JLabel("性别:");
        gender.setBounds(50,200,60,20);
        gender.setFont(font1);
        jPanel.add(gender);
        JRadioButton girl = new JRadioButton("女");
        girl.setBounds(125,195,80,30);
        JRadioButton boy = new JRadioButton("男");
        boy.setBounds(225,195,80,30);
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
        email.setBounds(50,250,60,20);
        email.setFont(font1);
        jPanel.add(email);
        JTextField emailTextField = new JTextField(20);
        emailTextField.setBounds(125,250,150,20);
        emailTextField.setText(user.getUserEmail());
        jPanel.add(emailTextField);

        JLabel emailTips = new JLabel("正确邮箱格式：1017328759@qq.com");
        emailTips.setBounds(275,250,300,20);
        emailTips.setForeground(Color.red);
        emailTips.setVisible(false);
        jPanel.add(emailTips);

        emailTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if(!"".equals(emailTextField.getText())){
                    String email = emailTextField.getText();
                    boolean judge = new StringUtils().isEmail(email);
                    //合法
                    emailTips.setVisible(!judge);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if(!"".equals(emailTextField.getText())){
                    String email = emailTextField.getText();
                    boolean judge = new StringUtils().isEmail(email);
                    //合法
                    emailTips.setVisible(!judge);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });
        //生日+下拉列表+年月日标签
        JLabel birth = new JLabel("生日:");
        birth.setBounds(50,300,60,20);
        birth.setFont(font1);
        jPanel.add(birth);
        //用集合先存放string,转为数组，数组直接放进列表框
        //产生年份
        ArrayList<String> year1 = new ArrayList<>();
        year1.add(DATE_DEFAULT);
        for (int i = YEAR_MAX;i>=YEAR_MIN;i--){
            year1.add(String.valueOf(i));
        }
        String[] year = year1.toArray(new String[0]);
        //产生月份
        ArrayList<String> month1 = new ArrayList();
        month1.add(DATE_DEFAULT);
        for (int i = MONTH_MIN;i<MONTH_MAX;i++){
            month1.add(String.valueOf(i+1));
        }
        String[] month = month1.toArray(new String[0]);
        //产生日期
        ArrayList<String> day1 = new ArrayList<>();
        day1.add(DATE_DEFAULT);
        for (int i = DAY_MIN; i< DAY_MAX;i++){
            day1.add(String.valueOf(i));
        }
        String[] day = day1.toArray(new String[0]);
        //把年份数组放进下拉列表中
        JComboBox<String> birthyear = new JComboBox<>(year);
        birthyear.setBounds(125,300,100,20);
        JComboBox<String> birthmonth = new JComboBox<>(month);
        birthmonth.setBounds(265,300,100,20);
        JComboBox<String> birthday = new JComboBox<>(day);
        birthday.setBounds(405,300,100,20);
        jPanel.add(birthyear);
        jPanel.add(birthmonth);
        jPanel.add(birthday);
        //年月日的标签提示
        JLabel yearLabel = new JLabel("年");
        yearLabel.setBounds(235,295,30,30);
        yearLabel.setFont(font1);
        jPanel.add(yearLabel);
        JLabel monthLabel = new JLabel("月");
        monthLabel.setBounds(375,295,30,30);
        monthLabel.setFont(font1);
        jPanel.add(monthLabel);
        JLabel dayLabel = new JLabel("日");
        dayLabel.setBounds(515,295,30,30);
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
            birthyear.setSelectedItem(DATE_DEFAULT);
            birthmonth.setSelectedItem(DATE_DEFAULT);
            birthday.setSelectedItem(DATE_DEFAULT);
        }


        //个人简介提示+文本域
        JLabel description = new JLabel("个人简介：");
        description.setBounds(15,350,100,20);
        description.setFont(font1);
        jPanel.add(description);
        //文本域
        JTextArea descriptionTextArea = new JTextArea(20,10);
        descriptionTextArea.setLineWrap(true);
        //自动换行
        descriptionTextArea.setBounds(125,350,500,100);
        descriptionTextArea.setText(user.getUserDescription());
        jPanel.add(descriptionTextArea);
        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setBounds(125,350,500,100);
        jScrollPane.setViewportView(descriptionTextArea);
        jPanel.add(jScrollPane);

        //修改密码的按钮，加监听器，跳转界面
        JButton changePassword = new JButton("修改密码");
        changePassword.setBounds(65,500,90,20);
        changePassword.addActionListener(e -> {
            //修改密码界面
            userInformation.dispose();
            new PasswordSwing(userId,eventGroupName);
        });
        jPanel.add(changePassword);

        //保存按钮,加监听器，获取上方的信息，判断修改个人信息是否完成
        JButton save = new JButton("保存");
        save.setBounds(175,500,80,20);
        save.addActionListener(e -> {
            //昵称
            String userNickname = nickNameTextField.getText();
            //性别
            int userGender1 =2;
            if(girl.isSelected()){
                userGender1 =0;
                //女
            } else if(boy.isSelected()){
                userGender1 =1;
                //男
            }
            //邮箱
            String userEmail = emailTextField.getText();

            Date userBirthday2 = null;
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
                 userBirthday2 = Date.valueOf(userBirthday1);
            }

            //个人简介
            String userDescription = descriptionTextArea.getText();
            int judgement =1;
            if(("---请选择---").equals(birthyear.getSelectedItem())&&("---请选择---").equals(birthmonth.getSelectedItem())&&("---请选择---").equals(birthday.getSelectedItem())){
                int confirm = JOptionPane.showConfirmDialog(null, "您还没有填写生日，确定继续吗？", "确认", JOptionPane.YES_NO_OPTION);
                if(confirm!=0){
                    //NO
                    judgement=0;
                    userBirthday2=null;
                }
            }
            if((("---请选择---").equals(birthyear.getSelectedItem())||("---请选择---").equals(birthmonth.getSelectedItem())||("---请选择---").equals(birthday.getSelectedItem()))
            &&!(("---请选择---").equals(birthyear.getSelectedItem())&&("---请选择---").equals(birthmonth.getSelectedItem())&&("---请选择---").equals(birthday.getSelectedItem()))){
                JOptionPane.showMessageDialog(null,"请填写完整的生日信息","错误",JOptionPane.ERROR_MESSAGE);
                judgement=0;
            }
            if(judgement==1){
                if(emailTips.isVisible()){
                    //错误提示还在
                    JOptionPane.showMessageDialog(null,"请填写正确的邮箱格式","错误",JOptionPane.ERROR_MESSAGE);
                }else {
                    int judge =userService.perfectInformation(userEmail, userNickname, userGender1, userDescription, 2, userBirthday2);
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
        reset.setBounds(275,500,80,20);
        reset.addActionListener(e -> {
            //昵称
            nickNameTextField.setText(user.getUserNickname());
            //性别
            int userGender12 = user.getUserGender();
            if(userGender12 ==0){
                girl.setSelected(true);
            }else {
                boy.setSelected(true);
            }
            //邮箱
            emailTextField.setText(user.getUserEmail());
            //生日
            Date userBirthday12 = user.getUserBirthday();
            //从数据库里获取生日
            Calendar calendar1 = Calendar.getInstance();
            if(userBirthday12 !=null){
                calendar1.setTime(userBirthday12);
                //获取年月日，同时选中
                String userYear = String.valueOf(calendar1.get(Calendar.YEAR));
                birthyear.setSelectedItem(userYear);
                String userMonth = String.valueOf(calendar1.get(Calendar.MONTH)+1);
                //calendar默认从0月开始
                birthmonth.setSelectedItem(userMonth);
                String userDay = String.valueOf(calendar1.get(Calendar.DATE));
                birthday.setSelectedItem(userDay);
            }else {
                birthyear.setSelectedItem("---请选择---");
                birthmonth.setSelectedItem("---请选择---");
                birthday.setSelectedItem("---请选择---");
            }
            //个人简介
            descriptionTextArea.setText(user.getUserDescription());
        });
        jPanel.add(reset);

        //返回按钮
        JButton cancel = new JButton("返回");
        cancel.setBounds(375,500,80,20);
        cancel.addActionListener(e -> {
            userInformation.dispose();
            //退出当前界面
            new GroupsSwing(userId,eventGroupName);
        });
        jPanel.add(cancel);

        userInformation.setVisible(true);
    }


}
