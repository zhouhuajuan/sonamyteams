package com.somanyteam.event.dto.result.blacklist;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@ApiModel("获取黑名单列表结果集")
@Data
public class GetBlacklistResult {

    @ApiModelProperty("问题id")
    private Long questionId;

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("拉黑时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date time;

    @ApiModelProperty("被拉黑问题的内容")
    private String content;


}
