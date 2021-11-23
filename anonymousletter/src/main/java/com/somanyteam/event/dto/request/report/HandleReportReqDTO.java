package com.somanyteam.event.dto.request.report;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("处理举报问题请求DTO")
@Data
public class HandleReportReqDTO {

    @ApiModelProperty("被举报问题id")
    private Long id;

    @ApiModelProperty("被举报者id的json")
    private String userId;

    @ApiModelProperty("处理举报操作")
    private Byte status;
}
