package com.somanyteam.event.service;

import com.somanyteam.event.dto.result.report.GetHandledReportListResult;
import com.somanyteam.event.dto.result.report.GetReportContentResult;
import com.somanyteam.event.entity.User;

import java.util.List;

public interface ReportService {

    List<GetHandledReportListResult> getHandledReportList(User curUser);

    GetReportContentResult getReportContent(Long id);
}
