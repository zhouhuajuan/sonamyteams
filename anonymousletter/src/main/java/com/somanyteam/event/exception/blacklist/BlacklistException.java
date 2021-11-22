package com.somanyteam.event.exception.blacklist;


import com.somanyteam.event.exception.BaseException;

public class BlacklistException extends BaseException {

    private static final long serialVersionUID = 1L;

    public BlacklistException(String code, Object[] args) {
        super("blacklist", code, args, null);
    }
}
