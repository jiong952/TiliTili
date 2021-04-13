package com.jiong.www.util;

import com.jiong.www.po.Event;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mono
 */
public class GroupPagingUtils extends JFrame {
    List<Event> list = new ArrayList<Event>();
    //list由外部传入，储存所有的数据
    private int currentPage = 1;
    private int lastPage;
    private int pageSize ;
    //页面的展示数目
    DefaultListModel defaultListModel = null;
    JPanel jPanel;
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

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public GroupPagingUtils(List<Event> list, DefaultListModel defaultListModel, JPanel jPanel,int pageSize)  {
        this.list = list;
        this.defaultListModel = defaultListModel;
        this.jPanel=jPanel;
        this.pageSize=pageSize;
        //设置末页
        if(list.size()%pageSize==0){
            setLastPage(list.size()/getPageSize());
        }else {
            setLastPage(list.size()/getPageSize()+1);
        }
        JButton first = new JButton("首页");
        first.setBounds(245,510,60,30);
        first.setActionCommand("首页");
        first.addActionListener(new GroupPagingUtils.MyList());
        jPanel.add(first);

        JButton previous = new JButton("上一页");
        previous.setBounds(345,510,90,30);
        previous.setActionCommand("上一页");
        previous.addActionListener(new GroupPagingUtils.MyList());
        jPanel.add(previous);

        JButton next = new JButton("下一页");
        next.setBounds(475,510,90,30);
        next.setActionCommand("下一页");
        next.addActionListener(new GroupPagingUtils.MyList());
        jPanel.add(next);

        JButton last = new JButton("尾页");
        last.setBounds(605,510,60,30);
        last.setActionCommand("尾页");
        last.addActionListener(new GroupPagingUtils.MyList());
        jPanel.add(last);

    }

    //传入list得到子list
    public List<Event> getList(int currentPage,int pageSize){
        List<Event> sonList = new ArrayList();
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
            if("首页".equals(e.getActionCommand())){
                showList(1);
            }
            if("上一页".equals(e.getActionCommand())){
                if(getCurrentPage()<=1){
                    setCurrentPage(2);
                }
                showList(getCurrentPage()-1);
            }
            if("下一页".equals(e.getActionCommand())){
                if(getCurrentPage()<getLastPage()){
                    showList(getCurrentPage()+1);
                }else {
                    showList(getLastPage());
                }
            }
            if("尾页".equals(e.getActionCommand())){
                showList(getLastPage());
            }
        }
    }


}
