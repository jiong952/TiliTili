package com.jiong.www.service.Iservice;

import com.jiong.www.po.Accuse;

import java.util.List;

/**
 * @author Mono
 */
public interface IAccuseService {
    /**用户可以举报瓜，给管理员处理
     * @param eventId 举报的瓜id，
     * @param userId 举报人的id
     * @param accusedContent 举报内容
     * @return 举报成功的判断
     * */
    int doAccuse(int eventId, int userId, String accusedContent);
    /**
     * 管理员查看自己管理瓜圈的举报情况
     * @param userId 管理员的id
     * @return 管理的瓜圈所有的举报信息*/
    List<Accuse> findAll(int userId);
    /**
     * 删除举报信息
     * @param eventId 被举报的瓜id
     * @param accusedContent 举报内容
     * */
    void doDelete(int eventId, String accusedContent);
    /**刷新列表信息
     * @param userId 管理员id
     * @return 返回列表信息
     * */
    Object[][] doRefresh(int userId);
}
