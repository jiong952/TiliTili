package com.jiong.www.util;

import com.jiong.www.po.Comment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

/**
 * @author Mono
 */
public class EventPagingUtils {
    /**list由外部传入，储存所有的数据*/
    List<Comment> list;
    private int currentPage = 1;
    private int lastPage;
    /**页面的展示数目*/
    private final int pageSize ;
    DefaultTableModel defaultTableModel;
    JPanel jPanel;
    /**常量，避免魔法值*/
    static final String FIRST_PAGE = "首页";
    static final String LAST_PAGE = "尾页";
    static final String PREVIOUS_PAGE = "上一页";
    static final String NEXT_PAGE = "下一页";
    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public int getPageSize() {
        return pageSize;
    }
    /**有参构造*/
    public EventPagingUtils(List<Comment> list, DefaultTableModel defaultTableModel, JPanel jPanel,int pageSize)  {
        this.list = list;
        this.defaultTableModel=defaultTableModel;
        this.jPanel=jPanel;
        this.pageSize=pageSize;
        //设置末页
        if(list.size()%pageSize==0){
            setLastPage(list.size()/getPageSize());
        }else {
            setLastPage(list.size()/getPageSize()+1);
        }
        JButton first = new JButton("首页");
        first.setBounds(245,575,60,30);
        first.setActionCommand("首页");
        first.addActionListener(new MyTable());
        jPanel.add(first);

        JButton previous = new JButton("上一页");
        previous.setBounds(345,575,90,30);
        previous.setActionCommand("上一页");
        previous.addActionListener(new MyTable());
        jPanel.add(previous);

        JButton next = new JButton("下一页");
        next.setBounds(475,575,90,30);
        next.setActionCommand("下一页");
        next.addActionListener(new MyTable());
        jPanel.add(next);

        JButton last = new JButton("尾页");
        last.setBounds(605,575,60,30);
        last.setActionCommand("尾页");
        last.addActionListener(new MyTable());
        jPanel.add(last);

    }

    /**传入list得到子list*/
    public List<Comment> getList(int currentPage,int pageSize){
        List<Comment> sonList;
        //子list
        int listLength = list.size();
        //总长度
        if(currentPage<1){
            currentPage=1;
        }
        int fromIndex=(currentPage-1)*pageSize;
        //初始下标
        int toIndex=fromIndex+pageSize;
        //初始下标加限定的页面长度
        if(toIndex>=listLength){
            toIndex=listLength;
            //防止下标越界
        }
        sonList = list.subList(fromIndex,toIndex);
        return sonList;
    }

    public void showList(int currentPage){
        defaultTableModel.setRowCount(0);
        //清除原有数据
        setCurrentPage(currentPage);
        List<Comment> sonList = getList(currentPage, pageSize);
        for (Comment value : sonList) {
            Vector<Object> vector = new Vector<>();
            vector.add(value.getCommenterName());
            vector.add(value.getCommentContent());
            vector.add(value.getCommentTime());
            defaultTableModel.addRow(vector);
        }
    }

    /**监听器类*/
    class MyTable implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(FIRST_PAGE.equals(e.getActionCommand())){
                showList(1);
            }
            if(PREVIOUS_PAGE.equals(e.getActionCommand())){
                if(getCurrentPage()<=1){
                    setCurrentPage(2);
                }
                showList(getCurrentPage()-1);
            }
            if(NEXT_PAGE.equals(e.getActionCommand())){
                if(getCurrentPage()<getLastPage()){
                    showList(getCurrentPage()+1);
                }else {
                    showList(getLastPage());
                }
            }
            if(LAST_PAGE.equals(e.getActionCommand())){
                showList(getLastPage());
            }
        }
    }
}
