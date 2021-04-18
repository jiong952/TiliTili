package com.jiong.www.util;

import com.jiong.www.po.Event;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * @author Mono
 */
public class GroupPagingUtils extends JFrame {
    /**list由外部传入，储存所有的数据*/
    List<Event> events;
    DefaultListModel<String> defaultListModel;
    private int currentPage = 1;
    private int lastPage;
    JButton first;
    JButton previous;
    JButton next;
    JButton last;
    /**页面的展示数目*/
    private final int pageSize ;
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

    public DefaultListModel<String> getDefaultListModel() {
        return defaultListModel;
    }

    public void setDefaultListModel(DefaultListModel<String> defaultListModel) {
        this.defaultListModel = defaultListModel;
    }

    public GroupPagingUtils(List<Event> events, DefaultListModel<String> defaultListModel, int pageSize, JButton first, JButton previous, JButton next, JButton last)  {
        this.events = events;
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
    public List<Event> getList(int currentPage,int pageSize){
        List<Event> sonList;
        //子list
        int listLength = events.size();
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
        sonList = events.subList(fromIndex,toIndex);
        return sonList;
    }

    public void showList(int currentPage){
        //设置末页
        if(events.size()%pageSize==0){
            setLastPage(events.size()/getPageSize());
        }else {
            setLastPage(events.size()/getPageSize()+1);
        }
        defaultListModel.clear();
        //清除原有数据
        setCurrentPage(currentPage);
        List<Event> sonList = getList(currentPage, pageSize);
        for (int i = 0; i < sonList.size(); i++) {
            defaultListModel.add(i,sonList.get(i).getEventName());
        }
    }
    class MyList implements ActionListener {
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
