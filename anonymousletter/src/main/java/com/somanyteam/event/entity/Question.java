package com.somanyteam.event.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class Question {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String qId;

    private String aId;

    private Byte answerStatus;

    private Date createTime;

    private Date updateTime;

    private String content;

    private Long parentQuestion;

    private Byte delFlag;


}