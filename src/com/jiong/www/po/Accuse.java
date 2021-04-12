package com.jiong.www.po;

import java.util.Date;

public class Accuse {

    private int accusedUserId;
    //举报人的id
    private String accusedUserName;
    //举报人的名字
    private Date accuseTime;
    //举报时间
    private String accusedContent;
    //举报内容
    private String accusedEventName;
    //举报瓜的名字
    private int eventId;
    //举报瓜的id
    private int userId;
    //举报人的id

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getAccusedUserId() {
        return accusedUserId;
    }

    public void setAccusedUserId(int accusedUserId) {
        this.accusedUserId = accusedUserId;
    }

    public String getAccusedUserName() {
        return accusedUserName;
    }

    public void setAccusedUserName(String accusedUserName) {
        this.accusedUserName = accusedUserName;
    }

    public Date getAccuseTime() {
        return accuseTime;
    }

    public void setAccuseTime(Date accuseTime) {
        this.accuseTime = accuseTime;
    }

    public String getAccusedContent() {
        return accusedContent;
    }

    public void setAccusedContent(String accusedContent) {
        this.accusedContent = accusedContent;
    }

    public String getAccusedEventName() {
        return accusedEventName;
    }

    public void setAccusedEventName(String accusedEventName) {
        this.accusedEventName = accusedEventName;
    }
}