package com.somanyteam.event.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;
@Data
public class Report {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String complaintant;

    private String defendant;

    private Byte type;

    private Byte status;

    private String handler;

    private Date createTime;

    private Date updateTime;

   }