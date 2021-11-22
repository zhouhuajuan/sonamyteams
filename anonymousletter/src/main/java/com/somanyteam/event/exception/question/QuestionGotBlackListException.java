package com.somanyteam.event.exception.question;

public class QuestionGotBlackListException extends QuestionException {

    private static final long serialVersionUID = 1L;

    public QuestionGotBlackListException() {
        super("question.got.blacklist", null);
    }
}
