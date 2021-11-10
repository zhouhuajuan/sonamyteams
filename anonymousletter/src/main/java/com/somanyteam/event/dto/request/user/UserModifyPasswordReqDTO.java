package com.somanyteam.event.dto.request.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("用户修改密码请求DTO")
public class UserModifyPasswordReqDTO {

    @ApiModelProperty(value = "原密码", required = true)
    private String originalPassword;

    @ApiModelProperty(value = "新密码", required = true)
    private String newPassword;

    @ApiModelProperty(value = "确认密码", required = true)
    private String confirmPassword;
}
