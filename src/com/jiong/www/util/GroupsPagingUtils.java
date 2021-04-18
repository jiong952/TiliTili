package com.jiong.www.util;

import com.jiong.www.po.EventGroup;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * @author Mono
 */
public class GroupsPagingUtils extends JFrame {
    /**list由外部传入，储存所有的数据*/
    List<EventGroup> eventGroups;
    private int currentPage = 1;
    private int lastPage;
    JButton first;
    JButton previous;
    JButton next;
    JButton last;
    /**页面的展示数目*/
    private final int pageSize ;
    DefaultListModel<String> defaultListModel;

    public DefaultListModel<String> getDefaultListModel() {
        return defaultListModel;
    }

    public void setDefaultListModel(DefaultListModel<String> defaultListModel) {
        this.defaultListModel = defaultListModel;
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


    public GroupsPagingUtils(List<EventGroup> eventGroups, DefaultListModel<String> defaultListModel, int pageSize, JButton first, JButton previous, JButton next, JButton last)  {
        this.eventGroups = eventGroups;
        this.defaultListModel = defaultListModel;
        this.pageSize=pageSize;
        this.first=first;
        this.previous=previous;
        this.next=next;
        this.last=last;
        first.addActionListener(new MyList());
        previous.addActionListener(new MyList());
        next.addActionListener(new MyList());
        last.addActionListener(new MyList());
    }

    /**传入list得到子list*/
    public List<EventGroup> getList(int currentPage,int pageSize){
        List<EventGroup> sonList;
        //子list
        int listLength = eventGroups.size();
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
        sonList = eventGroups.subList(fromIndex,toIndex);
        return sonList;
    }

    public void showList(int currentPage){
        //设置末页
        if(eventGroups.size()%pageSize==0){
            setLastPage(eventGroups.size()/getPageSize());
        }else {
            setLastPage(eventGroups.size()/getPageSize()+1);
        }
        defaultListModel.clear();
        //清除原有数据
        setCurrentPage(currentPage);
        List<EventGroup> sonList = getList(currentPage, pageSize);
        for (int i = 0; i < sonList.size(); i++) {
            defaultListModel.add(i,sonList.get(i).getEventGroupName());
        }
    }
    class MyList implements ActionListener{
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
