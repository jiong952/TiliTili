package com.jiong.www.service.serviceImpl;

import com.jiong.www.dao.dao.IEventGroupDao;
import com.jiong.www.dao.daoImpl.EventDaoImpl;
import com.jiong.www.dao.daoImpl.EventGroupDaoImpl;
import com.jiong.www.dao.daoImpl.UserDaoImpl;
import com.jiong.www.dao.daoImpl.AccuseDaoImpl;
import com.jiong.www.dao.dao.IAccuseDao;
import com.jiong.www.po.Accuse;
import com.jiong.www.po.Event;
import com.jiong.www.service.service.IAccuseService;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

/**
 * @author Mono
 */
public class AccuseServiceImpl implements IAccuseService {
    IAccuseDao iAccuseDao = new AccuseDaoImpl();
    IEventGroupDao iEventGroupDao = new EventGroupDaoImpl();
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
        List<Integer> integers = iEventGroupDao.viewAdminGroup(userId);
        List<Event> eventList = iEventGroupDao.viewEventGroup(integers);
        accuses=iAccuseDao.findAll(eventList);
        for(Accuse accuse:accuses){
            accuse.setAccusedUserName(new UserDaoImpl().queryUserInformation(accuse.getAccusedUserId()).getLoginName());
            accuse.setAccusedEventName(new EventDaoImpl().queryName(accuse.getEventId()));
        }
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

    /**
     * 查看用户是否举报过该瓜
     *
     * @param eventId 瓜id
     * @param userId  用户id
     * @return 判断是否存在
     */
    @Override
    public int verifyExist(int eventId, int userId) {
        int judge;
        Accuse accuse = new Accuse();
        accuse.setEventId(eventId);
        accuse.setAccusedUserId(userId);
        judge=iAccuseDao.verifyExist(accuse);
        return judge;
    }
}
