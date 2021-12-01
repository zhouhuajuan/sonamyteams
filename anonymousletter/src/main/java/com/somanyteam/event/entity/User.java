package com.somanyteam.event.entity;

import lombok.Data;

import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
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