package com.somanyteam.event.controller;

import com.somanyteam.event.dto.result.report.GetHandledReportListResult;
import com.somanyteam.event.entity.User;
import com.somanyteam.event.service.ReportService;
import com.somanyteam.event.util.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "举报相关接口")
//@RequestMapping("/report")
@RestController
public class ReportController {

    @Autowired
    private ReportService reportService;

    @ApiOperation("查看自己已处理的举报列表")
    @GetMapping("/admin/report/handled")
    public ResponseMessage<List<GetHandledReportListResult>> getHandledReportList(){
        Subject subject = SecurityUtils.getSubject();
        return ResponseMessage.newSuccessInstance(reportService.getHandledReportList((User) subject.getPrincipal()), "获取成功");
    }

}
