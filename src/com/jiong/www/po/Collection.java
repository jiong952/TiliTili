package com.jiong.www.po;


/**
 * @author Mono
 */
public class Collection {
    /**收藏id*/
    private int id;
    /**收藏人id*/
    private int userId;
    /**收藏瓜id*/
    private int eventId;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

}
