package com.jiong.www.po;

import java.util.Date;


/**
 * @author Mono
 */
public class Accuse implements Comparable<Accuse>{
    /**举报人的id*/
    private int accusedUserId;
    /**举报人的名字*/
    private String accusedUserName;
    /**举报时间*/
    private Date accuseTime;
    /**举报内容*/
    private String accusedContent;
    /**举报瓜的名字*/
    private String accusedEventName;
    /**举报瓜的id*/
    private int eventId;

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

    @Override
    public int compareTo(Accuse accuse) {
        return Long.compare(this.getAccuseTime().getTime(), accuse.getAccuseTime().getTime());
    }
}
