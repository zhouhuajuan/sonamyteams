package com.somanyteam.event.exception.report;


import com.somanyteam.event.exception.BaseException;

public class ReportException extends BaseException {

    private static final long serialVersionUID = 1L;

    public ReportException(String code, Object[] args) {
        super("report", code, args, null);
    }
}
