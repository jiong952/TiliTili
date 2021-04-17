package com.jiong.www.service.serviceImpl;

import com.jiong.www.dao.EventGroupDao;
import com.jiong.www.dao.daoImpl.AccuseDaoImpl;
import com.jiong.www.dao.Idao.IAccuseDao;
import com.jiong.www.po.Accuse;
import com.jiong.www.po.Event;
import com.jiong.www.service.Iservice.IAccuseService;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

/**
 * @author Mono
 */
public class AccuseServiceImpl implements IAccuseService {
    IAccuseDao iAccuseDao = new AccuseDaoImpl();
    EventGroupDao eventGroupDao = new EventGroupDao();
    /**用户可以举报瓜，给管理员处理*/
    @Override
    public int doAccuse(int eventId, int userId, String accusedContent){
        int row;
        Accuse accuse = new Accuse();
        accuse.setEventId(eventId);
        accuse.setAccusedUserId(userId);
        accuse.setAccusedContent(accusedContent);
        row= iAccuseDao.doAccuse(accuse);
        return row;
    }
    /**管理员查看自己管理瓜圈的举报情况*/
    @Override
    public List<Accuse> findAll(int userId){
        List<Accuse> accuses;
        List<Integer> integers = eventGroupDao.viewAdminGroup(userId);
        List<Event> eventList = eventGroupDao.viewEventGroup(integers);
        accuses=iAccuseDao.findAll(eventList);
        accuses=iAccuseDao.queryName(accuses);
        accuses.sort(Accuse::compareTo);
        return accuses;
    }
    /**删除举报信息*/
    @Override
    public void doDelete(int eventId, String accusedContent){
        Accuse accuse = new Accuse();
        accuse.setEventId(eventId);
        accuse.setAccusedContent(accusedContent);
        iAccuseDao.doDelete(accuse);
    }
    /**刷新举报信息的列表*/
    @Override
    public Object[][] doRefresh(int userId) {
        List<Accuse> accuses = findAll(userId);
        Object[][] rowData = new Object[accuses.size()][4];
        for (int i = 0; i < accuses.size(); i++) {
            rowData[i][0]=accuses.get(i).getAccusedUserName();
            rowData[i][1]=accuses.get(i).getAccusedEventName();
            rowData[i][2]=accuses.get(i).getAccusedContent();
            LocalDate localDate=accuses.get(i).getAccuseTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            Date date = java.sql.Date.valueOf(localDate);
            rowData[i][3]=date;
        }
        return rowData;
    }
}
