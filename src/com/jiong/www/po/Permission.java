package com.jiong.www.po;
/**
 * @author Mono
 */
public class Permission {
    private String permissionName;
    //权限名字
    private String permissionDescription;
    //权限说明

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getPermissionDescription() {
        return permissionDescription;
    }

    public void setPermissionDescription(String permissionDescription) {
        this.permissionDescription = permissionDescription;
    }
}
