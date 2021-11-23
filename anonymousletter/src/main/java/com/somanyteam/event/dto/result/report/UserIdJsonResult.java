package com.somanyteam.event.dto.result.report;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("从JSON获取被举报者id结果集")
@Data
public class UserIdJsonResult {

    @ApiModelProperty("被举报者id")
    private String userId;

}
