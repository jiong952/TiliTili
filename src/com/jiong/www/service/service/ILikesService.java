package com.jiong.www.service.service;

import com.jiong.www.po.Event;

import javax.swing.*;
import java.util.List;

/**
 * @author Mono
 */
public interface ILikesService {
    /**点赞,同时更新用户点赞表
     * * @param userId 点赞人id
     * * @param eventId 点赞瓜id
     */
    void doLikes(int userId, int eventId);
    /**取消点赞,同时删除用户点赞表中的相关数据
     * @param userId 取消点赞人id
     * @param eventId 取消点赞瓜id
     * */
    void doCancelLikes(int userId, int eventId);
    /**查看用户是否点赞
     * @param userId 用户id
     * @param eventId 查看的瓜
     * @return 查看判断
     * */
    int queryLikes(int userId, int eventId);
    /**查看点赞合集 每个瓜只展示事件标题 作者 发布时间 点赞量 收藏量 评论量
     * @param userId 用户id
     * @return 返回查询结果*/
    List<Event> findAll(int userId);
    /**刷新列表信息
     * @param userId 用户id
     * @return 刷新后的数据源
     * */
    DefaultListModel<String> doRefresh(int userId);
}
