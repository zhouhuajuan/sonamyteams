package com.somanyteam.event.exception.question;

public class QuestionEnterEmptyException extends QuestionException {

    private static final long serialVersionUID = 1L;

    public QuestionEnterEmptyException() {
        super("question.enter.empty", null);
    }
}
