package com.jiong.www.test;

import com.jiong.www.util.Md5Utils;

import java.text.*;

public class Main {
    public static void main(String[] args) {
        //按钮增加图片JButton b1 = new JButton(new ImageIcon("D:/images/1.jpg"));
        //邮箱增加后缀报错
        //头像的添加 文件选择器？
        //密码进行加密  MD5？
        //最后增加超级管理员的功能
        //界面美化：加显示当前的时间
        // 菜单项可以设置快捷键
        // 界面的外框可以加图标
        //瓜 瓜圈 判断是否被点过？
        Md5Utils md5Utils = new Md5Utils();
        String password = new String("123456");
        System.out.println(md5Utils.toMD5(password));

    }
}
