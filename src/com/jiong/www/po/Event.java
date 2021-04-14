package com.jiong.www.po;

import java.util.Date;

/**
 * @author Mono
 */
public class Event {
    /**瓜的内容*/
    private String eventContent;
    /**瓜的名称*/
    private String eventName;
    /**瓜的发布者名字*/
    private String publisherName;
    /**瓜的发布时间*/
    private Date createTime;
    /**瓜的点赞数*/
    private int likesNum;
    /**瓜的评论数*/
    private int commentNum;
    /**瓜的收藏数*/
    private int collectionNum;
    /**瓜id*/
    private int eventId;

    public String getEventContent() {
        return eventContent;
    }

    public void setEventContent(String eventContent) {
        this.eventContent = eventContent;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public int getLikesNum() {
        return likesNum;
    }

    public void setLikesNum(int likesNum) {
        this.likesNum = likesNum;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public int getCollectionNum() {
        return collectionNum;
    }

    public void setCollectionNum(int collectionNum) {
        this.collectionNum = collectionNum;
    }
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

}
