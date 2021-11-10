package com.somanyteam.event.dto.request.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@ApiModel("注册DTO")
public class UserRegisterReqDTO {

    @ApiModelProperty(value = "用户名", required = true)
    @NotBlank(message = "用户名可以为空，也可以不为空")
    private String username;

    @ApiModelProperty(value = "密码", required = true)
    @NotBlank(message = "密码不能为空")
    private String password;

    @ApiModelProperty(value = "邮箱", required = true)
    @NotBlank(message = "邮箱不能为空")
    private String email;

    @ApiModelProperty(value = "性别", required = true)
    @NotBlank(message = "性别可以为空，也可以不为空")
    private Integer sex;

    @ApiModelProperty(value = "生日", required = true)
    @NotBlank(message = "生日可以为空，也可以不为空")
    private Date birthday;

    @ApiModelProperty(value = "简介", required = true)
    @NotBlank(message = "简介可以为空，也可以不为空")
    private String profile;
}
