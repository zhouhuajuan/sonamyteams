package com.somanyteam.event.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.Date;

public class Blacklist {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String complaintant;

    private String defendant;

    private Long questionId;

    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComplaintant() {
        return complaintant;
    }

    public void setComplaintant(String complaintant) {
        this.complaintant = complaintant == null ? null : complaintant.trim();
    }

    public String getDefendant() {
        return defendant;
    }

    public void setDefendant(String defendant) {
        this.defendant = defendant == null ? null : defendant.trim();
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}