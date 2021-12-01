package com.somanyteam.event.shiro;

import lombok.Data;

import java.io.Serializable;

@Data
public class AccountProfile implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    private String username;

    private String imgUrl;

    private String email;

    private Byte identity;

}