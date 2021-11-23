package com.somanyteam.event.service.impl;

import com.somanyteam.event.mapper.ReportMapper;
import com.somanyteam.event.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportMapper reportMapper;
}
