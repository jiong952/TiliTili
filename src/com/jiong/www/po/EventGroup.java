package com.jiong.www.po;

import java.util.List;

/**
 * @author Mono
 */
public class EventGroup {
    /**瓜圈名称*/
    private String eventGroupName;
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
    public String getEventGroupName() {
        return eventGroupName;
    }

    public void setEventGroupName(String eventGroupName) {
        this.eventGroupName = eventGroupName;
    }

    public String getEventGroupDescription() {
        return eventGroupDescription;
    }

    public void setEventGroupDescription(String eventGroupDescription) {
        this.eventGroupDescription = eventGroupDescription;
    }
}
