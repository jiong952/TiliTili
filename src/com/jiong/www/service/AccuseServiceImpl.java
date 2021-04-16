package com.jiong.www.service;

import com.jiong.www.dao.AccuseDaoImpl;
import com.jiong.www.dao.IAccuseDao;
import com.jiong.www.po.Accuse;
import java.util.List;

/**
 * @author Mono
 */
public class AccuseServiceImpl implements IAccuseService {
    IAccuseDao iAccuseDao = new AccuseDaoImpl();
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
        accuses = iAccuseDao.findAll(userId);
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
            rowData[i][3]=accuses.get(i).getAccuseTime();
        }
        return rowData;
    }
}
