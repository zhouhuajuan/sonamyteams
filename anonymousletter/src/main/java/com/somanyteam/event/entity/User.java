package com.somanyteam.event.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;

    private String username;

    private Byte identity;

    private String password;

    private String salt;

    private String email;

    private Byte sex;

    private Date birthday;

    private String profile;

    private String imgUrl;

    private Date createTime;

    private Date updateTime;

   }