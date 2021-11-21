package com.somanyteam.event.entity;

import lombok.Data;

import java.util.Date;
@Data
public class Report {
    private Long id;

    private String complaintant;

    private String defendant;

    private Byte type;

    private Byte status;

    private String handler;

    private Date createTime;

    private Date updateTime;

   }