package com.somanyteam.event.exception.user;

public class ConfirmPwdNotMatchException extends UserException{
    private static final long serialVersionUID = 1L;

    public ConfirmPwdNotMatchException() {
        super("user.confirmPwd.not.match", null);
    }
}
