package com.somanyteam.event.service.impl;

import com.somanyteam.event.dto.result.report.GetHandledReportListResult;
import com.somanyteam.event.entity.User;
import com.somanyteam.event.mapper.ReportMapper;
import com.somanyteam.event.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportMapper reportMapper;

    @Override
    public List<GetHandledReportListResult> getHandledReportList(User curUser) {

        return null;
    }
}
