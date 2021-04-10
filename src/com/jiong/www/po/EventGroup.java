package com.jiong.www.po;

import java.util.List;

/**
 * @author Mono
 */
public class EventGroup {
    private String eventGroupName;
    //瓜圈名称
    private String eventGroupDescription;
    //瓜圈简介
    private int eventGroupId;

    public int getEventGroupId() {
        return eventGroupId;
    }

    public void setEventGroupId(int eventGroupId) {
        this.eventGroupId = eventGroupId;
    }

    //瓜圈Id
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
