package com.jiong.www.view.swing.commentSwing;

import com.jiong.www.po.Comment;
import com.jiong.www.po.Event;
import com.jiong.www.service.service.ICommentService;
import com.jiong.www.service.service.IEventService;
import com.jiong.www.service.serviceImpl.CommentServiceImpl;
import com.jiong.www.service.serviceImpl.EventServiceImpl;
import com.jiong.www.view.swing.eventSwing.EventSwing;

import javax.swing.*;
import java.awt.*;

/**
 * @author Mono
 */
public class ViewCommentSwing {
    int commentId;

    public ViewCommentSwing(int commentId) {
        this.commentId = commentId;
        ICommentService iCommentService = new CommentServiceImpl();
        JFrame jFrame = new JFrame("TiliTili瓜王系统");
        jFrame.setSize(500,560);
        //设置大小
        jFrame.setLocationRelativeTo(null);
        //窗口可见
        jFrame.setResizable(false);
        //不可拉伸
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JPanel jPanel =new JPanel();
        jFrame.add(jPanel);
        jPanel.setLayout(null);
        //绝对布局

        Comment comment = iCommentService.doView(commentId);

        Font font = new Font("黑体",Font.BOLD,18);
        //评论人标签+文本框
        JLabel commenterLabel = new JLabel("评论人:");
        commenterLabel.setBounds(100,100,120,30);
        commenterLabel.setFont(font);
        jPanel.add(commenterLabel);
        JLabel commenter = new JLabel(comment.getCommenterName());
        commenter.setBounds(180,100,120,30);
        commenter.setFont(font);
        jPanel.add(commenter);

        //评论时间
        JLabel commentTimeLabel = new JLabel("评论时间");
        commentTimeLabel.setBounds(100,150,120,30);
        commentTimeLabel.setFont(font);
        jPanel.add(commentTimeLabel);
        JLabel commentTime = new JLabel(comment.getCommentTime().toString());
        commentTime.setFont(font);
        commentTime.setBounds(200,150,250,20);
        jPanel.add(commentTime);

        //评论内容标签+文本框+滚动面板
        JLabel commentContentLabel = new JLabel("新瓜内容:");
        commentContentLabel.setBounds(100,175,120,30);
        commentContentLabel.setFont(font);
        jPanel.add(commentContentLabel);
        JTextArea commentContent = new JTextArea(comment.getCommentContent());
        commentContent.setEditable(false);
        commentContent.setBounds(100,200,300,150);
        jPanel.add(commentContent);
        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setBounds(100,200,300,150);
        jScrollPane.setViewportView(commentContent);
        jPanel.add(jScrollPane);

        jFrame.setVisible(true);
        //窗口可见
    }
}
