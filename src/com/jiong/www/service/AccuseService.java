package com.jiong.www.service;

import com.jiong.www.dao.IAccuseDaoImpl;
import com.jiong.www.po.Accuse;
import java.util.List;

/**
 * @author Mono
 */
public class AccuseService {
    IAccuseDaoImpl accuseDaoImpl =new IAccuseDaoImpl();
    /**用户可以举报瓜，给管理员处理*/
    public int accuseEvent(int eventId,int userId,String accusedContent){
        int row;
        Accuse accuse = new Accuse();
        accuse.setEventId(eventId);
        accuse.setAccusedUserId(userId);
        accuse.setAccusedContent(accusedContent);
        row= accuseDaoImpl.accuseEvent(accuse);
        return row;
    }
    /**管理员查看自己管理瓜圈的举报情况*/
    public List<Accuse> viewAccusation(int userId){
        List<Accuse> accuses;
        accuses = accuseDaoImpl.viewAccusation(userId);
        return accuses;
    }
    /**删除举报信息*/
    public void deleteAccuse(int eventId,String accusedContent){
        Accuse accuse = new Accuse();
        accuse.setEventId(eventId);
        accuse.setAccusedContent(accusedContent);
            accuseDaoImpl.deleteAccuse(accuse);
    }
}
