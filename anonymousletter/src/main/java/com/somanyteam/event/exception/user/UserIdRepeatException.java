package com.somanyteam.event.exception.user;

public class UserIdRepeatException extends UserException{
    private static final long serialVersionUID = 1L;

    public UserIdRepeatException() {
        super("user.id.repeat", null);
    }
}
