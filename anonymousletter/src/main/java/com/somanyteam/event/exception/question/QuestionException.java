package com.somanyteam.event.exception.question;


import com.somanyteam.event.exception.BaseException;

public class QuestionException extends BaseException {

    private static final long serialVersionUID = 1L;

    public QuestionException(String code, Object[] args) {
        super("question", code, args, null);
    }
}
