package com.somanyteam.event.service;

import com.somanyteam.event.dto.request.report.AddReportReqDTO;
import com.somanyteam.event.dto.request.report.HandleReportReqDTO;
import com.somanyteam.event.dto.result.report.GetReportListResult;
import com.somanyteam.event.dto.result.report.GetReportContentResult;
import com.somanyteam.event.entity.User;

import java.util.List;

public interface ReportService {

    List<GetReportListResult> getReportList(User curUser,Integer type);

    int addReport(AddReportReqDTO addReportReqDTO, String userId);

    int handleReport(HandleReportReqDTO handleReportReqDTO,String userId);

    GetReportContentResult getReportContent(Long id);
}
