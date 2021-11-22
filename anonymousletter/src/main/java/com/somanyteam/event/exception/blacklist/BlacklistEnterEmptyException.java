package com.somanyteam.event.exception.blacklist;

public class BlacklistEnterEmptyException extends BlacklistException {

    private static final long serialVersionUID = 1L;

    public BlacklistEnterEmptyException() {
        super("blacklist.enter.empty", null);
    }
}
