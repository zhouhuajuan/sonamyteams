package com.somanyteam.event.dto.result.report;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@ApiModel("查看管理员已处理的举报的结果集")
@Data
public class GetReportListResult {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("问题内容")
    private String content;

    @ApiModelProperty("问题id")
    private Long questionId;

    @ApiModelProperty("处理时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date time;
}
