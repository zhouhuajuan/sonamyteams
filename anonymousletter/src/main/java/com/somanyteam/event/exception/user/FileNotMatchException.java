package com.somanyteam.event.exception.user;

public class FileNotMatchException extends UserException{
    private static final long serialVersionUID = 1L;

    public FileNotMatchException() {
        super("user.file.not.match", null);
    }
}
