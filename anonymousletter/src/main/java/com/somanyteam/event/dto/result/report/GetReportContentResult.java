package com.somanyteam.event.dto.result.report;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.somanyteam.event.dto.result.question.QuestionAndAnswerListResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@ApiModel("管理员查看举报具体内容结果")
public class GetReportContentResult {

    @ApiModelProperty("举报用户用户名")
    private String complaintant;

    @ApiModelProperty("被举报用户(当第三方去举报时，被举报用户为提问者和被提问者)]")
    private List<String> defendant;

    @ApiModelProperty("举报类型")
    private String type;

    @ApiModelProperty("举报时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date reportTime;

    @ApiModelProperty("提问时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date questionTime;

    @ApiModelProperty("处理状态 0-未处理 1-举报通过 2-举报没有通过")
    private Byte status;

    @ApiModelProperty("问题和答案结果集")
    private QuestionAndAnswerListResult questionAndAnswerResult;



}
