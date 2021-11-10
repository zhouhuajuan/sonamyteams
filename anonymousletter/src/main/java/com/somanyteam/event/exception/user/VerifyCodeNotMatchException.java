package com.somanyteam.event.exception.user;

public class VerifyCodeNotMatchException extends UserException{
    public VerifyCodeNotMatchException() {
        super("user.verifycode.not.match", null);
    }
}
