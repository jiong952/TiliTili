package com.jiong.www.service.service;

import com.jiong.www.po.Event;

/**
 * @author Mono
 */
public interface IEventService {
    /**创建瓜，添加瓜信息到瓜表
     * @param userId 创键用户的id
     * @param  eventGroupId 瓜所在瓜圈id
     * @param  eventName 瓜名
     * @param  eventContent 瓜内容
     * @return 判断是否成功*/
    int doCreate(int userId, int eventGroupId, String eventName, String eventContent);
    /**验证瓜名是否存在
     * @param eventName 验证的瓜名
     * @return 判断是否操作成功
     * */
    int verifyExist(String eventName);
    /**删除瓜时验证是不是用户发的瓜
     * @param userId 用户id
     * @param eventId 瓜id
     * @return 判断是否操作成功
     * */
    int doVerify(int userId, int eventId);
    /**查询这个瓜所在的瓜圈名
     * @param eventId 瓜id
     * @return 瓜圈名
     * */
    String queryGroupName(int eventId);
    /**删除瓜
     * @param eventId 删除的瓜id
     * @return 判断是否删除成功
     * */
    int doDelete(int eventId);
    /**查看所有瓜
     * @param eventName 瓜名
     * @return 瓜信息
     * */
    Event doView(String eventName);
}
