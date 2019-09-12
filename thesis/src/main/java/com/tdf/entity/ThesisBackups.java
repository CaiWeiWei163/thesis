package com.tdf.entity;

import java.util.Date;

public class ThesisBackups {
    private String id;

    private String backupsList;

    private String fileinfoId;

    private Integer state;

    private String createUser;

    private Date createTime;

    private String updateUser;

    private Date updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getBackupsList() {
        return backupsList;
    }

    public void setBackupsList(String backupsList) {
        this.backupsList = backupsList == null ? null : backupsList.trim();
    }

    public String getFileinfoId() {
        return fileinfoId;
    }

    public void setFileinfoId(String fileinfoId) {
        this.fileinfoId = fileinfoId == null ? null : fileinfoId.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser == null ? null : updateUser.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}