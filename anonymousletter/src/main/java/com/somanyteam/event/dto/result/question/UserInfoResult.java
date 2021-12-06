package com.somanyteam.event.dto.result.question;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("用户信息结果")
@Data
public class UserInfoResult {
    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("简介")
    private String profile;

    @ApiModelProperty(value = "性别,0-男 1-女 可不填", required = true)
    private Byte sex;

    @ApiModelProperty("头像链接")
    private String imgUrl;

}
