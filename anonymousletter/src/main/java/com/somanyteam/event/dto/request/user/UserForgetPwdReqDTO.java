package com.somanyteam.event.dto.request.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("忘记密码DTO")
public class UserForgetPwdReqDTO {

    @ApiModelProperty(value = "邮箱", required = true)
    @NotBlank(message = "邮箱不能为空")
    private String email;

    @ApiModelProperty(value = "验证码", required = true)
    @NotBlank(message = "验证码不能为空")
    private String code;

    @ApiModelProperty(value = "修改密码", required = true)
    @NotBlank(message = "修改密码不能为空")
    private String modifyPwd;

    @ApiModelProperty(value = "确认密码", required = true)
    @NotBlank(message = "确认密码不能为空")
    private String confirmPwd;
}
