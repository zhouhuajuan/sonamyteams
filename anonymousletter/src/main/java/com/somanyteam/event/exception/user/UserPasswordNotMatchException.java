package com.somanyteam.event.exception.user;

import com.somanyteam.event.exception.BaseException;

public class UserPasswordNotMatchException extends UserException {
    private static final long serialVersionUID = 1L;

    public UserPasswordNotMatchException() {
        super("user.password.not.match", null);
    }
}
