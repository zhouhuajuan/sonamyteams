package com.somanyteam.event.dto.request.report;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("举报问题请求DTO")
@Data
public class AddReportReqDTO {

    @ApiModelProperty("被举报问题id")
    private Long questionId;

    @ApiModelProperty("被举报类型")
    private Byte type;
}
