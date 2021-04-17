package com.jiong.www.view.swing;

import com.jiong.www.po.Comment;
import com.jiong.www.po.Event;
import com.jiong.www.service.*;
import com.jiong.www.service.service.ICollectionService;
import com.jiong.www.service.service.ICommentService;
import com.jiong.www.service.service.IEventService;
import com.jiong.www.service.serviceImpl.CollectionServiceImpl;
import com.jiong.www.service.serviceImpl.CommentServiceImpl;
import com.jiong.www.service.serviceImpl.EventServiceImpl;
import com.jiong.www.service.serviceImpl.LikesServiceImpl;
import com.jiong.www.util.EventPagingUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * @author Mono
 */
public class EventSwing {
    int userId;
    String eventName;
    int eventId;
    String eventGroupName;
    List<Comment> comments;
    DefaultTableModel defaultTableModel;
    EventPagingUtils eventPagingUtils;
    static final int  ADMIN = 2;
    static final int  SUPER_ADMIN = 4;
    static final int  VISITOR = 3;
    public EventSwing(int userId, String eventName, int eventId, String eventGroupName) {
        this.userId = userId;
        this.eventName = eventName;
        this.eventId=eventId;
        this.eventGroupName=eventGroupName;

        UserService userService = new UserService();
        EventGroupServiceImpl eventGroupServiceImpl = new EventGroupServiceImpl();
        IEventService iEventService = new EventServiceImpl();
        LikesServiceImpl likesServiceImpl = new LikesServiceImpl();
        ICollectionService iCollectionService = new CollectionServiceImpl();
        ICommentService iCommentService = new CommentServiceImpl();

        //设置窗口
        JFrame selectedEvent = new JFrame("TiliTili瓜王系统");
        selectedEvent.setSize(1200,800);
        //设置大小
        selectedEvent.setLocationRelativeTo(null);
        //窗口可见
        selectedEvent.setResizable(false);
        //不可拉伸
        selectedEvent.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
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
        new MenuSwing(userId,selectedEvent,eventGroupName);
        Event event = iEventService.doView(eventName);

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
        int judge = likesServiceImpl.queryLikes(userId, eventId);
        if(judge==1){
            like.setSelected(true);
        }else {
            notLike.setSelected(true);
        }
        //点赞
        like.addActionListener(e -> {
            likesServiceImpl.doLikes(userId,eventId);
            Event event1 = iEventService.doView(eventName);
            likesNumber.setText(String.valueOf(event1.getLikesNum()));
        });
        //取消点赞
        notLike.addActionListener(e -> {
            int judge1 = JOptionPane.showConfirmDialog(null, "您确定要取消点赞吗？", "删除提示", JOptionPane.YES_NO_OPTION);
            if(judge1 ==0){
                //YES
            likesServiceImpl.doCancelLikes(userId,eventId);
            Event event1 = iEventService.doView(eventName);
            likesNumber.setText(String.valueOf(event1.getLikesNum()));
            }else {
                like.setSelected(true);
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
        int judge2 = iCollectionService.queryCollect(userId, eventId);
        if(judge2==1){
            collected.setSelected(true);
        }else {
            notCollected.setSelected(true);
        }
        //收藏
        collected.addActionListener(e -> {
            iCollectionService.doCollect(userId,eventId);
            Event event1 = iEventService.doView(eventName);
            collectionNumber.setText(String.valueOf(event1.getCollectionNum()));
        });
        //取消收藏
        notCollected.addActionListener(e -> {
            int judge3 = JOptionPane.showConfirmDialog(null, "您确定要取消收藏吗？", "删除提示", JOptionPane.YES_NO_OPTION);
            if(judge3 ==0){
                iCollectionService.doCancelCollect(userId,eventId);
                Event event1 = iEventService.doView(eventName);
                collectionNumber.setText(String.valueOf(event1.getCollectionNum()));
            }else {
                collected.setSelected(true);
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

        JButton first = new JButton("首页");
        first.setBounds(245,575,60,30);
        first.setActionCommand("首页");
        jPanel.add(first);

        JButton previous = new JButton("上一页");
        previous.setBounds(345,575,90,30);
        previous.setActionCommand("上一页");
        jPanel.add(previous);

        JButton next = new JButton("下一页");
        next.setBounds(475,575,90,30);
        next.setActionCommand("下一页");
        jPanel.add(next);

        JButton last = new JButton("尾页");
        last.setBounds(605,575,60,30);
        last.setActionCommand("尾页");
        jPanel.add(last);

        String[] columnNames = {"评论人","评论内容","评论时间"};
        //查询瓜的所有评论
        comments = iCommentService.findAll(event.getEventId());
        int pageSize = 6;
        //每一页展示评论数目
        Object[][] rowData = iCommentService.doDataProcess(pageSize,comments);
        //rowData里面经过数据处理，放第一页的数据
        //创建一个表格来放评论
        JTable table = new JTable();
        defaultTableModel = new DefaultTableModel(rowData, columnNames){
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };

        table.setModel(defaultTableModel);
        table.setFont(font1);
        table.setRowHeight(32);
        table.setBounds(135,420,800,150);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //单选
        JScrollPane jScrollPane1 = new JScrollPane();
        jScrollPane1.setBounds(135,420,920,150);
        jScrollPane1.setViewportView(table);
        jPanel.add(jScrollPane1);

        //分页处理
        eventPagingUtils = new EventPagingUtils(comments, defaultTableModel, pageSize, first, previous, next, last);

        //评论标签
        JLabel myComment = new JLabel("我要评论:");
        myComment.setFont(font1);
        myComment.setForeground(Color.PINK);
        myComment.setBounds(20,637,80,30);
        jPanel.add(myComment);

        //评论区文本框
        JTextArea myCommentArea = new JTextArea();
        myCommentArea.setBounds(100,637,550,100);
        jPanel.add(myCommentArea);
        JScrollPane jScrollPane2 = new JScrollPane();
        jScrollPane2.setBounds(100,637,550,100);
        jScrollPane2.setViewportView(myCommentArea);
        jPanel.add(jScrollPane2);
        //评论发送按钮
        JButton sendComment = new JButton("发送");
        sendComment.setBounds(700,707,60,30);
        jPanel.add(sendComment);
        //发送按钮的监听器
        sendComment.addActionListener(e -> {
            if("".equals(myCommentArea.getText())){
                JOptionPane.showMessageDialog(null,"评论内容不能为空！","错误",JOptionPane.ERROR_MESSAGE);
            }else {
            iCommentService.doComment(userId,eventId,myCommentArea.getText());
            JOptionPane.showMessageDialog(null,"评论成功！");
            //置空
            myCommentArea.setText("");
            //重新设置数据源重新分页
            comments=iCommentService.doRefresh(comments, defaultTableModel, eventId, columnNames,eventPagingUtils);
            //重新设置评论数
            commentNumber.setText(String.valueOf(iEventService.doView(eventName).getCommentNum()));
            }
        });

        //删除评论+清空评论
        JButton deleteComment= new JButton("删除评论");
        deleteComment.setBounds(750,637,90,30);
        deleteComment.addActionListener(e -> {
            if(table.getSelectedRow()<0){
                JOptionPane.showMessageDialog(null,"请先单击选择要删除的评论!","错误",JOptionPane.ERROR_MESSAGE);
            }else {
            int judge4 = userService.verifyRole(userId);
            if(judge4 ==1){
                //吃瓜群众
                if(userId==comments.get(table.getSelectedRow()).getCommenterId()){
                    //由第一页的相应行数推得删除行的实际行数
                    iCommentService.doCancel(comments.get(((eventPagingUtils.getCurrentPage()-1)*pageSize+table.getSelectedRow())).getCommentId(),eventId);
                    JOptionPane.showMessageDialog(null,"删除成功");
                    //重新设置数据源重新分页
                    comments=iCommentService.doRefresh(comments, defaultTableModel, eventId, columnNames,eventPagingUtils);
                    //重新设置评论数
                    commentNumber.setText(String.valueOf(iEventService.doView(eventName).getCommentNum()));
                }else {
                    JOptionPane.showMessageDialog(null,"您没有权限","错误",JOptionPane.ERROR_MESSAGE);
                }
            }else if(judge4 ==2){
                //管理员
                int judge5 = eventGroupServiceImpl.verifyOfAdmin(userId, eventGroupName);
                if(judge5 ==1){
                    iCommentService.doCancel(comments.get(table.getSelectedRow()).getCommentId(),eventId);
                    JOptionPane.showMessageDialog(null,"删除成功！");
                    //重新设置数据源重新分页
                    comments= iCommentService.doRefresh(comments, defaultTableModel, eventId, columnNames,eventPagingUtils);
                    //重新设置评论数
                    commentNumber.setText(String.valueOf(iEventService.doView(eventName).getCommentNum()));
                }else {
                    JOptionPane.showMessageDialog(null,"这不是您管理的瓜圈","错误",JOptionPane.ERROR_MESSAGE);
                }
            }
            }
        });
        jPanel.add(deleteComment);

        JButton clearComment= new JButton("清空评论");
        clearComment.setBounds(850,637,90,30);
        clearComment.addActionListener(e -> {
            int judge6 = eventGroupServiceImpl.verifyOfAdmin(userId, eventGroupName);
            if(judge6 ==1){
                //是管理员管理的
                iCommentService.doClear(eventId);
                JOptionPane.showMessageDialog(null,"删除成功！");
                //重新设置数据源
                defaultTableModel.setDataVector(null,columnNames);
                //重新设置评论数
                commentNumber.setText(String.valueOf(iEventService.doView(eventName).getCommentNum()));
            }else {
                JOptionPane.showMessageDialog(null,"这不是您管理的瓜圈","错误",JOptionPane.ERROR_MESSAGE);
            }
    });
        clearComment.setVisible(false);
        jPanel.add(clearComment);
        JButton delete = new JButton("删除瓜");
        delete.setBounds(1050,637,90,30);
        delete.addActionListener(e -> {
            int judge0 = eventGroupServiceImpl.verifyOfAdmin(userId, eventGroupName);
            //判断是不是该管理员管理的瓜圈
            if (judge0 == 1) {
                //是
                int judge12 = JOptionPane.showConfirmDialog(null, "您确定要删除此瓜吗？", "确认", JOptionPane.YES_NO_OPTION);
                if (judge12 == 0) {
                    //选择是
                    judge0 = iEventService.doDelete(eventId);
                    if (judge0 == 1) {
                        JOptionPane.showMessageDialog(null, "删除瓜成功！");
                        new GroupsSwing(userId, eventGroupName);
                    }
                }
            } else {
                //不是
                JOptionPane.showMessageDialog(null, "这不是您管理的瓜圈", "错误", JOptionPane.ERROR_MESSAGE);
            }

        });
        delete.setVisible(false);
        jPanel.add(delete);

        //清空评论只有在管理员时显示
        int roleId = new UserService().verifyRole(userId);
        if(roleId==ADMIN||roleId==SUPER_ADMIN){
            clearComment.setVisible(true);
            delete.setVisible(true);
        }

        //举报按钮
        JButton accuse = new JButton("举报");
        accuse.setBounds(850,707,90,30);
        accuse.addActionListener(e -> new AccuseSwing(userId,eventName,eventId));
        jPanel.add(accuse);

        //返回上一层按钮
        JButton back = new JButton("返回");
        back.setBounds(1050,707,78,30);
        back.addActionListener(e -> {
            selectedEvent.dispose();
            new GroupSwing(userId,eventGroupName);
        });
        back.setFont(font1);
        jPanel.add(back);

        if(roleId==VISITOR){
            myComment.setVisible(false);
            myCommentArea.setVisible(false);
            sendComment.setVisible(false);
            deleteComment.setVisible(false);
            accuse.setVisible(false);
            like.setVisible(false);
            notLike.setVisible(false);
            collected.setVisible(false);
            notCollected.setVisible(false);
        }
        selectedEvent.setVisible(true);

    }
}
