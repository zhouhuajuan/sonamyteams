package com.somanyteam.event.exception.question;

import com.somanyteam.event.exception.user.UserException;

public class UserIdIsEmptyException extends QuestionException {

    private static final long serialVersionUID = 1L;

    public UserIdIsEmptyException() {
        super("question.userId.is.empty", null);
    }
}
