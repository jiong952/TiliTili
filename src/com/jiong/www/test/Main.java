package com.jiong.www.test;

import com.jiong.www.util.ImageUtils;
import com.jiong.www.util.JdbcUtils;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, IOException {
        //按钮增加图片JButton b1 = new JButton(new ImageIcon("D:/images/1.jpg"));
        //头像的添加 文件选择器？
        //最后增加超级管理员的功能
        //界面美化：加显示当前的时间
        // 菜单项可以设置快捷键
        // 界面的外框可以加图标
        //瓜 瓜圈 判断是否被点过？

        Connection connection = JdbcUtils.getConnection();
//        String sql="INSERT INTO `text` (id,image) VALUES(?,?)";
//        File file = new File("C:\\Users\\Mono\\Desktop\\助理\\歌队\\1.jpg");
//        FileInputStream fileInputStream = new FileInputStream(file);
        String sql="SELECT * FROM `text` where `id`=1";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
//        preparedStatement.setInt(1,1);
//        preparedStatement.setBinaryStream(2,fileInputStream,fileInputStream.available());
//        int i = preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            InputStream binaryStream = resultSet.getBinaryStream("image");
            new ImageUtils().readBlob(binaryStream,"C:\\Users\\Mono\\Desktop\\TiliTili照片\\1.png");
        }
        JdbcUtils.release(connection,preparedStatement,resultSet);

    }
}
