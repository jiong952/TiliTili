package com.jiong.www.service;

import com.jiong.www.dao.AccuseDao;
import com.jiong.www.po.Accuse;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccuseService {
    AccuseDao accuseDao =new AccuseDao();

    //用户可以举报瓜，给管理员处理
    public int accuseEvent(int eventId,int userId,String accusedContent){
        int row=0;
        Accuse accuse = new Accuse();
        accuse.setEventId(eventId);
        accuse.setAccusedUserId(userId);
        accuse.setAccusedContent(accusedContent);
        try {
            accuseDao.accuseEvent(accuse);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row;
    }
    //管理员查看自己管理瓜圈的举报情况
    public List<Accuse> viewAccusation(int userId){
        List<Accuse> accuses = new ArrayList<Accuse>();
        try {
            accuses = accuseDao.viewAccusation(userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accuses;
    }
    //删除举报
    public int deleteAccuse(int eventId,String accusedContent){
        int row=0;
        Accuse accuse = new Accuse();
        accuse.setEventId(eventId);
        accuse.setAccusedContent(accusedContent);
        try {
            accuseDao.deleteAccuse(accuse);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row;
    }
}
