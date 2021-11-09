package com.somanyteam.event.exception.user;

public class UserEmailNotMatchException extends UserException{
    private static final long serialVersionUID = 1L;
    public UserEmailNotMatchException() {
        super("user.email.not.match", null);
    }
}
