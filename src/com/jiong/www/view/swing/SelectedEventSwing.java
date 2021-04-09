package com.jiong.www.view.swing;

import com.jiong.www.po.Comment;
import com.jiong.www.po.Event;
import com.jiong.www.service.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * @author Mono
 */
public class SelectedEventSwing {
    int userId;
    String eventName;
    int eventId;
    String eventGroupName;
    public static void main(String[] args) {
        new SelectedEventSwing(2,"赵英俊留给世界最后的话：不要把我忘了，永别了",7,"范冰冰");
    }
    public SelectedEventSwing(int userId, String eventName, int eventId, String eventGroupName) {
        this.userId = userId;
        this.eventName = eventName;
        this.eventId=eventId;
        this.eventGroupName=eventGroupName;
        UserService userService = new UserService();
        EventGroupService eventGroupService = new EventGroupService();
        EventService eventService = new EventService();
        LikesService likesService = new LikesService();
        CollectionService collectionService = new CollectionService();
        CommentService commentService = new CommentService();
        //设置窗口
        JFrame selectedEvent = new JFrame("TiliTili瓜王系统");
        selectedEvent.setSize(1200,800);
        //设置大小
        selectedEvent.setLocationRelativeTo(null);
        //窗口可见
        selectedEvent.setResizable(false);
        //不可拉伸
        selectedEvent.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //默认关闭

        //放面板
        JPanel jPanel =new JPanel();
        selectedEvent.add(jPanel);
        jPanel.setLayout(null);
        //绝对布局
        Font font = new Font("宋体", Font.BOLD, 40);
        //居中显示瓜名
        JLabel jLabel = new JLabel(eventName);
        jLabel.setFont(font);
        jLabel.setSize(1200,100);
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel.setForeground(Color.PINK);
        jPanel.add(jLabel);

        //加入顶部菜单栏
        new MenuSwing(userId,selectedEvent);

        Event event = eventService.viewEvent(eventName);

        //显示作者+发布时间+点赞量+点赞的单选按钮+收藏量+收藏的单选按钮+评论数+评论按钮  查询的结果设置为醒目的红色
        Font font1 = new Font("黑体",Font.PLAIN,15);
        //作者名
        JLabel authorLabel = new JLabel("作者名：");
        authorLabel.setBounds(150,70,100,60);
        authorLabel.setFont(font1);
        jPanel.add(authorLabel);
        JLabel author = new JLabel(event.getPublisherName());
        author.setBounds(220,70,200,60);
        author.setFont(font1);
        author.setForeground(Color.red);
        jPanel.add(author);
        //发布时间
        JLabel createTimeLabel = new JLabel("发布时间:");
        createTimeLabel.setBounds(380,70,100,60);
        createTimeLabel.setFont(font1);
        jPanel.add(createTimeLabel);
        JLabel createTime = new JLabel(event.getCreateTime().toString());
        createTime.setBounds(480,70,200,60);
        createTime.setFont(font1);
        createTime.setForeground(Color.red);
        jPanel.add(createTime);

        //点赞量标签 + 查询的点赞数 + 点赞的单选按钮 + 两个按钮加上监听器
        JLabel likesNumberLabel = new JLabel("点赞量:");
        likesNumberLabel.setBounds(150,110,100,60);
        likesNumberLabel.setFont(font1);
        jPanel.add(likesNumberLabel);
        JLabel likesNumber = new JLabel(String.valueOf(event.getLikesNum()));
        likesNumber.setBounds(220,110,100,60);
        likesNumber.setFont(font1);
        likesNumber.setForeground(Color.red);
        jPanel.add(likesNumber);
        JRadioButton like = new JRadioButton("点赞");
        like.setBounds(240,126,60,30);
        JRadioButton notLike = new JRadioButton("不点赞");
        notLike.setBounds(300,126,70,30);
        //查询是否已经点赞
        int judge = likesService.likesIfOrNot(userId, eventId);
        if(judge==1){
            like.setSelected(true);
        }else {
            notLike.setSelected(true);
        }
        //点赞
        like.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                likesService.likes(userId,eventId);
                Event event1 = eventService.viewEvent(eventName);
                likesNumber.setText(String.valueOf(event1.getLikesNum()));
            }
        });
        //取消点赞
        notLike.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                likesService.cancelLikes(userId,eventId);
                Event event1 = eventService.viewEvent(eventName);
                likesNumber.setText(String.valueOf(event1.getLikesNum()));
            }
        });
        //把单选按钮放进按钮组
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(like);
        buttonGroup.add(notLike);
        jPanel.add(like);
        jPanel.add(notLike);

        //收藏量标签 + 查询的收藏数 + 收藏的单选按钮 + 两个按钮加上监听器
        JLabel collectionNumberLabel = new JLabel("收藏量:");
        collectionNumberLabel.setBounds(380,110,100,60);
        collectionNumberLabel.setFont(font1);
        jPanel.add(collectionNumberLabel);
        JLabel collectionNumber = new JLabel(String.valueOf(event.getCollectionNum()));
        collectionNumber.setBounds(450,110,100,60);
        collectionNumber.setFont(font1);
        collectionNumber.setForeground(Color.red);
        jPanel.add(collectionNumber);
        JRadioButton collected = new JRadioButton("收藏");
        collected.setBounds(470,126,60,30);
        JRadioButton notCollected = new JRadioButton("不收藏");
        notCollected.setBounds(530,126,70,30);
        //查询是否已经收藏
        int judge1 = collectionService.collectionIfOrNot(userId, eventId);
        if(judge1==1){
            collected.setSelected(true);
        }else {
            notCollected.setSelected(true);
        }
        //收藏
        collected.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                collectionService.collection(userId,eventId);
                Event event1 = eventService.viewEvent(eventName);
                collectionNumber.setText(String.valueOf(event1.getCollectionNum()));
            }
        });
        //取消收藏
        notCollected.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                collectionService.cancelCollection(userId,eventId);
                Event event1 = eventService.viewEvent(eventName);
                collectionNumber.setText(String.valueOf(event1.getCollectionNum()));
            }
        });
        //把两个按钮放进按钮组
        ButtonGroup buttonGroup1 = new ButtonGroup();
        buttonGroup1.add(collected);
        buttonGroup1.add(notCollected);
        jPanel.add(collected);
        jPanel.add(notCollected);

        //评论标签+查询的评论数+评论按钮
        JLabel commentNumberLabel = new JLabel("评论数:");
        commentNumberLabel.setBounds(610,110,100,60);
        commentNumberLabel.setFont(font1);
        jPanel.add(commentNumberLabel);
        //放评论数
        JLabel commentNumber = new JLabel(String.valueOf(event.getCommentNum()));
        commentNumber.setBounds(680,110,100,60);
        commentNumber.setFont(font1);
        commentNumber.setForeground(Color.red);
        jPanel.add(commentNumber);
        JButton comment = new JButton("评论");
        comment.setBounds(720,125,60,30);
        jPanel.add(comment);
        //评论按钮的监听器，点击则跳转到评论界面，这样回来就可以自动更新评论
        comment.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //进入评论界面
                selectedEvent.dispose();
                new CommentingSwing(userId, eventId, eventName, eventGroupName);

            }
        });



        //瓜的内容加一个滚动面板防止内容过多
        JTextArea eventContent = new JTextArea(event.getEventContent());
        eventContent.setBounds(130,170,920,200);
        eventContent.setEditable(false);
        jPanel.add(eventContent);
        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setBounds(130,170,920,200);
        jScrollPane.setViewportView(eventContent);
        jPanel.add(jScrollPane);

        //评论区的标签
        JLabel commentLabel = new JLabel("评论区");
        commentLabel.setBounds(8,375,150,60);
        commentLabel.setFont(font);
        commentLabel.setForeground(Color.PINK);
        jPanel.add(commentLabel);

        //创建一个列表框来放评论
        JList<String> list = new JList<>();
        list.setFont(font1);
        list.setFixedCellHeight(40);
        //单元格的大小
        list.setSelectionBackground(Color.gray);
        jPanel.add(list);
        //查询瓜的所有评论
        List<Comment> comments = commentService.viewComment(event.getEventId());
        //用listModel放评论，便于修改
        DefaultListModel<String> listModel = new DefaultListModel<String>();
        for (int i = 0; i < comments.size(); i++) {

            if(userId==comments.get(i).getCommenterId()){
                listModel.add(i, "***我的评论***："+comments.get(i).getCommentContent() + "              " + "评论人：" + comments.get(i).getCommenterName() + "     评论时间:" + comments.get(i).getCommentTime().toString());
            }else {
                 listModel.add(i, comments.get(i).getCommentContent() + "              " + "评论人：" + comments.get(i).getCommenterName() + "     评论时间:" + comments.get(i).getCommentTime().toString());
            }
        }
        list.setModel(listModel);
        //向列表框中加入该瓜圈的所有瓜名
        JScrollPane jScrollPane1 = new JScrollPane();
        jScrollPane1.setBounds(135,420,920,250);
        jScrollPane1.setViewportView(list);
        jPanel.add(jScrollPane1);

        //添加一个弹出式菜单，在选中评论后右键就弹出
        JPopupMenu jPopupMenu = new JPopupMenu();
        JMenu delete = new JMenu("删除");
        jPopupMenu.add(delete);
        //删除单个评论
        JMenuItem deleteComment = new JMenuItem("删除该评论");
        deleteComment.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int judge = userService.verifyRole(userId);
                if(judge==1){
                    //吃瓜群众
                    if(userId==comments.get(list.getSelectedIndex()).getCommenterId()){
                        commentService.cancelComment(comments.get(list.getSelectedIndex()).getCommentId(),eventId);
                        JOptionPane.showMessageDialog(null,"删除成功");
                        List<Comment> comments1 = commentService.viewComment(event.getEventId());
                        for (int i = 0; i < comments1.size(); i++) {
                            listModel.set(i,comments1.get(i).getCommentContent()+"              "+"评论人："+comments1.get(i).getCommenterName()+"     评论时间:"+comments1.get(i).getCommentTime().toString());
                            listModel.set(comments1.size(),null);
                        }
                    }else {
                        JOptionPane.showMessageDialog(null,"您没有权限","错误",JOptionPane.ERROR_MESSAGE);
                    }
                }else if(judge==2){
                    //管理员
                    int judge1 = eventGroupService.verifyEventGroupOfAdmin(userId, eventGroupName);
                    if(judge1==1){
                        commentService.cancelComment(comments.get(list.getSelectedIndex()).getCommenterId(),eventId);
                        JOptionPane.showMessageDialog(null,"删除成功！");
                        List<Comment> comments1 = commentService.viewComment(event.getEventId());
                        //更新评论区
                        for (int i = 0; i < comments1.size(); i++) {
                            listModel.set(i,comments1.get(i).getCommentContent()+"              "+"评论人："+comments1.get(i).getCommenterName()+"     评论时间:"+comments1.get(i).getCommentTime().toString());
                            listModel.set(comments1.size(),null);
                        }
                    }else {
                        JOptionPane.showMessageDialog(null,"这不是您管理的瓜圈","错误",JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        //删除所有的评论
        JMenuItem deleteAllComment = new JMenuItem("删除所有评论");
        deleteAllComment.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int judge = userService.verifyRole(userId);
                if(judge==1){
                    //吃瓜群众
                    JOptionPane.showMessageDialog(null,"您没有权限","错误",JOptionPane.ERROR_MESSAGE);
                }else if(judge==2){
                    //管理员
                    int judge1 = eventGroupService.verifyEventGroupOfAdmin(userId, eventGroupName);
                    if(judge1==1){
                        commentService.clearComment(eventId);
                        JOptionPane.showMessageDialog(null,"删除成功！");
                        //清空
                        for (int i = 0; i < comments.size(); i++) {
                            listModel.set(i,null);
                        }
                    }else {
                        JOptionPane.showMessageDialog(null,"这不是您管理的瓜圈","错误",JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        delete.add(deleteComment);
        delete.add(deleteAllComment);
        list.add(jPopupMenu);
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(e.getButton()==MouseEvent.BUTTON3&& !list.isSelectionEmpty()){
                    //右键
                    jPopupMenu.show(list,e.getX(),e.getY());
                }
            }
        });



        selectedEvent.setVisible(true);





    }
}
