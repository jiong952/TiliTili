package com.jiong.www.po;

/**
 * @author Mono
 */
public class EventGroup {
    /**瓜圈名称*/
    private String name;
    /**瓜圈简介*/
    private String eventGroupDescription;
    /**瓜圈id*/
    private int eventGroupId;

    public int getEventGroupId() {
        return eventGroupId;
    }

    public void setEventGroupId(int eventGroupId) {
        this.eventGroupId = eventGroupId;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEventGroupDescription() {
        return eventGroupDescription;
    }

    public void setEventGroupDescription(String eventGroupDescription) {
        this.eventGroupDescription = eventGroupDescription;
    }
}
