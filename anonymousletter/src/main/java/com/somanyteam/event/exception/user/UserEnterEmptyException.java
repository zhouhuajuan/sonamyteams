package com.somanyteam.event.exception.user;

public class UserEnterEmptyException extends UserException {

    private static final long serialVersionUID = 1L;

    public UserEnterEmptyException() {
        super("user.enter.empty", null);
    }
}
