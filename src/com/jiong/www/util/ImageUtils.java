package com.jiong.www.util;

import com.jiong.www.service.serviceImpl.UserServiceImpl;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;

/**
 * @author Mono
 */
public class ImageUtils extends JFrame {
    /**背景图片*/
    JPanel jPanel;
    JLabel jLabel;
    ImageIcon imageChosen;
    /**选择的图片*/
    final int PREVIEW_SIZE = 150;
    /**图片预览的大小*/
    JButton set;
    JButton save;
    JButton reset;
    int userId;
    JFileChooser jFileChooser = new JFileChooser();
    JLabel accessory = new JLabel();
    File file;
    /**预览时图片加载的标签*/
    public ImageUtils(JLabel jLabel, JPanel jPanel, JButton set,JButton save,JButton reset,int userId){
        this.jLabel=jLabel;
        this.jPanel=jPanel;
        this.set =set;
        this.save =save;
        this.reset =reset;
        this.userId=userId;
        init();
    }

    public ImageUtils() {
    }

    public void init(){
        jFileChooser.setCurrentDirectory(new File("C:\\Users\\Mono\\Desktop"));
        //设置当前目录
        jFileChooser.addChoosableFileFilter(new FileCanChoose());
        //设置过滤器
        jFileChooser.setAccessory(accessory);
        //为文件选择器指定一个预览图片的附件组件
        accessory.setPreferredSize(new Dimension(PREVIEW_SIZE, PREVIEW_SIZE));
        //设置预览图片组件的大小
        accessory.setBorder(BorderFactory.createEtchedBorder());
        //设置预览图片组件的蚀刻边框
        //用于检测被选择文件的改变事件
        jFileChooser.addPropertyChangeListener(event -> {
            if (event.getPropertyName().equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY))
            //JFileChooser的被选文件已经发生了改变
            {
                File file = (File) event.getNewValue();
                //获取用户选择的新图片
                if (file == null)
                {
                    accessory.setIcon(null);
                    return;
                }
                ImageIcon icon = new ImageIcon(file.getPath());  //将所文件读入ImageIcon对象中
                //如果图像太大，则缩小它
                if(icon.getIconWidth() > PREVIEW_SIZE)
                {
                    icon = new ImageIcon(icon.getImage()
                            .getScaledInstance(PREVIEW_SIZE, -1, Image.SCALE_DEFAULT));
                }
                //改变accessory Label的图标
                accessory.setIcon(icon);
            }
        });
        //为set增加事件
        set.addActionListener(event -> {
            int result = jFileChooser.showDialog(jPanel , "打开");  //显示文件对话框
            //如果用户选择了APPROVE（赞同）按钮，即放进个人信息或者放进瓜
            if(result == JFileChooser.APPROVE_OPTION)
            {
                if(jFileChooser.getSelectedFile().getName().toLowerCase().endsWith(".png")||jFileChooser.getSelectedFile().getName().toLowerCase().endsWith(".jpg")){
                    String address = jFileChooser.getSelectedFile().getAbsolutePath();
                    imageChosen =new ImageIcon(address); //显示指定图片
                    imageChosen = new ImageIcon(imageChosen.getImage().getScaledInstance(jLabel.getWidth(),-1,Image.SCALE_DEFAULT));
                    //高度设置为-1可以缩放好图片大小
                    jLabel.setIcon(imageChosen);
                    file = jFileChooser.getSelectedFile();
                }else {
                    JOptionPane.showMessageDialog(null,"请选择.jpg格式或者.png格式图片","错误",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        //为save增加事件
        save.addActionListener(e -> {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                int judge = new UserServiceImpl().saveIcon(fileInputStream, userId);
                if(judge==1){
                    JOptionPane.showMessageDialog(null,"保存成功");
                }else {
                    JOptionPane.showMessageDialog(null,"保存失败","错误",JOptionPane.ERROR_MESSAGE);
                }
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
        });
        //为reset增加事件
        reset.addActionListener(e -> {
            boolean flag = new UserServiceImpl().deleteIcon(userId);
            if(!flag){
                JOptionPane.showMessageDialog(null,"已是原始头像","失败",JOptionPane.ERROR_MESSAGE);
            }
        //刷新
            File file = new File("C:\\Users\\Mono\\Desktop\\TiliTili照片\\" + userId + ".jpg");
            if(!file.exists()){
                file = new File("C:\\Users\\Mono\\Desktop\\TiliTili照片\\默认.jpg");
            }
            ImageIcon imageIcon = new ImageIcon(file.getAbsolutePath());
            imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(jLabel.getWidth(),-1,Image.SCALE_DEFAULT));
            //高度设置为-1可以缩放好图片大小
            jLabel.setIcon(imageIcon);
        });

    }

    /**从数据库中读取二进制文件再存入指定文件夹*/
    public void readBlob (InputStream in ,String targetPath){
        File file = new File(targetPath);
        String path = targetPath.substring(0, targetPath.lastIndexOf("\\"));
        if(!file.exists()){
            new File(path).mkdirs();
        }
        FileOutputStream fos =null;
        try {
            fos = new FileOutputStream(file);
            int length ;
            byte[] bytes = new byte[1024];
            while((length = in.read(bytes)) != -1){
                fos.write(bytes,0,length);
            }
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(fos!=null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    class FileCanChoose extends FileFilter{

        @Override
        public boolean accept(File file) {
            String filename =file.getName();
            return filename.toLowerCase().endsWith(".jpg") || filename.toLowerCase().endsWith(".png");
        }

        @Override
        public String getDescription() {

            return "图片文件：.jpg,.png";
        }
    }


}

