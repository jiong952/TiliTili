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
public class CommentPagingUtils {
    /**list由外部传入，储存所有的数据*/
    List<Comment> list;
    private int currentPage = 1;
    private int lastPage;
    JButton first;
    JButton previous;
    JButton next;
    JButton last;
    /**页面的展示数目*/
    private final int pageSize ;
    DefaultTableModel defaultTableModel;

    public void setList(List<Comment> list) {
        this.list = list;
    }

    public void setDefaultTableModel(DefaultTableModel defaultTableModel) {
        this.defaultTableModel = defaultTableModel;
    }

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
    public CommentPagingUtils(List<Comment> list, DefaultTableModel defaultTableModel, int pageSize, JButton first, JButton previous, JButton next, JButton last)  {
        this.list = list;
        this.defaultTableModel=defaultTableModel;
        this.pageSize=pageSize;
        this.first=first;
        this.previous=previous;
        this.next=next;
        this.last=last;
        first.addActionListener(new MyTable());
        previous.addActionListener(new MyTable());
        next.addActionListener(new MyTable());
        last.addActionListener(new MyTable());

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
        //设置末页
        if(list.size()%pageSize==0){
            setLastPage(list.size()/getPageSize());
        }else {
            setLastPage(list.size()/getPageSize()+1);
        }
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
