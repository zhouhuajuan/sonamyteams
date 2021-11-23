package com.somanyteam.event.controller;

import com.somanyteam.event.service.ReportService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "举报相关接口")
@RequestMapping("/report")
@RestController
public class ReportController {

    @Autowired
    private ReportService reportService;

}
