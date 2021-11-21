package com.somanyteam.event.exception.question;

public class QuestionNotAnsweredException extends QuestionException {

    private static final long serialVersionUID = 1L;

    public QuestionNotAnsweredException() {
        super("question.not.answered", null);
    }
}
