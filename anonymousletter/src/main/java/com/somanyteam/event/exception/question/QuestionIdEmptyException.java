package com.somanyteam.event.exception.question;

public class QuestionIdEmptyException extends QuestionException {

    private static final long serialVersionUID = 1L;

    public QuestionIdEmptyException() {
        super("question.id.empty", null);
    }
}
