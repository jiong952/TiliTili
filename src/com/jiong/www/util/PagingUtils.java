package com.jiong.www.util;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Mono
 */
public class PagingUtils<T>extends JFrame {
    /**list由外部传入，储存所有的数据*/
    List<T> t;
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

    public PagingUtils(List<T> t, DefaultListModel<String> defaultListModel, int pageSize, JButton first, JButton previous, JButton next, JButton last)  {
        this.t = t;
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
    public List<T> getList(int currentPage,int pageSize){
        List<T> sonList;
        //子list
        int listLength = t.size();
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
        sonList = t.subList(fromIndex,toIndex);
        return sonList;
    }

    public void showList(int currentPage)  {
        //设置末页
        if(t.size()%pageSize==0){
            setLastPage(t.size()/getPageSize());
        }else {
            setLastPage(t.size()/getPageSize()+1);
        }
        defaultListModel.clear();
        //清除原有数据
        setCurrentPage(currentPage);
        List<T> sonList = getList(currentPage, pageSize);
        for (int i = 0; i < sonList.size(); i++) {
            defaultListModel.add(i,getName(sonList.get(i)));
        }
    }
    public String getName(T t)  {
        Class<?> tClass = t.getClass();
        //得到T的所有属性
        Field[] field = tClass.getDeclaredFields();
        //event 和 eventGroup中 Name 都在第一个
        field[0].setAccessible(true);
        //获取属性的名字
        String filedName = field[0].getName();
        //将属性名字的首字母大写
        filedName = filedName.replaceFirst(filedName.substring(0, 1), filedName.substring(0, 1).toUpperCase());
        //整合出 getName() 属性这个方法
        Method m = null;
        try {
            m = tClass.getMethod("get"+filedName);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        //调用这个整合出来的get方法，强转成自己需要的类型
        String name = null;
        try {
            assert m != null;
            name = (String)m.invoke(t);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return name;
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
