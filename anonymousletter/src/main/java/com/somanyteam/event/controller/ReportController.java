package com.somanyteam.event.controller;

import com.somanyteam.event.dto.request.report.AddReportReqDTO;
import com.somanyteam.event.dto.request.report.HandleReportReqDTO;
import com.somanyteam.event.dto.result.report.GetReportContentResult;
import com.somanyteam.event.dto.result.report.GetReportListResult;
import com.somanyteam.event.entity.User;
import com.somanyteam.event.service.ReportService;
import com.somanyteam.event.util.ResponseMessage;
import com.somanyteam.event.util.ShiroUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "举报相关接口")
//@RequestMapping("/report")
@RestController
public class ReportController {

    @Autowired
    private ReportService reportService;

    @RequiresAuthentication
    @ApiOperation("根据type查看自己举报处理列表（0-未处理，1-已处理）")
    @GetMapping("/admin/report/list/{type}")
    public ResponseMessage<List<GetReportListResult>> getReportList(@PathVariable("type") Integer type){
//        Subject subject = SecurityUtils.getSubject();
        return ResponseMessage.newSuccessInstance(reportService.getReportList(ShiroUtil.getUser(), type), "获取成功");
    }

    @ApiOperation("用户根据问题id进行举报")
    @PostMapping("/user/report")
    public ResponseMessage addReport(@RequestBody @Validated AddReportReqDTO addReportReqDTO){
        User loginUser = (User) SecurityUtils.getSubject().getPrincipal();
        String loginUserId = loginUser == null ? null : loginUser.getId();
        int i = reportService.addReport(addReportReqDTO, loginUserId);
        return i>0 ? ResponseMessage.newSuccessInstance("举报成功") : ResponseMessage.newErrorInstance("举报失败");
    }

    @RequiresAuthentication
    @ApiOperation("处理举报内容")
    @PostMapping("/admin/report/handle")
    public ResponseMessage handleReport(@RequestBody @Validated HandleReportReqDTO handleReportReqDTO){
        User loginUser = ShiroUtil.getUser();
        String loginUserId = loginUser.getId();
        int i = reportService.handleReport(handleReportReqDTO, loginUserId);
        return i>0 ? ResponseMessage.newSuccessInstance("处理完成") : ResponseMessage.newErrorInstance("处理失败");
    }

    @RequiresAuthentication
    @ApiOperation("管理员查看举报的具体内容")
    @GetMapping("/admin/report/{id}")
    public ResponseMessage<GetReportContentResult> getReportContent(@PathVariable("id") Long id){

        return ResponseMessage.newSuccessInstance(reportService.getReportContent(id), "获取成功");
    }

}
