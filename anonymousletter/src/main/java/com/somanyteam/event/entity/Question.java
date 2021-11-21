package com.somanyteam.event.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Question {
    private Long id;

    private String qId;

    private String aId;

    private Byte answerStatus;

    private Date createTime;

    private Date updateTime;

    private String content;

    private String parentQuestion;

    private Byte delFlag;


}