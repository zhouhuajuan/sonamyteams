package com.somanyteam.event.exception.report;

public class ReportEnterEmptyException extends ReportException {

    private static final long serialVersionUID = 1L;

    public ReportEnterEmptyException() {
        super("report.enter.empty", null);
    }
}
