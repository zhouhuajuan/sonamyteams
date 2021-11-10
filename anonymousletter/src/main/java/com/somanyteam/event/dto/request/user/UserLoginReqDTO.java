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

    @ApiModelProperty(value = "记住我 0-表示不记住，1表示记住", required = true)
    private Integer rememberMe;

    @ApiModelProperty(value = "验证码",required = true)
    private String code;
}
