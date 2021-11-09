package com.somanyteam.event.exception.user;

public class UserNotExistException extends UserException {

    private static final long serialVersionUID = 1L;

    public UserNotExistException() {
        super("user.not.exist", null);
    }
}
