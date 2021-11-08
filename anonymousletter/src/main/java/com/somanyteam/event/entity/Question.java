package com.somanyteam.event.entity;

import java.util.Date;

public class Question {
    private Long id;

    private String qId;

    private String aId;

    private Integer answerStatus;

    private Date createTime;

    private Date updateTime;

    private String content;

    private String parentQuestion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getqId() {
        return qId;
    }

    public void setqId(String qId) {
        this.qId = qId == null ? null : qId.trim();
    }

    public String getaId() {
        return aId;
    }

    public void setaId(String aId) {
        this.aId = aId == null ? null : aId.trim();
    }

    public Integer getAnswerStatus() {
        return answerStatus;
    }

    public void setAnswerStatus(Integer answerStatus) {
        this.answerStatus = answerStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getParentQuestion() {
        return parentQuestion;
    }

    public void setParentQuestion(String parentQuestion) {
        this.parentQuestion = parentQuestion == null ? null : parentQuestion.trim();
    }
}