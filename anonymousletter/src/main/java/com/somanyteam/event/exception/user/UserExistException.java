package com.somanyteam.event.exception.user;

public class UserExistException extends UserException {

    private static final long serialVersionUID = 1L;

    public UserExistException() {
        super("user.exist", null);
    }
}
