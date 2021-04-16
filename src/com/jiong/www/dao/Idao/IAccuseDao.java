package com.jiong.www.dao.Idao;

import com.jiong.www.po.Accuse;

import java.util.List;


/**
 * @author Mono
 */
public interface IAccuseDao {
    /**用户举报瓜
     * @param accuse accuse对象传入举报信息
     * @return row 返回值用于判断举报是否成功
     * */
    int doAccuse(Accuse accuse);
    /**管理员查看自己管理瓜圈的举报情况
     * @param  userId 用户id
     * @return 返回管理员的管理瓜圈的举报信息list
     * */
    List<Accuse> findAll(int userId) ;
    /**删除举报
     * @param accuse 传入要删除举报信息的accuse对象
     * */
    void doDelete(Accuse accuse) ;
}
