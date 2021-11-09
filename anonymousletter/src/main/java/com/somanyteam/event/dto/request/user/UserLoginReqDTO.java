package com.somanyteam.event.dto.request.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@ApiModel("用户登录DTO")
public class UserLoginReqDTO {

    @ApiModelProperty(value = "邮箱", required = true)
    @NotBlank(message = "密码不能为空")
    private String email;

    @ApiModelProperty(value = "密码", required = true)
    @NotBlank(message = "密码不能为空")
    private String password;
}
