package com.somanyteam.event.dto.result.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("用户登录信息结果")
public class UserLoginResult {

    @ApiModelProperty("用户唯一id")
    private String id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("身份 0-普通用户 1-管理员")
    private Byte identity;

    @ApiModelProperty("头像链接")
    private String imgUrl;
}
