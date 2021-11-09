package com.somanyteam.event.exception.user;


import com.somanyteam.event.exception.BaseException;

public class UserException extends BaseException {

    private static final long serialVersionUID = 1L;

    public UserException(String code, Object[] args) {
        super("user", code, args, null);
    }
}
