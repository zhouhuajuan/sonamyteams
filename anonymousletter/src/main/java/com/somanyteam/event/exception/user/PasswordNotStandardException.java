package com.somanyteam.event.exception.user;

public class PasswordNotStandardException extends UserException{
    private static final long serialVersionUID = 1L;

    public PasswordNotStandardException() {
        super("user.password.not.standard", null);
    }
}
